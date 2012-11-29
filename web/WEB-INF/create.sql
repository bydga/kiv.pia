SET foreign_key_checks = 0;

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



INSERT INTO bydga_user (`login`, `password`, `name`, `surname`, `sex`, `birthdate`, `bio`, `image`) VALUES
	("user1", MD5("User@1234"), "Martin", "Bydžovský", "Male", "1988-08-09", "bydga descr", "1-49.png"),
	("user2", MD5("User@1234"), "USER1", "Prijmeni", "Male", null, null, "2-72.png"),
	("user3", MD5("User@1234"), null, null, "Male", null, null, "3-68.png"),
	("user4", MD5("User@1234"), "USER3", null, "Male", null, null, null);

INSERT INTO bydga_tweet(`published`, `user_id`,`text`, `retweet_id`) VALUES
	("2012-11-01 20:00", 1, "tweet1", null),
	("2012-11-02 21:00", 1, "tweet2", null),
	("2012-11-03 21:00", 3, "tweet1", 1),
	("2012-11-04 21:00", 2, "tweet4", null),
	("2012-11-05 21:00", 1, "tweet5", null),
	("2012-11-05 23:00", 1, "tweet4", 4),
	("2012-11-05 23:00", 3, "tweet4", 4),
	("2012-11-06 20:00", 1, "tweet8", null),
	("2012-11-07 21:00", 1, "tweet9", null),
	("2012-11-01 20:00", 1, "tweet10", null),
	("2012-11-02 21:00", 1, "tweet11", null),
	("2012-11-03 21:00", 3, "tweet1", 1),
	("2012-11-04 21:00", 2, "tweet13", null),
	("2012-11-05 21:00", 1, "tweet14", null),
	("2012-11-05 23:00", 1, "tweet4", 4),
	("2012-11-05 23:00", 3, "tweet4", 4),
	("2012-11-06 20:00", 1, "tweet17", null),
	("2012-11-07 21:00", 1, "tweet18", null),
	("2012-11-01 20:00", 1, "tweet19", null),
	("2012-11-02 21:00", 2, "tweet1", 1),
	("2012-11-02 21:00", 1, "tweet20", null),
  ("2012-11-02 21:00", 2, "tweet22", null),
  ("2012-11-02 21:00", 2, "tweet23", null),
  ("2012-11-02 21:00", 2, "tweet24", null),
  ("2012-11-02 21:00", 2, "tweet25", null),
  ("2012-11-02 21:00", 2, "tweet26", null),
  ("2012-11-02 21:00", 2, "tweet27", null),
  ("2012-11-02 21:00", 2, "tweet28", null),
  ("2012-11-02 21:00", 2, "tweet29", null),
  ("2012-11-02 21:00", 2, "tweet30", null),
  ("2012-11-02 21:00", 2, "tweet31", null),
  ("2012-11-02 21:00", 2, "tweet32", null);

INSERT INTO bydga_friend(`user_id`, `friend_id`) VALUES
	(1,2),
	(1,3),
	(1,4),
	(2,3),
	(2,1),
	(3,4);
SET foreign_key_checks = 1;