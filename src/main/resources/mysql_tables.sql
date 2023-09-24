CREATE TABLE mydb.user (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(25),
  login VARCHAR(15) NOT NULL,
  password BLOB NOT NULL,
  created DATE NOT NULL,
  PRIMARY KEY (id)
  );

CREATE TABLE mydb.product_map (
  id INT NOT NULL AUTO_INCREMENT,
  title VARCHAR(15) NOT NULL,
  price INT NOT NULL,
  PRIMARY KEY (id));

CREATE TABLE mydb.work_record (
	id INT NOT NULL AUTO_INCREMENT,
	patient VARCHAR(20) NOT NULL,
	clinic VARCHAR(20) NOT NULL,
	accepted DATE NOT NULL,
    complete DATE,
    closed BOOLEAN NOT NULL DEFAULT 0,
    paid BOOLEAN NOT NULL DEFAULT 0,
	photo BLOB,
	comment VARCHAR(45),
	PRIMARY KEY (id)
    );

CREATE TABLE mydb.product (
    work_id INT NOT NULL,
    title VARCHAR(15) NOT NULL,
    quantity SMALLINT DEFAULT 0,
    price INT DEFAULT 0,
    CONSTRAINT work_id FOREIGN KEY (work_id) REFERENCES work_record(id)
    );