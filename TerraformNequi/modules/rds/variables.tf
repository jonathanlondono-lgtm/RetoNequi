variable "app_name" {
  type        = string
  description = "Nombre de la aplicación"
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

variable "db_port" {
  type        = number
  description = "Puerto de la base de datos"
}

variable "db_instance_class" {
  type        = string
  description = "Clase de instancia de RDS"
}

variable "private_subnet_ids" {
  type        = list(string)
  description = "IDs de las subnets privadas"
}

variable "rds_security_group_id" {
  type        = string
  description = "ID del security group de RDS"
}

variable "tags" {
  type        = map(string)
  description = "Tags a aplicar a los recursos"
}
