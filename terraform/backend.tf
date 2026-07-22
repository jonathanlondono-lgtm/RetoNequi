terraform {
    required_providers {
        aws = {
            source  = "hashicorp/aws"
            version = "~> 5.0"
        }
    }

    backend "s3" {
        bucket = "nequi-terraform-state-jonathan-londono"
        key    = "nequi/terraform.tfstate"
        region = "us-east-1"
    }
}

provider "aws" {
    region = var.region
}
