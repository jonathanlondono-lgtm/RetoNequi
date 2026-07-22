output "db_host_arn" {
  value = aws_ssm_parameter.db_host.arn
}

output "db_port_arn" {
  value = aws_ssm_parameter.db_port.arn
}

output "db_name_arn" {
  value = aws_ssm_parameter.db_name.arn
}

output "db_credentials_arn" {
  value = aws_secretsmanager_secret.db_credentials.arn
}

output "circuit_breaker_sliding_window_size_arn" {
  value = aws_ssm_parameter.circuit_breaker_sliding_window_size.arn
}

output "circuit_breaker_failure_rate_threshold_arn" {
  value = aws_ssm_parameter.circuit_breaker_failure_rate_threshold.arn
}

output "circuit_breaker_wait_duration_arn" {
  value = aws_ssm_parameter.circuit_breaker_wait_duration.arn
}

output "circuit_breaker_half_open_calls_arn" {
  value = aws_ssm_parameter.circuit_breaker_half_open_calls.arn
}

output "circuit_breaker_timeout_arn" {
  value = aws_ssm_parameter.circuit_breaker_timeout.arn
}

output "retry_max_attempts_arn" {
  value = aws_ssm_parameter.retry_max_attempts.arn
}

output "retry_wait_duration_arn" {
  value = aws_ssm_parameter.retry_wait_duration.arn
}

