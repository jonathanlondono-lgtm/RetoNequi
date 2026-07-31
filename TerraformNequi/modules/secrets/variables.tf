variable "app_name" {
  type        = string
  description = "Nombre de la aplicación"
}

variable "db_host" {
  type        = string
  description = "Host de la base de datos"
}

variable "db_port" {
  type        = number
  description = "Puerto de la base de datos"
}

variable "db_name" {
  type        = string
  description = "Nombre de la base de datos"
}

variable "db_user" {
  type        = string
  sensitive   = true
  description = "Usuario de la base de datos"
}

variable "db_password" {
  type        = string
  sensitive   = true
  description = "Contraseña de la base de datos"
}

variable "circuit_breaker_sliding_window_size" {
  type        = number
  description = "Tamaño de la ventana deslizante del circuit breaker"
}

variable "circuit_breaker_failure_rate_threshold" {
  type        = number
  description = "Umbral de tasa de fallos del circuit breaker"
}

variable "circuit_breaker_wait_duration" {
  type        = string
  description = "Duración de espera del circuit breaker"
}

variable "circuit_breaker_half_open_calls" {
  type        = number
  description = "Llamadas en estado half-open del circuit breaker"
}

variable "circuit_breaker_timeout" {
  type        = string
  description = "Timeout del circuit breaker"
}

variable "retry_max_attempts" {
  type        = number
  description = "Número máximo de reintentos"
}

variable "retry_wait_duration" {
  type        = string
  description = "Duración de espera entre reintentos"
}

variable "tags" {
  type        = map(string)
  description = "Tags a aplicar a los recursos"
}
