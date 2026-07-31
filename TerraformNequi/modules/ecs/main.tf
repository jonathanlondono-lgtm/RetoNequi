resource "aws_iam_role" "this" {
  name = "${var.app_name}-ecs-execution-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Action    = "sts:AssumeRole"
      Effect    = "Allow"
      Principal = { Service = "ecs-tasks.amazonaws.com" }
    }]
  })

  tags = var.tags
}

resource "aws_iam_role_policy_attachment" "this" {
  role       = aws_iam_role.this.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

resource "aws_iam_policy" "this" {
  name = "${var.app_name}-ecs-secrets-policy"

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Effect = "Allow"
      Action = [
        "ssm:GetParameters",
        "secretsmanager:GetSecretValue",
        "logs:CreateLogGroup",
        "logs:CreateLogStream",
        "logs:PutLogEvents"
      ]
      Resource = "*"
    }]
  })

  tags = var.tags
}

resource "aws_iam_role_policy_attachment" "secrets" {
  role       = aws_iam_role.this.name
  policy_arn = aws_iam_policy.this.arn
}

resource "aws_ecs_cluster" "this" {
  name = "${var.app_name}-cluster"
  tags = var.tags
}

resource "aws_ecs_task_definition" "this" {
  family                   = "${var.app_name}-task"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = var.container_cpu
  memory                   = var.container_memory
  execution_role_arn       = aws_iam_role.this.arn

  container_definitions = jsonencode([{
    name  = var.app_name
    image = "${var.repository_url}:latest"
    portMappings = [{
      containerPort = var.server_port
    }]
    environment = [
      { name = "SPRING_PROFILES_ACTIVE", value = "prod" }
    ]
    secrets = [
      { name = "DB_HOST", valueFrom = var.db_host_arn },
      { name = "DB_PORT", valueFrom = var.db_port_arn },
      { name = "DB_NAME", valueFrom = var.db_name_arn },
      { name = "DB_USER", valueFrom = "${var.db_credentials_arn}:DB_USER::" },
      { name = "DB_PASSWORD", valueFrom = "${var.db_credentials_arn}:DB_PASSWORD::" },
      { name = "CIRCUIT_BREAKER_SLIDING_WINDOW_SIZE", valueFrom = var.circuit_breaker_sliding_window_size_arn },
      { name = "CIRCUIT_BREAKER_FAILURE_RATE_THRESHOLD", valueFrom = var.circuit_breaker_failure_rate_threshold_arn },
      { name = "CIRCUIT_BREAKER_WAIT_DURATION", valueFrom = var.circuit_breaker_wait_duration_arn },
      { name = "CIRCUIT_BREAKER_HALF_OPEN_CALLS", valueFrom = var.circuit_breaker_half_open_calls_arn },
      { name = "CIRCUIT_BREAKER_TIMEOUT", valueFrom = var.circuit_breaker_timeout_arn },
      { name = "RETRY_MAX_ATTEMPTS", valueFrom = var.retry_max_attempts_arn },
      { name = "RETRY_WAIT_DURATION", valueFrom = var.retry_wait_duration_arn }
    ]
    logConfiguration = {
      logDriver = "awslogs"
      options = {
        "awslogs-group"         = "/ecs/${var.app_name}"
        "awslogs-region"        = var.region
        "awslogs-stream-prefix" = "ecs"
        "awslogs-create-group"  = "true"
      }
    }
  }])

  tags = var.tags
}

resource "aws_ecs_service" "this" {
  name                              = "${var.app_name}-service"
  cluster                           = aws_ecs_cluster.this.id
  task_definition                   = aws_ecs_task_definition.this.arn
  launch_type                       = "FARGATE"
  desired_count                     = var.ecs_min_capacity
  health_check_grace_period_seconds = 180

  network_configuration {
    subnets          = var.public_subnet_ids
    security_groups  = [var.ecs_security_group_id]
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = var.target_group_arn
    container_name   = var.app_name
    container_port   = var.server_port
  }

  tags = var.tags
}

resource "aws_appautoscaling_target" "this" {
  max_capacity       = var.ecs_max_capacity
  min_capacity       = var.ecs_min_capacity
  resource_id        = "service/${aws_ecs_cluster.this.name}/${aws_ecs_service.this.name}"
  scalable_dimension = "ecs:service:DesiredCount"
  service_namespace  = "ecs"
}

resource "aws_appautoscaling_policy" "this" {
  name               = "${var.app_name}-cpu-scaling"
  policy_type        = "TargetTrackingScaling"
  resource_id        = aws_appautoscaling_target.this.resource_id
  scalable_dimension = aws_appautoscaling_target.this.scalable_dimension
  service_namespace  = aws_appautoscaling_target.this.service_namespace

  target_tracking_scaling_policy_configuration {
    predefined_metric_specification {
      predefined_metric_type = "ECSServiceAverageCPUUtilization"
    }
    target_value = 70.0
  }
}
