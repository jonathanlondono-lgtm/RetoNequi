variable "app_name" {
  type = string
}

variable "region" {
  type = string
}

variable "container_cpu" {
  type = number
}

variable "container_memory" {
  type = number
}

variable "server_port" {
  type = number
}

variable "repository_url" {
  type = string
}

variable "private_subnet_ids" {
  type = list(string)
}

variable "ecs_security_group_id" {
  type = string
}

variable "target_group_arn" {
  type = string
}

variable "ecs_min_capacity" {
  type = number
}

variable "ecs_max_capacity" {
  type = number
}

variable "db_host_arn" {
  type = string
}

variable "db_port_arn" {
  type = string
}

variable "db_name_arn" {
  type = string
}

variable "db_credentials_arn" {
  type = string
}

variable "circuit_breaker_sliding_window_size_arn" {
  type = string
}

variable "circuit_breaker_failure_rate_threshold_arn" {
  type = string
}

variable "circuit_breaker_wait_duration_arn" {
  type = string
}

variable "circuit_breaker_half_open_calls_arn" {
  type = string
}

variable "circuit_breaker_timeout_arn" {
  type = string
}

variable "retry_max_attempts_arn" {
  type = string
}

variable "retry_wait_duration_arn" {
  type = string
}

