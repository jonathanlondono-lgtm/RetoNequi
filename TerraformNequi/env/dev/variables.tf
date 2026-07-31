# Variables generales
variable "region" {
  type        = string
  description = "Region de aws"
}

variable "project_name" {
  type        = string
  description = "Nombre del proyecto"
}

variable "environment" {
  type        = string
  description = "Entorno de despliegue"
}

variable "country" {
  type        = string
  description = "Pais de la aplicacion"
}

variable "capacity" {
  type        = string
  description = "Capacidad tecnica del proyecto"
}

variable "service_id" {
  type        = string
  description = "Service id de la aplicacion"
}

variable "bia" {
  type        = string
  description = "Indica si es proceso critico para el negocio"
}

# ECR
variable "repository_name" {
  type        = string
  description = "Nombre del repositorio ECR"
}

variable "image_tag_mutability" {
  type        = string
  description = "Mutabilidad del tag de imagen"
}

variable "force_delete" {
  type        = bool
  description = "Habilita el borrado forzado del repositorio"
}

variable "scan_on_push" {
  type        = bool
  description = "Habilita el escaneo de imagenes al hacer push"
}

variable "kms_key_arn" {
  type        = string
  description = "ARN o alias de la llave KMS para cifrado"
}

variable "max_image_count" {
  type        = number
  description = "Numero maximo de imagenes a mantener"
}

# Networking
variable "vpc_cidr" {
  type        = string
  description = "CIDR block de la VPC"
}

variable "server_port" {
  type        = number
  description = "Puerto de la aplicacion"
}

variable "db_port" {
  type        = number
  description = "Puerto de la base de datos"
}

# RDS
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
  description = "Contrasena de la base de datos"
}

variable "db_instance_class" {
  type        = string
  description = "Clase de instancia de RDS"
}

# ECS
variable "container_cpu" {
  type        = number
  description = "CPU asignada al contenedor en units"
}

variable "container_memory" {
  type        = number
  description = "Memoria asignada al contenedor en MB"
}

variable "ecs_min_capacity" {
  type        = number
  description = "Capacidad minima del servicio ECS"
}

variable "ecs_max_capacity" {
  type        = number
  description = "Capacidad maxima del servicio ECS"
}

# Circuit Breaker y Retry
variable "circuit_breaker_sliding_window_size" {
  type        = number
  description = "Tamano de la ventana deslizante del circuit breaker"
}

variable "circuit_breaker_failure_rate_threshold" {
  type        = number
  description = "Umbral de tasa de fallos del circuit breaker"
}

variable "circuit_breaker_wait_duration" {
  type        = string
  description = "Duracion de espera del circuit breaker"
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
  description = "Numero maximo de reintentos"
}

variable "retry_wait_duration" {
  type        = string
  description = "Duracion de espera entre reintentos"
}
