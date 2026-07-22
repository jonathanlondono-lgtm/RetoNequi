variable "app_name" {
  type = string
}

variable "db_host" {
  type = string
}

variable "db_port" {
  type = number
}

variable "db_name" {
  type = string
}

variable "db_user" {
  type = string
}

variable "db_password" {
  type      = string
  sensitive = true
}

variable "circuit_breaker_sliding_window_size" {
  type = number
}

variable "circuit_breaker_failure_rate_threshold" {
  type = number
}

variable "circuit_breaker_wait_duration" {
  type = string
}

variable "circuit_breaker_timeout" {
  type = string
}

variable "retry_max_attempts" {
  type = number
}

variable "retry_wait_duration" {
  type = string
}

variable "circuit_breaker_half_open_calls" {
  type = number
}

