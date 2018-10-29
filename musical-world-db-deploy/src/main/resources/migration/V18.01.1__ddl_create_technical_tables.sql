CREATE TABLE `address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `latitude` varchar(255) DEFAULT NULL,
  `longitude` varchar(255) DEFAULT NULL,
  `postal_code` varchar(255) DEFAULT NULL,
  `street` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE
);

CREATE TABLE `type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE
);

CREATE TABLE `picture` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45),
  `file_name` VARCHAR(45),
  `creation_date` DATETIME,
  `type_id` bigint(20),
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  FOREIGN KEY (`type_id`) REFERENCES type(`id`)
);

CREATE TABLE `role` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE
);


CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `confirmed` bit(1) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `remember_me` bit(1) DEFAULT NULL,
  `to_be_deleted` bit(1) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `address_id` bigint(20) DEFAULT NULL,
  `picture_id` bigint(20) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  FOREIGN KEY (`address_id`) REFERENCES address(`id`),
  FOREIGN KEY (`picture_id`) REFERENCES picture(`id`),
  KEY `FKddefmvbrws3hvl5t0hnnsv8ox` (`address_id`)
);

CREATE TABLE `user_roles` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  KEY `FKt4v0rrweyk393bdgt107vdx0x` (`role_id`),
  KEY `FKgd3iendaoyh04b95ykqise6qh` (`user_id`),
  FOREIGN KEY (`user_id`) REFERENCES user(`id`),
  FOREIGN KEY (`role_id`) REFERENCES role(`id`)
);

CREATE TABLE `verification_token` (
  `id` bigint(20) NOT NULL,
  `token` VARCHAR(45) NOT NULL,
  `expiry_date` DATETIME NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  UNIQUE INDEX `token_UNIQUE` (`token` ASC) VISIBLE,
  FOREIGN KEY (`user_id`) REFERENCES user(`id`)
);
CREATE TABLE `password_reset_token` (
  `id` bigint(20) NOT NULL,
  `token` VARCHAR(45) NOT NULL,
  `expiry_date` DATETIME NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  UNIQUE INDEX `token_UNIQUE` (`token` ASC) VISIBLE,
  FOREIGN KEY (`user_id`) REFERENCES user(`id`)
);

CREATE TABLE `orchestra` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `owner_id` bigint(20) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `description` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  FOREIGN KEY (`owner_id`) REFERENCES user(`id`)
);



CREATE TABLE `concert` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `description` VARCHAR(45) NULL,
  `address_id` bigint(20) NULL,
  `owner_id` bigint(20) NOT NULL,
  `date_from` DATETIME NULL,
  `date_to` DATETIME NULL,
  `number_of_rehearsals` bigint(20) DEFAULT 0,
  `ensured_drive` bit(1) DEFAULT b'0',
  `guaranteed_meal` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  FOREIGN KEY (`owner_id`) REFERENCES user(`id`),
  FOREIGN KEY (`address_id`) REFERENCES address(`id`)
);

CREATE TABLE `friendship` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `friend_id` bigint(20) NOT NULL,
  `accepted` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  FOREIGN KEY (`user_id`) REFERENCES user(`id`),
  FOREIGN KEY (`friend_id`) REFERENCES user(`id`)
);





CREATE TABLE `instrument` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `type_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  FOREIGN KEY (`type_id`) REFERENCES type(`id`)
);

CREATE TABLE `concert_instrument_slot` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20),
  `concert_id` bigint(20) NOT NULL,
  `instrument_id` bigint(20) NOT NULL,
  `taken` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  FOREIGN KEY (`user_id`) REFERENCES user(`id`),
  FOREIGN KEY (`concert_id`) REFERENCES concert(`id`),
  FOREIGN KEY (`instrument_id`) REFERENCES instrument(`id`)
);

CREATE TABLE `concert_application` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `concert_instrument_slot_id` bigint(20) NOT NULL,
  `accepted` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  FOREIGN KEY (`user_id`) REFERENCES user(`id`),
  FOREIGN KEY (`concert_instrument_slot_id`) REFERENCES concert_instrument_slot(`id`)
);

CREATE TABLE `orchestra_member` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `orchestra_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `instrument_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  FOREIGN KEY (`user_id`) REFERENCES user(`id`),
  FOREIGN KEY (`orchestra_id`) REFERENCES orchestra(`id`),
  FOREIGN KEY (`instrument_id`) REFERENCES instrument(`id`)
);


CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
);