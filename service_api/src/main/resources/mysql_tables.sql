DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS dental_work;
DROP TABLE IF EXISTS product_map;
DROP TABLE IF EXISTS report;
DROP TABLE IF EXISTS user;


CREATE TABLE dental.user (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(63),
  email VARCHAR(129) NOT NULL UNIQUE,
  password BLOB NOT NULL,
  created DATE NOT NULL,
  PRIMARY KEY (id)
  );

CREATE TABLE dental.report (
	id INT NOT NULL AUTO_INCREMENT,
	year INT NOT NULL,
    month ENUM ('january', 'february', 'march',
					'april', 'may', 'june',
                'july', 'august', 'september',
               'october', 'november', 'december') NOT NULL,
    PRIMARY KEY (id)
    );

CREATE TABLE dental.product_map (
  user_id INT NOT NULL,
  id INT NOT NULL AUTO_INCREMENT,
  title VARCHAR(30) NOT NULL,
  price INT NOT NULL,
  FOREIGN KEY (user_id) REFERENCES user(id),
  PRIMARY KEY (id, user_id)
  );

CREATE TABLE dental.dental_work (
	user_id INT NOT NULL,
	id INT NOT NULL AUTO_INCREMENT,
	patient VARCHAR(63) NOT NULL,
	clinic VARCHAR(63) NOT NULL,
	accepted DATE NOT NULL,
    complete DATE,
    status ENUM('MAKE', 'CLOSED', 'PAID') DEFAULT 'MAKE',
	photo BLOB,
	comment VARCHAR(127),
    report_id INT DEFAULT null,
    FOREIGN KEY (report_id) REFERENCES report (id),
    CONSTRAINT user_id FOREIGN KEY (user_id) REFERENCES user(id),
	PRIMARY KEY (id, user_id)
    );

CREATE TABLE dental.product (
    work_id INT NOT NULL,
    title INT NOT NULL,
    quantity SMALLINT DEFAULT 0,
    price INT DEFAULT 0,
    CONSTRAINT work_id FOREIGN KEY (work_id) REFERENCES dental_work(id),
    FOREIGN KEY (title) REFERENCES product_map (id),
    PRIMARY KEY (work_id, title)
    );


SELECT dental_work.*,
	GROUP_CONCAT(product.title) AS entry_id,
    GROUP_CONCAT(product_map.title) AS title,
    GROUP_CONCAT(product.quantity) AS quantity,
    GROUP_CONCAT(product.price) AS price
	FROM dental_work
    JOIN product ON product.work_id = dental_work.id
    JOIN product_map ON product_map.id = product.title
	WHERE dental_work.user_id = 1
    GROUP BY dental_work.id;

SELECT product.title AS entry_id,
    product_map.title AS title, product.quantity, product.price
    FROM product
    JOIN product_map ON product_map.id = product.title
    WHERE product.title IN
    (SELECT product_map.id FROM product_map WHERE product_map.title = ?)
    AND
    (work_id IN
    (SELECT dental_work.id FROM dental_work WHERE dental_work.user_id =
    (SELECT dental_work.user_id FROM dental_work WHERE dental_work.id = ?)))
    AND quantity = ?;