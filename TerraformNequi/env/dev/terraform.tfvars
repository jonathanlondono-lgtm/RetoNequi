# Generales
region       = "us-east-1"
project_name = "cocustomermngt"
environment  = "dev"
country      = "co"
capacity     = "pse"
service_id   = "NEQ001"
bia          = "false"

# ECR
repository_name      = "cocustomermngt-dev-service"
image_tag_mutability = "MUTABLE"
force_delete         = true
scan_on_push         = true
kms_key_arn          = "alias/aws/ecr"
max_image_count      = 5

# Networking
vpc_cidr    = "10.0.0.0/16"
server_port = 8080
db_port     = 5432

# RDS
db_name           = "franchises"
db_user           = "postgres"
db_password       = "nequi_password"
db_instance_class = "db.t3.micro"

# ECS
container_cpu    = 256
container_memory = 512
ecs_min_capacity = 1
ecs_max_capacity = 3

# Circuit Breaker y Retry
circuit_breaker_sliding_window_size    = 5
circuit_breaker_failure_rate_threshold = 50
circuit_breaker_wait_duration          = "10s"
circuit_breaker_half_open_calls        = 3
circuit_breaker_timeout                = "3s"
retry_max_attempts                     = 3
retry_wait_duration                    = "500ms"
