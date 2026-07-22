module "networking" {
  source      = "./modules/networking"
  vpc_cidr    = var.vpc_cidr
  region      = var.region
  server_port = var.server_port
  db_port     = var.db_port
}

module "ecr" {
  source   = "./modules/ecr"
  app_name = var.app_name
}

module "rds" {
  source                = "./modules/rds"
  app_name              = var.app_name
  db_name               = var.db_name
  db_user               = var.db_user
  db_password           = var.db_password
  db_port               = var.db_port
  db_instance_class     = var.db_instance_class
  private_subnet_ids    = module.networking.private_subnet_ids
  rds_security_group_id = module.networking.rds_security_group_id
}

module "secrets" {
  source                                 = "./modules/secrets"
  app_name                               = var.app_name
  db_host                                = module.rds.db_host
  db_port                                = var.db_port
  db_name                                = var.db_name
  db_user                                = var.db_user
  db_password                            = var.db_password
  circuit_breaker_sliding_window_size    = var.circuit_breaker_sliding_window_size
  circuit_breaker_failure_rate_threshold = var.circuit_breaker_failure_rate_threshold
  circuit_breaker_wait_duration          = var.circuit_breaker_wait_duration
  circuit_breaker_half_open_calls        = var.circuit_breaker_half_open_calls
  circuit_breaker_timeout                = var.circuit_breaker_timeout
  retry_max_attempts                     = var.retry_max_attempts
  retry_wait_duration                    = var.retry_wait_duration
}

module "alb" {
  source            = "./modules/alb"
  app_name          = var.app_name
  vpc_id            = module.networking.vpc_id
  public_subnet_ids = module.networking.public_subnet_ids
  server_port       = var.server_port
}

module "ecs" {
  source                                     = "./modules/ecs"
  app_name                                   = var.app_name
  region                                     = var.region
  container_cpu                              = var.container_cpu
  container_memory                           = var.container_memory
  server_port                                = var.server_port
  repository_url                             = module.ecr.repository_url
  private_subnet_ids                         = module.networking.private_subnet_ids
  ecs_security_group_id                      = module.networking.ecs_security_group_id
  target_group_arn                           = module.alb.target_group_arn
  ecs_min_capacity                           = var.ecs_min_capacity
  ecs_max_capacity                           = var.ecs_max_capacity
  db_host_arn                                = module.secrets.db_host_arn
  db_port_arn                                = module.secrets.db_port_arn
  db_name_arn                                = module.secrets.db_name_arn
  db_credentials_arn                         = module.secrets.db_credentials_arn
  circuit_breaker_sliding_window_size_arn    = module.secrets.circuit_breaker_sliding_window_size_arn
  circuit_breaker_failure_rate_threshold_arn = module.secrets.circuit_breaker_failure_rate_threshold_arn
  circuit_breaker_wait_duration_arn          = module.secrets.circuit_breaker_wait_duration_arn
  circuit_breaker_half_open_calls_arn        = module.secrets.circuit_breaker_half_open_calls_arn
  circuit_breaker_timeout_arn                = module.secrets.circuit_breaker_timeout_arn
  retry_max_attempts_arn                     = module.secrets.retry_max_attempts_arn
  retry_wait_duration_arn                    = module.secrets.retry_wait_duration_arn
}

