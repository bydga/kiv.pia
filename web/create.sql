DROP TABLE IF EXISTS bydga_friend;
DROP TABLE IF EXISTS bydga_tweet;
DROP TABLE IF EXISTS bydga_user;


CREATE TABLE bydga_tweet(
	`tweet_id` INT PRIMARY KEY AUTO_INCREMENT,
	`published` DATETIME,
	`user_id` INT,
	`text` TEXT,
	`retweet_id` INT
)TYPE=innodb CHARACTER SET utf8 COLLATE utf8_czech_ci;

CREATE TABLE bydga_user(
	`user_id` INT	PRIMARY KEY AUTO_INCREMENT,
	`login` VARCHAR(255),
	`password` VARCHAR(255),
	`name` VARCHAR(255),
	`surname` VARCHAR(255),
	`sex` VARCHAR(255),
	`birthdate` DATE,
	`bio` TEXT,
	`image` VARCHAR(255)
)TYPE=innodb CHARACTER SET utf8 COLLATE utf8_czech_ci;

CREATE TABLE bydga_friend(
	`user_id` INT,
	`friend_id` INT
)TYPE=innodb CHARACTER SET utf8 COLLATE utf8_czech_ci;

ALTER TABLE bydga_user ADD UNIQUE KEY (login);
ALTER TABLE bydga_tweet ADD INDEX (user_id);
ALTER TABLE bydga_tweet ADD FOREIGN KEY (retweet_id) REFERENCES bydga_tweet(tweet_id) ON DELETE CASCADE;
ALTER TABLE bydga_tweet ADD FOREIGN KEY (user_id) REFERENCES bydga_user(user_id) ON DELETE CASCADE;
ALTER TABLE bydga_friend ADD FOREIGN KEY (user_id) REFERENCES bydga_user(user_id) ON DELETE CASCADE;
ALTER TABLE bydga_friend ADD FOREIGN KEY (friend_id) REFERENCES bydga_user(user_id) ON DELETE CASCADE;
ALTER TABLE bydga_friend ADD UNIQUE KEY (user_id, friend_id);


SET foreign_key_checks = 0;

INSERT INTO bydga_user (`login`, `password`, `name`, `surname`, `sex`, `birthdate`, `bio`, `image`) VALUES
	("bydga", MD5("bydga"), "Martin", "Bydžovský", "Male", "1988-08-09", "bydga descr", "1.jpg"),
	("user1", MD5("user1"), "USER1", null, "Male", null, null, null),
	("user2", MD5("user2"), null, null, "Male", null, null, null),
	("user3", MD5("user3"), "USER3", null, "Male", null, null, null),
	("user4", MD5("user4"), "USER4", null, "Male", null, null, null),
	("user5", MD5("user5"), "USER5", null, "Male", null, null, null),
	("user6", MD5("user6"), "USER6", null, "Male", null, null, null);

INSERT INTO bydga_tweet(`published`, `user_id`,`text`, `retweet_id`) VALUES
	("2012-11-01 20:00", 1, "První bydga tweet", null),
	("2012-11-02 21:00", 1, "Druhý bydga tweet", null),
	("2012-11-03 21:00", 3, "První bydga tweet", 1),
	("2012-11-04 21:00", 2, "Cizí tweet tweet", null),
	("2012-11-05 21:00", 1, "Třetí bydga tweet", null),
	("2012-11-05 23:00", 1, "Cizí tweet tweet", 4),
	("2012-11-05 23:00", 3, "Cizí tweet tweet", 4),
	("2012-11-06 20:00", 1, "bydga next", null),
	("2012-11-07 21:00", 1, "bydga nex2", null),
("2012-11-01 20:00", 1, "První bydga tweet", null),
	("2012-11-02 21:00", 1, "Druhý bydga tweet", null),
	("2012-11-03 21:00", 3, "První bydga tweet", 1),
	("2012-11-04 21:00", 2, "Cizí tweet tweet", null),
	("2012-11-05 21:00", 1, "Třetí bydga tweet", null),
	("2012-11-05 23:00", 1, "Cizí tweet tweet", 4),
	("2012-11-05 23:00", 3, "Cizí tweet tweet", 4),
	("2012-11-06 20:00", 1, "bydga next", null),
	("2012-11-07 21:00", 1, "bydga nex2", null),
("2012-11-01 20:00", 1, "První bydga tweet", null),
	("2012-11-02 21:00", 1, "Druhý bydga tweet", null),
	("2012-11-03 21:00", 3, "První bydga tweet", 1),
	("2012-11-04 21:00", 2, "Cizí tweet tweet", null),
	("2012-11-05 21:00", 1, "Třetí bydga tweet", null),
	("2012-11-05 23:00", 1, "Cizí tweet tweet", 4),
	("2012-11-05 23:00", 3, "Cizí tweet tweet", 4),
	("2012-11-06 20:00", 1, "bydga next", null),
	("2012-11-07 21:00", 1, "bydga nex2", null),
("2012-11-01 20:00", 1, "První bydga tweet", null),
	("2012-11-02 21:00", 1, "Druhý bydga tweet", null),
	("2012-11-03 21:00", 3, "První bydga tweet", 1),
	("2012-11-04 21:00", 2, "Cizí tweet tweet", null),
	("2012-11-05 21:00", 1, "Třetí bydga tweet", null),
	("2012-11-05 23:00", 1, "Cizí tweet tweet", 4),
	("2012-11-05 23:00", 3, "Cizí tweet tweet", 4),
	("2012-11-06 20:00", 1, "bydga next", null),
	("2012-11-07 21:00", 1, "bydga nex2", null),
	("2012-11-20 21:00", 1, "next next", null);

INSERT INTO bydga_friend(`user_id`, `friend_id`) VALUES
	(1,2),
	(1,3),
	(1,4),
	(1,5),
	(1,6),
	(2,3),
	(2,1),
	(5,1),
	(3,4);
SET foreign_key_checks = 1;