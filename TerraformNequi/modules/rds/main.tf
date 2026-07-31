resource "aws_db_subnet_group" "this" {
  name       = "${var.app_name}-subnet-group"
  subnet_ids = var.private_subnet_ids

  tags = merge(var.tags, {
    Name = "${var.app_name}-subnet-group"
  })
}

resource "aws_db_instance" "this" {
  identifier        = "${var.app_name}-db"
  engine            = "postgres"
  engine_version    = "15"
  instance_class    = var.db_instance_class
  allocated_storage = 20

  db_name  = var.db_name
  username = var.db_user
  password = var.db_password
  port     = var.db_port

  db_subnet_group_name   = aws_db_subnet_group.this.name
  vpc_security_group_ids = [var.rds_security_group_id]

  skip_final_snapshot = true
  publicly_accessible = false

  tags = merge(var.tags, {
    Name = "${var.app_name}-db"
  })
}
