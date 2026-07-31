variable "vpc_cidr" {
  type        = string
  description = "CIDR block de la VPC"
}

variable "region" {
  type        = string
  description = "Región de AWS"
}

variable "server_port" {
  type        = number
  description = "Puerto de la aplicación"
}

variable "db_port" {
  type        = number
  description = "Puerto de la base de datos"
}

variable "tags" {
  type        = map(string)
  description = "Tags a aplicar a los recursos"
}
