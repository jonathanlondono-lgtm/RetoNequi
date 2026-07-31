variable "app_name" {
  type        = string
  description = "Nombre de la aplicación"
}

variable "vpc_id" {
  type        = string
  description = "ID de la VPC"
}

variable "public_subnet_ids" {
  type        = list(string)
  description = "IDs de las subnets públicas"
}

variable "server_port" {
  type        = number
  description = "Puerto de la aplicación"
}

variable "alb_security_group_id" {
  type        = string
  description = "ID del security group del ALB"
}

variable "tags" {
  type        = map(string)
  description = "Tags a aplicar a los recursos"
}
