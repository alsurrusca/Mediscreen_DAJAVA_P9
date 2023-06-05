CREATE TABLE IF NOT EXISTS patient (
  id tinyint NOT NULL AUTO_INCREMENT,
  last_name VARCHAR(255) NOT NULL,
  first_name VARCHAR(255) NOT NULL,
  birth_date DATE NOT NULL,
  gender VARCHAR(1) NOT NULL,
  address VARCHAR(255) NOT NULL,
  phone VARCHAR(12) NOT NULL
);