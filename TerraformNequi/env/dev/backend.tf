terraform {
  backend "s3" {
    bucket  = "nequi-terraform-state-dev"
    key     = "cocustomermngt/dev/terraform.tfstate"
    region  = "us-east-1"
    encrypt = true
  }
}
