
CREATE TABLE IF NOT EXISTS dental.user (
	id INT NOT NULL AUTO_INCREMENT,
	name VARCHAR(63),
	email VARCHAR(129) NOT NULL UNIQUE,
	password BLOB NOT NULL,
	created DATE NOT NULL,
	PRIMARY KEY (id, email)
	);

CREATE TABLE IF NOT EXISTS dental.report (
	id INT NOT NULL AUTO_INCREMENT,
	year INT NOT NULL,
    month ENUM ('january', 'february', 'march',
					'april', 'may', 'june',
                'july', 'august', 'september',
               'october', 'november', 'december') NOT NULL,
    PRIMARY KEY (id, year, month)
    );

CREATE TABLE IF NOT EXISTS dental.product_map (
	user_id INT NOT NULL,
	id INT NOT NULL AUTO_INCREMENT,
	title VARCHAR(31) NOT NULL UNIQUE,
	price INT NOT NULL,
	CONSTRAINT FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
	PRIMARY KEY (id, user_id, title)
  );

CREATE TABLE IF NOT EXISTS dental.dental_work (
	user_id INT NOT NULL,
	id INT NOT NULL AUTO_INCREMENT,
	patient VARCHAR(63) NOT NULL,
	clinic VARCHAR(63) NOT NULL,
	accepted DATE NOT NULL,
    complete DATE,
    status ENUM('MAKE', 'CLOSED', 'PAID') DEFAULT 'MAKE',
	comment VARCHAR(127),
    report_id INT DEFAULT null,
    FOREIGN KEY (report_id) REFERENCES report (id),
    CONSTRAINT user_id FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
	PRIMARY KEY (id, user_id)
    );

CREATE TABLE IF NOT EXISTS dental.product (
    work_id INT NOT NULL,
    title INT NOT NULL,
    quantity SMALLINT DEFAULT 0,
    price INT DEFAULT 0,
    CONSTRAINT work_id FOREIGN KEY (work_id) REFERENCES dental_work(id) ON DELETE CASCADE,
    CONSTRAINT FOREIGN KEY (title) REFERENCES product_map (id) ON DELETE CASCADE,
    PRIMARY KEY (work_id, title)
    );

CREATE TABLE IF NOT EXISTS dental.photo (
	id INT NOT NULL AUTO_INCREMENT,
    work_id INT NOT NULL,
    file BLOB NOT NULL,
    CONSTRAINT FOREIGN KEY (work_id) REFERENCES dental_work(id) ON DELETE CASCADE,
    PRIMARY KEY (id, work_id)
    );