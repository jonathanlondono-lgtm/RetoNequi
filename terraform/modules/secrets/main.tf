resource "aws_ssm_parameter" "db_host" {
  name  = "/${var.app_name}/DB_HOST"
  type  = "String"
  value = var.db_host
}

resource "aws_ssm_parameter" "db_port" {
  name  = "/${var.app_name}/DB_PORT"
  type  = "String"
  value = var.db_port
}

resource "aws_ssm_parameter" "db_name" {
  name  = "/${var.app_name}/DB_NAME"
  type  = "String"
  value = var.db_name
}

resource "aws_ssm_parameter" "circuit_breaker_sliding_window_size" {
  name  = "/${var.app_name}/CIRCUIT_BREAKER_SLIDING_WINDOW_SIZE"
  type  = "String"
  value = var.circuit_breaker_sliding_window_size
}

resource "aws_ssm_parameter" "circuit_breaker_failure_rate_threshold" {
  name  = "/${var.app_name}/CIRCUIT_BREAKER_FAILURE_RATE_THRESHOLD"
  type  = "String"
  value = var.circuit_breaker_failure_rate_threshold
}

resource "aws_ssm_parameter" "circuit_breaker_wait_duration" {
  name  = "/${var.app_name}/CIRCUIT_BREAKER_WAIT_DURATION"
  type  = "String"
  value = var.circuit_breaker_wait_duration
}

resource "aws_ssm_parameter" "circuit_breaker_half_open_calls" {
  name  = "/${var.app_name}/CIRCUIT_BREAKER_HALF_OPEN_CALLS"
  type  = "String"
  value = var.circuit_breaker_half_open_calls
}

resource "aws_ssm_parameter" "circuit_breaker_timeout" {
  name  = "/${var.app_name}/CIRCUIT_BREAKER_TIMEOUT"
  type  = "String"
  value = var.circuit_breaker_timeout
}

resource "aws_ssm_parameter" "retry_max_attempts" {
  name  = "/${var.app_name}/RETRY_MAX_ATTEMPTS"
  type  = "String"
  value = var.retry_max_attempts
}

resource "aws_ssm_parameter" "retry_wait_duration" {
  name  = "/${var.app_name}/RETRY_WAIT_DURATION"
  type  = "String"
  value = var.retry_wait_duration
}

resource "aws_secretsmanager_secret" "db_credentials" {
  name = "/${var.app_name}/db-credentials"
}

resource "aws_secretsmanager_secret_version" "db_credentials" {
  secret_id = aws_secretsmanager_secret.db_credentials.id
  secret_string = jsonencode({
    DB_USER     = var.db_user
    DB_PASSWORD = var.db_password
  })
}

