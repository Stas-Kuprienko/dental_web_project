DROP TABLE IF EXISTS mydb.reports;
DROP TABLE IF EXISTS mydb.products;
DROP TABLE IF EXISTS mydb.work_records;
DROP TABLE IF EXISTS mydb.accounts;

CREATE TABLE mydb.accounts (
  id INT NOT NULL,
  login VARCHAR(15) NOT NULL,
  PRIMARY KEY (id)
  );

CREATE TABLE mydb.work_records (
	account_id INT NOT NULL,
	FOREIGN KEY(account_id) REFERENCES mydb.accounts(id) ON DELETE RESTRICT,
	id INT NOT NULL,
	patient VARCHAR(20),
	clinic VARCHAR(20),
	complete DATE,
	accepted DATE NOT NULL,
	closed BOOLEAN,
	PRIMARY KEY (id)
    );

CREATE TABLE mydb.products (
	work_id INT NOT NULL,
    title VARCHAR(15) NOT NULL,
    quantity SMALLINT,
    price INT NOT NULL,
    FOREIGN KEY(work_id) REFERENCES mydb.work_records(id) ON DELETE RESTRICT
    );

CREATE TABLE mydb.reports (
	r_year YEAR NOT NULL,
	r_month ENUM('january', 'february', 'march',
					'april', 'may', 'june', 'july',
                    'august', 'september', 'october',
                    'november', 'december') NOT NULL,
	r_id INT NOT NULL AUTO_INCREMENT,
	account_id INT NOT NULL,
    FOREIGN KEY(account_id) REFERENCES mydb.accounts(id)  ON DELETE RESTRICT,
	PRIMARY KEY (r_id, account_id)
	);