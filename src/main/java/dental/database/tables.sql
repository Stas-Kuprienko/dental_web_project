DROP TABLE IF EXISTS mydb.work_types;
DROP TABLE IF EXISTS mydb.work_objects;
DROP TABLE IF EXISTS mydb.records;


CREATE TABLE mydb.work_types (
    title VARCHAR(30) NOT NULL,
    price INT NOT NULL,
    PRIMARY KEY (title));

CREATE TABLE mydb.work_objects (
	id INT NOT NULL AUTO_INCREMENT,
    title VARCHAR(30) NOT NULL,
    quantity SMALLINT,
    price INT NOT NULL,
    PRIMARY KEY(id));

CREATE TABLE mydb.record (
    patient VARCHAR(45),
    clinic VARCHAR(30),
    work INT NOT NULL,
    FOREIGN KEY(work) REFERENCES mydb.work_objects(id) ON DELETE RESTRICT,
    complete DATE,
    accepted TIMESTAMP NOT NULL,
    closed BOOLEAN);