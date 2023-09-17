CREATE TABLE IF NOT EXISTS mydb.account;

CREATE TABLE mydb.account (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(25),
  login VARCHAR(15) NOT NULL,
  password BLOB NOT NULL,
  created DATE NOT NULL,
  PRIMARY KEY (id)
  );

///user table of the products prices
CREATE TABLE mydb.product_map (
  id INT NOT NULL,
  title VARCHAR(15) NOT NULL,
  price INT NOT NULL,
  PRIMARY KEY (id));


///user table of the work records
CREATE TABLE mydb.work_record_? (
	id INT NOT NULL AUTO_INCREMENT,
	patient VARCHAR(20),
	clinic VARCHAR(20),
	?
	complete DATE,
	accepted DATE NOT NULL,
    closed BOOLEAN NOT NULL DEFAULT 0,
    paid BOOLEAN NOT NULL DEFAULT 0,
	photo BLOB,
	comment VARCHAR(45),
	PRIMARY KEY (id)
    );