/*Table structure for table `customer` */

DROP TABLE IF EXISTS `customer`;

CREATE TABLE `customer` (
  `id`         BIGINT(20) NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(255)        DEFAULT NULL,
  `last_name`  VARCHAR(255)        DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;