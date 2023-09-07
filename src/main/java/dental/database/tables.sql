DROP TABLE IF EXISTS mydb.report;
DROP TABLE IF EXISTS mydb.product;
DROP TABLE IF EXISTS mydb.work_record;
DROP TABLE IF EXISTS mydb.account;

CREATE TABLE mydb.account (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(25),
  login VARCHAR(15) NOT NULL,
  password BLOB NOT NULL,
  created DATE NOT NULL,
  PRIMARY KEY (id)
  );

CREATE TABLE mydb.work_record (
	account_id INT NOT NULL,
	FOREIGN KEY(account_id) REFERENCES mydb.account(id) ON DELETE CASCAD,
	id INT NOT NULL,
	patient VARCHAR(20),
	clinic VARCHAR(20),
	complete DATE,
	accepted DATE NOT NULL,
	closed BOOLEAN,
	photo BLOB,
	comment VARCHAR(45),
	PRIMARY KEY (id)
    );

CREATE TABLE mydb.product (
	work_id INT NOT NULL,
    title VARCHAR(15) NOT NULL,
    quantity SMALLINT,
    price INT NOT NULL,
    FOREIGN KEY(work_id) REFERENCES mydb.work_record(id) ON DELETE CASCAD
    );

CREATE TABLE mydb.reports (
	r_year YEAR NOT NULL,
	r_month ENUM('january', 'february', 'march',
					'april', 'may', 'june', 'july',
                    'august', 'september', 'october',
                    'november', 'december') NOT NULL,
	r_id INT NOT NULL AUTO_INCREMENT,
	account_id INT NOT NULL,
    FOREIGN KEY(account_id) REFERENCES mydb.account(id)  ON DELETE CASCAD,
	PRIMARY KEY (r_id, account_id)
	);