DROP TABLE IF EXISTS mydb.accounts;
DROP TABLE IF EXISTS mydb.records;
DROP TABLE IF EXISTS mydb.work_position;
DROP TABLE IF EXISTS mydb.reports;

CREATE TABLE mydb.accounts (
  id INT NOT NULL,
  login VARCHAR(15) NOT NULL,
  PRIMARY KEY (id)
  );

CREATE TABLE mydb.records (
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

CREATE TABLE mydb.work_position (
	record_id INT NOT NULL,
    title VARCHAR(15) NOT NULL,
    quantity SMALLINT,
    price INT NOT NULL,
    FOREIGN KEY(record_id) REFERENCES mydb.records(id) ON DELETE RESTRICT
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