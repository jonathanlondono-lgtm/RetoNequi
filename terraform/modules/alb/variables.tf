variable "app_name" {
  type = string
}

variable "public_subnet_ids" {
  type = list(string)
}

variable "vpc_id" {
  type = string
}

variable "server_port" {
  type = number
}

variable "alb_security_group_id" {
  type = string
}

