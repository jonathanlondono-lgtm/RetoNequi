variable "app_name" {
  type        = string
  description = "Nombre de la aplicación"
}

variable "region" {
  type        = string
  description = "Región de AWS"
}

variable "container_cpu" {
  type        = number
  description = "CPU asignada al contenedor en units"
}

variable "container_memory" {
  type        = number
  description = "Memoria asignada al contenedor en MB"
}

variable "server_port" {
  type        = number
  description = "Puerto de la aplicación"
}

variable "repository_url" {
  type        = string
  description = "URL del repositorio ECR"
}

variable "public_subnet_ids" {
  type        = list(string)
  description = "IDs de las subnets públicas"
}

variable "ecs_security_group_id" {
  type        = string
  description = "ID del security group de ECS"
}

variable "target_group_arn" {
  type        = string
  description = "ARN del target group del ALB"
}

variable "ecs_min_capacity" {
  type        = number
  description = "Capacidad mínima del servicio ECS"
}

variable "ecs_max_capacity" {
  type        = number
  description = "Capacidad máxima del servicio ECS"
}

variable "db_host_arn" {
  type        = string
  description = "ARN del parámetro SSM con el host de la BD"
}

variable "db_port_arn" {
  type        = string
  description = "ARN del parámetro SSM con el puerto de la BD"
}

variable "db_name_arn" {
  type        = string
  description = "ARN del parámetro SSM con el nombre de la BD"
}

variable "db_credentials_arn" {
  type        = string
  description = "ARN del secreto con las credenciales de la BD"
}

variable "circuit_breaker_sliding_window_size_arn" {
  type        = string
  description = "ARN del parámetro SSM del circuit breaker sliding window"
}

variable "circuit_breaker_failure_rate_threshold_arn" {
  type        = string
  description = "ARN del parámetro SSM del circuit breaker failure rate"
}

variable "circuit_breaker_wait_duration_arn" {
  type        = string
  description = "ARN del parámetro SSM del circuit breaker wait duration"
}

variable "circuit_breaker_half_open_calls_arn" {
  type        = string
  description = "ARN del parámetro SSM del circuit breaker half open calls"
}

variable "circuit_breaker_timeout_arn" {
  type        = string
  description = "ARN del parámetro SSM del circuit breaker timeout"
}

variable "retry_max_attempts_arn" {
  type        = string
  description = "ARN del parámetro SSM de los reintentos máximos"
}

variable "retry_wait_duration_arn" {
  type        = string
  description = "ARN del parámetro SSM de la duración de espera entre reintentos"
}

variable "tags" {
  type        = map(string)
  description = "Tags a aplicar a los recursos"
}
