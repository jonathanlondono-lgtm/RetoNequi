variable "region" {
    type    = string
    default = "us-east-1"
}

variable "app_name" {
    type    = string
    default = "nequi"
}

variable "vpc_cidr" {
    type    = string
    default = "10.0.0.0/16"
}

variable "db_name" {
    type    = string
    default = "franchises"
}

variable "db_user" {
    type    = string
    default = "postgres"
}

variable "db_password" {
    type        = string
    description = "Database password"
}

variable "db_port" {
    type    = number
    default = 5432
}

variable "db_instance_class" {
    type    = string
    default = "db.t3.micro"
}

variable "server_port" {
    type    = number
    default = 8080
}

variable "container_cpu" {
    type    = number
    default = 256
}

variable "container_memory" {
    type    = number
    default = 512
}

variable "ecs_min_capacity" {
    type    = number
    default = 1
}

variable "ecs_max_capacity" {
    type    = number
    default = 3
}

variable "circuit_breaker_sliding_window_size" {
    type    = number
    default = 5
}

variable "circuit_breaker_failure_rate_threshold" {
    type    = number
    default = 50
}

variable "circuit_breaker_wait_duration" {
    type    = string
    default = "10s"
}

variable "circuit_breaker_timeout" {
    type    = string
    default = "3s"
}

variable "retry_max_attempts" {
    type    = number
    default = 3
}

variable "retry_wait_duration" {
    type    = string
    default = "500ms"
}

variable "circuit_breaker_half_open_calls" {
  type    = number
  default = 3
}

