variable "repository_name" {
    type = string
    description = "The name of the resource"
}

variable "image_tag_mutability" {
    type = string
    description = "The image tag mutability for the repository"  
}

variable "force_delete" {
    type = bool
    description = "Enable force delete for the repository"
}

variable "scan_on_push" {
    type = bool
    description = "Enable scan on push for the repository"
}

variable "kms_key_arn" {
    type = string
    description = "The KMS key to use for encryption"
}

variable "max_image_count" {
    type = number
    description = "The maximum number of images to keep in the repository"
}

variable "tags" {
    type = map(string)
    description = "A map of tags to apply to the resource"  
}


