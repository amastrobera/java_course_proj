/* ==========  database schema =========== */

CREATE DATABASE `meals_and_allergies` /*!40100 DEFAULT CHARACTER SET latin1 */;

/* =========== tables schema =========== */

/* users data structure */

CREATE TABLE `meals_and_allergies`.`addresses` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `street` varchar(80) NOT NULL,
  `postcode` varchar(10) NOT NULL,
  `city` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `meals_and_allergies`.`parents` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `surname` varchar(45) NOT NULL,
  `phone` varchar(15) NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE `meals_and_allergies`.`users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `surname` varchar(45) NOT NULL,
  `type` varchar(10) NOT NULL,
  `phone` varchar(15) NOT NULL,
  `address` int(11) NULL,
  `parent1` int(11) NULL,
  `parent2` int(11) NULL,
  `allergies` varchar(250),
  PRIMARY KEY (`id`),
  KEY `address_idx` (`address`),
  KEY `parent1_idx` (`parent1`),
  KEY `parent2_idx` (`parent2`),
  CONSTRAINT `address` FOREIGN KEY (`address`) REFERENCES `addresses` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `parent1` FOREIGN KEY (`parent1`) REFERENCES `parents` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `parent2` FOREIGN KEY (`parent2`) REFERENCES `parents` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/* notes: a special table only for student users */
CREATE TABLE `meals_and_allergies`.`notes` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `student` INT NULL,
  `note` VARCHAR(500) NULL,
  PRIMARY KEY (`id`),
  INDEX `student_idx` (`student` ASC),
  CONSTRAINT `student`
    FOREIGN KEY (`student`)
    REFERENCES `meals_and_allergies`.`users` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


/* menus data structure */

CREATE TABLE `meals_and_allergies`.`courses` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(80) NOT NULL,
  `type` varchar(10) NOT NULL,
  `ingredients` varchar(250) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE `meals_and_allergies`.`menus` (
  `date` date NOT NULL,
  `name` varchar(80) DEFAULT NULL,
  `first` int(11) NOT NULL,
  `second` int(11) NOT NULL,
  `dessert` int(11) NULL,
  `fruit` int(11) NULL,
  PRIMARY KEY (`date`),
  KEY `first_idx` (`first`),
  KEY `second_idx` (`second`),
  KEY `dessert_idx` (`dessert`),
  KEY `fruit_idx` (`fruit`),
  CONSTRAINT `dessert` FOREIGN KEY (`dessert`) REFERENCES `courses` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `first` FOREIGN KEY (`first`) REFERENCES `courses` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `fruit` FOREIGN KEY (`fruit`) REFERENCES `courses` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `second` FOREIGN KEY (`second`) REFERENCES `courses` (`id`) ON UPDATE CASCADE,
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

ALTER TABLE `meals_and_allergies`.`menus`
ADD CONSTRAINT `DessertOfFruit`
CHECK ( (`dessert` IS NULL)  OR (`fruit` IS NULL) );


/* =========== inserting data =========== */

/* address */
insert into meals_and_allergies.addresses(id,street,postcode,city) values(1,"largo Augusto 80","10121","Torino");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(2,"largo Augusto 54","15121","Alessandria");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(3,"corso Como 98","16121","Genova");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(4,"via Ripamonti 68","20100","Milano");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(5,"corso Como 50","10121","Torino");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(6,"corso Como 22","15121","Alessandria");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(7,"via Ripamonti 62","15121","Alessandria");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(8,"via Gramsci 20","31100","Treviso");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(9,"largo Augusto 35","15121","Alessandria");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(10,"via Cavour 23","10121","Torino");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(11,"largo Augusto 2","10121","Torino");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(12,"largo Augusto 32","20100","Milano");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(13,"via Gramsci 66","16121","Genova");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(14,"via Gramsci 83","31100","Treviso");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(15,"via Cavour 68","15121","Alessandria");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(16,"corso Como 44","20100","Milano");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(17,"via Cavour 35","15121","Alessandria");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(18,"via Ripamonti 6","20100","Milano");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(19,"via Ripamonti 55","15121","Alessandria");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(20,"via Cavour 57","15121","Alessandria");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(21,"corso Como 46","16121","Genova");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(22,"via Gramsci 55","20100","Milano");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(23,"via Ripamonti 97","31100","Treviso");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(24,"via Gramsci 86","15121","Alessandria");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(25,"via Ripamonti 30","16121","Genova");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(26,"largo Augusto 26","20100","Milano");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(27,"corso Como 63","16121","Genova");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(28,"largo Augusto 64","15121","Alessandria");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(29,"via Gramsci 14","16121","Genova");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(30,"via Gramsci 19","20100","Milano");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(31,"via Gramsci 80","15121","Alessandria");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(32,"corso Como 93","10121","Torino");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(33,"via Ripamonti 40","31100","Treviso");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(34,"largo Augusto 28","15121","Alessandria");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(35,"via Cavour 90","10121","Torino");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(36,"corso Como 28","15121","Alessandria");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(37,"via Cavour 33","31100","Treviso");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(38,"largo Augusto 54","20100","Milano");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(39,"largo Augusto 12","16121","Genova");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(40,"corso Como 44","15121","Alessandria");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(41,"via Gramsci 57","10121","Torino");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(42,"via Ripamonti 70","10121","Torino");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(43,"via Cavour 73","16121","Genova");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(44,"via Cavour 95","20100","Milano");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(45,"via Gramsci 14","10121","Torino");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(46,"corso Como 10","15121","Alessandria");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(47,"corso Como 31","20100","Milano");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(48,"largo Augusto 5","20100","Milano");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(49,"via Cavour 65","20100","Milano");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(50,"via Gramsci 20","16121","Genova");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(51,"via Gramsci 48","15121","Alessandria");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(52,"via Ripamonti 7","10121","Torino");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(53,"via Cavour 27","10121","Torino");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(54,"corso Como 50","10121","Torino");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(55,"via Cavour 73","20100","Milano");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(56,"via Cavour 96","10121","Torino");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(57,"via Ripamonti 18","10121","Torino");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(58,"via Cavour 56","20100","Milano");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(59,"via Gramsci 82","10121","Torino");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(60,"via Ripamonti 83","10121","Torino");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(61,"largo Augusto 61","31100","Treviso");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(62,"corso Como 61","15121","Alessandria");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(63,"via Ripamonti 77","16121","Genova");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(64,"via Cavour 43","15121","Alessandria");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(65,"largo Augusto 10","15121","Alessandria");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(66,"corso Como 12","15121","Alessandria");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(67,"via Cavour 55","16121","Genova");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(68,"via Cavour 46","10121","Torino");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(69,"via Gramsci 50","15121","Alessandria");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(70,"via Ripamonti 61","20100","Milano");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(71,"largo Augusto 29","20100","Milano");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(72,"largo Augusto 39","31100","Treviso");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(73,"largo Augusto 99","16121","Genova");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(74,"largo Augusto 35","20100","Milano");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(75,"corso Como 29","16121","Genova");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(76,"largo Augusto 91","15121","Alessandria");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(77,"largo Augusto 78","15121","Alessandria");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(78,"via Cavour 57","10121","Torino");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(79,"via Ripamonti 83","10121","Torino");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(80,"via Gramsci 23","31100","Treviso");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(81,"largo Augusto 70","16121","Genova");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(82,"largo Augusto 87","16121","Genova");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(83,"corso Como 2","16121","Genova");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(84,"largo Augusto 13","15121","Alessandria");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(85,"via Gramsci 84","10121","Torino");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(86,"via Ripamonti 77","16121","Genova");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(87,"via Ripamonti 76","20100","Milano");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(88,"corso Como 48","15121","Alessandria");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(89,"largo Augusto 60","15121","Alessandria");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(90,"via Gramsci 70","20100","Milano");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(91,"via Cavour 47","20100","Milano");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(92,"via Cavour 73","10121","Torino");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(93,"via Cavour 98","15121","Alessandria");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(94,"via Gramsci 47","31100","Treviso");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(95,"largo Augusto 26","15121","Alessandria");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(96,"largo Augusto 97","20100","Milano");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(97,"via Ripamonti 33","15121","Alessandria");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(98,"largo Augusto 95","15121","Alessandria");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(99,"via Gramsci 61","10121","Torino");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(100,"via Cavour 32","10121","Torino");
insert into meals_and_allergies.addresses(id,street,postcode,city) values(101,"via Cavour 23","10121","Torino");

/* parents */
insert into meals_and_allergies.parents(id,name,surname,phone) values(1,"Pietro","Rossi","390.28.63.202");
insert into meals_and_allergies.parents(id,name,surname,phone) values(2,"Franca","Conti","349.50.96.834");
insert into meals_and_allergies.parents(id,name,surname,phone) values(3,"Davide","Secchi","390.57.59.924");
insert into meals_and_allergies.parents(id,name,surname,phone) values(4,"Franca","Conti","349.7.55.553");
insert into meals_and_allergies.parents(id,name,surname,phone) values(5,"Giovanni","Costa","333.8.84.99");
insert into meals_and_allergies.parents(id,name,surname,phone) values(6,"Franca","Lombardi","391.42.19.470");
insert into meals_and_allergies.parents(id,name,surname,phone) values(7,"Mario","Gallo","333.95.11.491");
insert into meals_and_allergies.parents(id,name,surname,phone) values(8,"Cristina","Conti","349.86.73.176");
insert into meals_and_allergies.parents(id,name,surname,phone) values(9,"Giovanni","Ricci","333.73.5.98");
insert into meals_and_allergies.parents(id,name,surname,phone) values(10,"Cristina","Fontana","391.19.68.629");
insert into meals_and_allergies.parents(id,name,surname,phone) values(11,"Davide","Costa","349.55.96.313");
insert into meals_and_allergies.parents(id,name,surname,phone) values(12,"Franca","Conti","349.85.5.120");
insert into meals_and_allergies.parents(id,name,surname,phone) values(13,"Mario","Secchi","391.40.23.754");
insert into meals_and_allergies.parents(id,name,surname,phone) values(14,"Maria","Lombardi","339.62.86.812");
insert into meals_and_allergies.parents(id,name,surname,phone) values(15,"Mario","Gallo","333.92.21.699");
insert into meals_and_allergies.parents(id,name,surname,phone) values(16,"Franca","Lombardi","349.48.46.873");
insert into meals_and_allergies.parents(id,name,surname,phone) values(17,"Mario","Verdi","347.87.57.384");
insert into meals_and_allergies.parents(id,name,surname,phone) values(18,"Carmela","Barbieri","390.80.82.197");
insert into meals_and_allergies.parents(id,name,surname,phone) values(19,"Pietro","Esposito","391.29.52.443");
insert into meals_and_allergies.parents(id,name,surname,phone) values(20,"Cristina","Conti","390.83.13.687");
insert into meals_and_allergies.parents(id,name,surname,phone) values(21,"Mario","Gallo","390.72.52.348");
insert into meals_and_allergies.parents(id,name,surname,phone) values(22,"Maria","Conti","347.0.91.483");
insert into meals_and_allergies.parents(id,name,surname,phone) values(23,"Davide","Verdi","347.34.89.238");
insert into meals_and_allergies.parents(id,name,surname,phone) values(24,"Carmela","Barbieri","391.88.33.884");
insert into meals_and_allergies.parents(id,name,surname,phone) values(25,"Claudio","Bianchi","391.8.74.161");
insert into meals_and_allergies.parents(id,name,surname,phone) values(26,"Paola","Barbieri","339.2.64.319");
insert into meals_and_allergies.parents(id,name,surname,phone) values(27,"Davide","Ferrari","338.90.68.883");
insert into meals_and_allergies.parents(id,name,surname,phone) values(28,"Maria","Fontana","333.0.24.231");
insert into meals_and_allergies.parents(id,name,surname,phone) values(29,"Mario","Esposito","339.35.66.725");
insert into meals_and_allergies.parents(id,name,surname,phone) values(30,"Paola","Conti","339.88.84.540");
insert into meals_and_allergies.parents(id,name,surname,phone) values(31,"Giovanni","Rossi","338.60.9.164");
insert into meals_and_allergies.parents(id,name,surname,phone) values(32,"Maria","Lombardi","333.5.63.170");
insert into meals_and_allergies.parents(id,name,surname,phone) values(33,"Pietro","Bianchi","390.17.45.43");
insert into meals_and_allergies.parents(id,name,surname,phone) values(34,"Carmela","Lombardi","339.13.96.518");
insert into meals_and_allergies.parents(id,name,surname,phone) values(35,"Pietro","Verdi","339.67.40.374");
insert into meals_and_allergies.parents(id,name,surname,phone) values(36,"Cristina","Barbieri","333.75.10.415");
insert into meals_and_allergies.parents(id,name,surname,phone) values(37,"Giovanni","Fontana","391.31.12.140");
insert into meals_and_allergies.parents(id,name,surname,phone) values(38,"Franca","Fontana","338.82.48.209");
insert into meals_and_allergies.parents(id,name,surname,phone) values(39,"Mario","Rossi","390.30.91.977");
insert into meals_and_allergies.parents(id,name,surname,phone) values(40,"Franca","Conti","347.36.49.984");
insert into meals_and_allergies.parents(id,name,surname,phone) values(41,"Mario","Ricci","339.36.2.11");
insert into meals_and_allergies.parents(id,name,surname,phone) values(42,"Carmela","Moretti","339.67.60.232");
insert into meals_and_allergies.parents(id,name,surname,phone) values(43,"Pietro","Ricci","391.8.64.258");
insert into meals_and_allergies.parents(id,name,surname,phone) values(44,"Maria","Moretti","347.5.84.831");
insert into meals_and_allergies.parents(id,name,surname,phone) values(45,"Pietro","Ferrari","391.11.27.823");
insert into meals_and_allergies.parents(id,name,surname,phone) values(46,"Paola","Moretti","347.80.34.215");
insert into meals_and_allergies.parents(id,name,surname,phone) values(47,"Davide","Ricci","347.75.66.713");
insert into meals_and_allergies.parents(id,name,surname,phone) values(48,"Paola","Conti","338.52.30.314");
insert into meals_and_allergies.parents(id,name,surname,phone) values(49,"Claudio","Verdi","390.14.31.344");
insert into meals_and_allergies.parents(id,name,surname,phone) values(50,"Maria","Moretti","339.58.27.534");
insert into meals_and_allergies.parents(id,name,surname,phone) values(51,"Mario","Verdi","338.62.39.56");
insert into meals_and_allergies.parents(id,name,surname,phone) values(52,"Paola","Fontana","333.68.83.688");
insert into meals_and_allergies.parents(id,name,surname,phone) values(53,"Davide","Rossi","390.89.6.134");
insert into meals_and_allergies.parents(id,name,surname,phone) values(54,"Paola","Barbieri","349.63.32.51");
insert into meals_and_allergies.parents(id,name,surname,phone) values(55,"Pietro","Bianchi","390.3.43.892");
insert into meals_and_allergies.parents(id,name,surname,phone) values(56,"Cristina","Moretti","338.35.80.309");
insert into meals_and_allergies.parents(id,name,surname,phone) values(57,"Claudio","Bianchi","349.15.65.71");
insert into meals_and_allergies.parents(id,name,surname,phone) values(58,"Carmela","Conti","339.20.74.697");
insert into meals_and_allergies.parents(id,name,surname,phone) values(59,"Mario","Fontana","333.38.56.994");
insert into meals_and_allergies.parents(id,name,surname,phone) values(60,"Franca","Moretti","390.37.30.57");
insert into meals_and_allergies.parents(id,name,surname,phone) values(61,"Mario","Secchi","339.49.41.261");
insert into meals_and_allergies.parents(id,name,surname,phone) values(62,"Cristina","Lombardi","391.98.40.200");
insert into meals_and_allergies.parents(id,name,surname,phone) values(63,"Mario","Ricci","391.78.29.482");
insert into meals_and_allergies.parents(id,name,surname,phone) values(64,"Paola","Moretti","391.68.22.506");
insert into meals_and_allergies.parents(id,name,surname,phone) values(65,"Davide","Ferrari","347.95.26.328");
insert into meals_and_allergies.parents(id,name,surname,phone) values(66,"Paola","Conti","339.6.76.748");
insert into meals_and_allergies.parents(id,name,surname,phone) values(67,"Mario","Esposito","339.8.65.884");
insert into meals_and_allergies.parents(id,name,surname,phone) values(68,"Carmela","Barbieri","339.92.23.343");
insert into meals_and_allergies.parents(id,name,surname,phone) values(69,"Pietro","Costa","391.8.50.599");
insert into meals_and_allergies.parents(id,name,surname,phone) values(70,"Maria","Barbieri","391.46.78.764");
insert into meals_and_allergies.parents(id,name,surname,phone) values(71,"Davide","Secchi","338.57.30.759");
insert into meals_and_allergies.parents(id,name,surname,phone) values(72,"Paola","Barbieri","347.69.92.395");
insert into meals_and_allergies.parents(id,name,surname,phone) values(73,"Giovanni","Fontana","390.28.84.926");
insert into meals_and_allergies.parents(id,name,surname,phone) values(74,"Carmela","Lombardi","390.80.14.713");
insert into meals_and_allergies.parents(id,name,surname,phone) values(75,"Pietro","Secchi","390.61.54.327");
insert into meals_and_allergies.parents(id,name,surname,phone) values(76,"Cristina","Moretti","333.97.74.586");
insert into meals_and_allergies.parents(id,name,surname,phone) values(77,"Pietro","Gallo","338.50.42.974");
insert into meals_and_allergies.parents(id,name,surname,phone) values(78,"Cristina","Conti","347.92.80.937");
insert into meals_and_allergies.parents(id,name,surname,phone) values(79,"Pietro","Bianchi","391.9.34.669");
insert into meals_and_allergies.parents(id,name,surname,phone) values(80,"Paola","Conti","391.73.98.9");
insert into meals_and_allergies.parents(id,name,surname,phone) values(81,"Giovanni","Fontana","390.75.12.312");
insert into meals_and_allergies.parents(id,name,surname,phone) values(82,"Carmela","Lombardi","390.52.68.709");
insert into meals_and_allergies.parents(id,name,surname,phone) values(83,"Mario","Esposito","390.46.33.95");
insert into meals_and_allergies.parents(id,name,surname,phone) values(84,"Carmela","Fontana","338.51.9.818");
insert into meals_and_allergies.parents(id,name,surname,phone) values(85,"Davide","Fontana","391.24.15.97");
insert into meals_and_allergies.parents(id,name,surname,phone) values(86,"Paola","Barbieri","390.10.60.167");
insert into meals_and_allergies.parents(id,name,surname,phone) values(87,"Davide","Bianchi","391.35.92.773");
insert into meals_and_allergies.parents(id,name,surname,phone) values(88,"Maria","Conti","349.36.47.237");
insert into meals_and_allergies.parents(id,name,surname,phone) values(89,"Davide","Fontana","338.21.72.694");
insert into meals_and_allergies.parents(id,name,surname,phone) values(90,"Franca","Lombardi","338.82.92.940");
insert into meals_and_allergies.parents(id,name,surname,phone) values(91,"Giovanni","Gallo","339.83.90.24");
insert into meals_and_allergies.parents(id,name,surname,phone) values(92,"Paola","Lombardi","333.19.81.910");
insert into meals_and_allergies.parents(id,name,surname,phone) values(93,"Giovanni","Ricci","339.38.73.95");
insert into meals_and_allergies.parents(id,name,surname,phone) values(94,"Paola","Moretti","338.2.32.177");
insert into meals_and_allergies.parents(id,name,surname,phone) values(95,"Claudio","Bianchi","333.10.26.627");
insert into meals_and_allergies.parents(id,name,surname,phone) values(96,"Maria","Barbieri","333.63.16.992");
insert into meals_and_allergies.parents(id,name,surname,phone) values(97,"Mario","Bianchi","333.78.57.603");
insert into meals_and_allergies.parents(id,name,surname,phone) values(98,"Carmela","Barbieri","338.8.29.104");
insert into meals_and_allergies.parents(id,name,surname,phone) values(99,"Mario","Ferrari","349.15.61.60");
insert into meals_and_allergies.parents(id,name,surname,phone) values(100,"Maria","Moretti","338.72.67.730");
insert into meals_and_allergies.parents(id,name,surname,phone) values(101,"Claudio","Rossi","338.3.77.6");
insert into meals_and_allergies.parents(id,name,surname,phone) values(102,"Paola","Moretti","339.41.35.265");
insert into meals_and_allergies.parents(id,name,surname,phone) values(103,"Pietro","Secchi","339.44.72.869");
insert into meals_and_allergies.parents(id,name,surname,phone) values(104,"Paola","Moretti","338.80.53.669");
insert into meals_and_allergies.parents(id,name,surname,phone) values(105,"Mario","Gallo","333.95.11.491");
insert into meals_and_allergies.parents(id,name,surname,phone) values(106,"Cristina","Conti","349.86.73.176");

/* users */
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(1,"Elena","Esposito","professor","390.6.62.646",1,NULL,NULL,"latte,burro");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(2,"Sara","Ferrari","professor","391.28.41.512",2,NULL,NULL,"latte,maiale");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(3,"Sara","Rossi","student","338.62.25.800",3,1,2,"olio di semi di girasole");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(4,"Matteo","Ricci","professor","390.26.83.648",4,NULL,NULL,"mozzarella");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(5,"Giulia","Secchi","student","391.31.80.202",5,3,4,"maiale,pomodoro");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(6,"Alessandro","Fontana","professor","390.63.17.497",6,NULL,NULL,"");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(7,"Gabriele","Costa","student","339.80.5.34",7,5,6,"");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(8,"Antonio","Costa","professor","347.52.95.834",8,NULL,NULL,"pomodoro");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(9,"Andrea","Costa","professor","390.58.21.398",9,NULL,NULL,"olio di semi di girasole");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(10,"Gabriele","Gallo","student","333.91.12.248",10,7,8,"mozzarella,maiale");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(11,"Alessandro","Rossi","professor","349.69.16.393",11,NULL,NULL,"pomodoro,cipolla");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(12,"Teresa","Ricci","student","338.25.45.81",12,9,10,"burro,olio di semi di girasole");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(13,"Giulia","Ferrari","professor","391.69.38.624",13,NULL,NULL,"mozzarella");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(14,"Sara","Verdi","professor","391.6.93.212",14,NULL,NULL,"");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(15,"Sara","Costa","student","347.81.83.181",15,11,12,"");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(16,"Sara","Secchi","professor","349.0.69.188",16,NULL,NULL,"latte");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(17,"Sara","Fontana","professor","390.54.89.239",17,NULL,NULL,"cipolla");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(18,"Matteo","Rossi","professor","349.97.11.443",18,NULL,NULL,"");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(19,"Gabriele","Ferrari","professor","390.53.67.167",19,NULL,NULL,"cipolla,maiale");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(20,"Gabriele","Ricci","professor","390.47.16.4",20,NULL,NULL,"");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(21,"Giulia","Fontana","professor","338.89.56.514",21,NULL,NULL,"pomodoro");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(22,"Teresa","Bianchi","professor","391.79.78.628",22,NULL,NULL,"");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(23,"Andrea","Secchi","student","333.79.61.158",23,13,14,"burro,cipolla");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(24,"Teresa","Gallo","student","338.49.30.702",24,15,16,"olio di semi di girasole");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(25,"Claudia","Verdi","student","333.55.76.564",25,17,18,"");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(26,"Gabriele","Esposito","student","347.96.37.439",26,19,20,"mozzarella");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(27,"Matteo","Secchi","professor","391.29.7.388",27,NULL,NULL,"mozzarella,mozzarella");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(28,"Andrea","Verdi","professor","390.59.11.252",28,NULL,NULL,"pomodoro,cipolla");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(29,"Giulia","Gallo","student","349.26.44.754",29,21,22,"mozzarella");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(30,"Teresa","Verdi","student","390.33.28.208",30,23,24,"");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(31,"Elena","Costa","professor","347.25.4.666",31,NULL,NULL,"");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(32,"Teresa","Ferrari","professor","349.93.85.819",32,NULL,NULL,"");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(33,"Andrea","Bianchi","student","390.86.20.817",33,25,26,"");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(34,"Alessandro","Ferrari","student","391.25.86.270",34,27,28,"mozzarella,latte");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(35,"Antonio","Esposito","student","333.70.4.709",35,29,30,"latte,maiale");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(36,"Alessandro","Costa","professor","338.91.60.987",36,NULL,NULL,"");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(37,"Claudia","Rossi","professor","390.0.63.300",37,NULL,NULL,"olio di semi di girasole,cipolla");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(38,"Andrea","Ricci","professor","390.96.65.181",38,NULL,NULL,"pomodoro,maiale");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(39,"Andrea","Fontana","professor","333.78.15.227",39,NULL,NULL,"");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(40,"Antonio","Rossi","student","339.17.65.553",40,31,32,"olio di semi di girasole");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(41,"Alessandro","Gallo","professor","339.5.40.444",41,NULL,NULL,"latte,pomodoro");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(42,"Gabriele","Bianchi","student","347.75.52.778",42,33,34,"");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(43,"Gabriele","Verdi","student","333.64.51.792",43,35,36,"cipolla,latte");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(44,"Claudia","Fontana","student","333.57.1.217",44,37,38,"");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(45,"Andrea","Rossi","student","338.96.30.805",45,39,40,"burro,maiale");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(46,"Antonio","Ricci","student","339.12.38.82",46,41,42,"olio di semi di girasole");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(47,"Giulia","Verdi","professor","333.9.53.666",47,NULL,NULL,"mozzarella");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(48,"Claudia","Ricci","student","349.40.60.502",48,43,44,"latte,latte");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(49,"Gabriele","Secchi","professor","339.16.48.812",49,NULL,NULL,"burro");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(50,"Antonio","Ferrari","student","339.64.95.478",50,45,46,"burro,mozzarella");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(51,"Giulia","Ricci","student","333.65.98.688",51,47,48,"");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(52,"Claudia","Gallo","professor","390.88.13.712",52,NULL,NULL,"");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(53,"Antonio","Verdi","student","338.0.20.478",53,49,50,"olio di semi di girasole,maiale");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(54,"Matteo","Esposito","professor","339.25.46.231",54,NULL,NULL,"olio di semi di girasole,pomodoro");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(55,"Alessandro","Verdi","student","333.96.72.924",55,51,52,"maiale,maiale");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(56,"Elena","Rossi","student","333.14.12.864",56,53,54,"latte");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(57,"Matteo","Verdi","professor","338.73.77.996",57,NULL,NULL,"pomodoro,pomodoro");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(58,"Giulia","Bianchi","student","338.76.58.961",58,55,56,"");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(59,"Alessandro","Bianchi","professor","338.7.91.148",59,NULL,NULL,"");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(60,"Giulia","Esposito","professor","349.52.48.118",60,NULL,NULL,"");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(61,"Matteo","Gallo","professor","349.59.71.274",61,NULL,NULL,"olio di semi di girasole");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(62,"Elena","Ricci","professor","339.68.42.416",62,NULL,NULL,"burro,olio di semi di girasole");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(63,"Alessandro","Esposito","professor","333.65.0.396",63,NULL,NULL,"burro,cipolla");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(64,"Claudia","Bianchi","student","390.2.65.706",64,57,58,"maiale");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(65,"Elena","Fontana","student","347.79.41.586",65,59,60,"pomodoro,pomodoro");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(66,"Sara","Esposito","professor","391.51.38.383",66,NULL,NULL,"maiale");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(67,"Andrea","Gallo","professor","391.57.49.167",67,NULL,NULL,"");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(68,"Claudia","Secchi","student","333.7.6.547",68,61,62,"");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(69,"Teresa","Secchi","professor","390.81.31.18",69,NULL,NULL,"cipolla,mozzarella");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(70,"Alessandro","Ricci","student","391.61.63.297",70,63,64,"");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(71,"Matteo","Ferrari","student","349.11.72.646",71,65,66,"maiale,maiale");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(72,"Teresa","Esposito","student","338.9.87.491",72,67,68,"burro");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(73,"Claudia","Ferrari","professor","391.93.49.31",73,NULL,NULL,"maiale,maiale");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(74,"Teresa","Costa","student","333.18.6.204",74,69,70,"mozzarella");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(75,"Alessandro","Secchi","student","390.50.10.141",75,71,72,"mozzarella,latte");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(76,"Antonio","Fontana","student","349.40.54.442",76,73,74,"mozzarella");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(77,"Matteo","Costa","professor","391.97.84.126",77,NULL,NULL,"maiale,maiale");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(78,"Elena","Secchi","student","333.65.64.490",78,75,76,"maiale,cipolla");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(79,"Claudia","Costa","professor","347.23.33.206",79,NULL,NULL,"");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(80,"Antonio","Gallo","student","390.89.39.467",80,77,78,"burro,cipolla");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(81,"Antonio","Bianchi","student","391.9.70.757",81,79,80,"");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(82,"Teresa","Fontana","student","391.81.88.211",82,81,82,"cipolla,pomodoro");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(83,"Andrea","Esposito","student","339.19.13.629",83,83,84,"");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(84,"Gabriele","Fontana","student","338.86.35.716",84,85,86,"pomodoro");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(85,"Sara","Bianchi","student","338.17.80.95",85,87,88,"cipolla");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(86,"Giulia","Costa","professor","390.54.6.618",86,NULL,NULL,"");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(87,"Claudia","Esposito","professor","347.44.2.672",87,NULL,NULL,"");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(88,"Elena","Ferrari","professor","390.15.76.965",88,NULL,NULL,"maiale");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(89,"Matteo","Fontana","student","349.56.57.350",89,89,90,"pomodoro");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(90,"Sara","Gallo","professor","338.18.58.117",90,NULL,NULL,"pomodoro");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(91,"Gabriele","Rossi","professor","391.76.58.477",91,NULL,NULL,"latte");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(92,"Elena","Verdi","professor","347.68.9.740",92,NULL,NULL,"pomodoro");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(93,"Elena","Gallo","student","347.60.18.825",93,91,92,"pomodoro");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(94,"Sara","Ricci","student","333.62.5.58",94,93,94,"cipolla");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(95,"Giulia","Rossi","professor","391.30.53.632",95,NULL,NULL,"burro");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(96,"Elena","Bianchi","student","339.32.80.258",96,95,96,"");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(97,"Matteo","Bianchi","student","391.40.68.940",97,97,98,"burro");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(98,"Andrea","Ferrari","student","349.71.80.973",98,99,100,"");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(99,"Teresa","Rossi","student","333.51.60.866",99,101,102,"maiale,olio di semi di girasole");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(100,"Antonio","Secchi","student","338.61.89.506",100,103,104,"");
insert into meals_and_allergies.users(id,name,surname,type,phone,address,parent1,parent2,allergies) values(101,"Gabriele","Pollo","student","333.91.12.248",101,105,106,"mozzarella,maiale");


/* courses */
insert into meals_and_allergies.courses(id,name, type, ingredients) values(1,"Crema di fagioli con pasta","first","pasta,fagioli borlotti,carota,cipolla");
insert into meals_and_allergies.courses(id,name, type, ingredients) values(2,"Crema di patate e porri","first","patate,porro,aglio,sale");
insert into meals_and_allergies.courses(id,name, type, ingredients) values(3,"Crema di piselli con riso","first","piselli,patate,riso,olio d'oliva,olio di semi di girasole,sale"); /*'*/
insert into meals_and_allergies.courses(id,name, type, ingredients) values(4,"Gnocchi al pomodoro","first","gnocchi di patate,pomodoro,carota,cipolla,olio d’oliva,sale,basilico");
insert into meals_and_allergies.courses(id,name, type, ingredients) values(5,"Pasta ai broccoli","first","pasta,broccoli,latte,farina,sale");
insert into meals_and_allergies.courses(id,name, type, ingredients) values(6,"Orzotto con zucchine e asiago","first","orzo,zucchine,latte,asiago,olio di semi di girasole,farina,sale");
insert into meals_and_allergies.courses(id,name, type, ingredients) values(7,"Pasta al ragù","first","pasta,pomodoro,manzo,pollo,carota,cipolla,sedano,olio d'oliva,sale");/*'*/
insert into meals_and_allergies.courses(id,name, type, ingredients) values(8,"Pizza margherita","first","farina,acqua,olio di semi di girasole,lievito,sale,zucchero,pomodoro,mozzarella,olio d'oliva,origano,sale");/*'*/
insert into meals_and_allergies.courses(id,name, type, ingredients) values(9,"Riso alla milanese","first","riso,olio d'oliva,burro,zafferano,sale");/*'*/
insert into meals_and_allergies.courses(id,name, type, ingredients) values(10,"Riso alla zucca","first","riso,zucca,latte,farina,olio d'oliva,sale");/*'*/
insert into meals_and_allergies.courses(id,name, type, ingredients) values(11,"Riso alle zucchine","first","riso,zucchine,latte,farina,olio d'oliva,sale");/*'*/
insert into meals_and_allergies.courses(id,name, type, ingredients) values(12,"Arrosto di tacchino al forno","second","tacchino,carota,sedano,cipolla,olio di semi di girasole,patate,sale");
insert into meals_and_allergies.courses(id,name, type, ingredients) values(13,"Bastoncini fil. merluzzo","second","merluzzo,farina,olio di semi di girasole,grano,mais,lievito,sale,zucchero,spezie,sali minerali");
insert into meals_and_allergies.courses(id,name, type, ingredients) values(14,"Bocconcini di Vitellone in umido","second","vitello,pomodoro,carote,sedano,cipolla,olio d'oliva,sale,patate,erbe aromatiche");/*'*/
insert into meals_and_allergies.courses(id,name, type, ingredients) values(15,"Fettina di maiale alla piastra","second","maiale,olio d'oliva,sale,pepe");/*'*/
insert into meals_and_allergies.courses(id,name, type, ingredients) values(16,"Fettina di tacchino alla piastra","second","fesa di tacchino,olio di semi di girasole,olio d'oliva,sale");/*'*/
insert into meals_and_allergies.courses(id,name, type, ingredients) values(17,"Filetto di merluzzo alla pizzaiola","second","merluzzo,pomodoro,olio di semi di girasole,olio d'oliva,erbe aromatiche,sale");/*'*/
insert into meals_and_allergies.courses(id,name, type, ingredients) values(18,"Petto di pollo alla piastra","second","pollo,olio d'oliva,sale,pepe");/*'*/
insert into meals_and_allergies.courses(id,name, type, ingredients) values(19,"Sovracosce di pollo al forno","second","pollo,olio di semi di girasole,erbe aromatiche");
insert into meals_and_allergies.courses(id,name, type, ingredients) values(20,"Tonno sott’olio","second","tonno");
insert into meals_and_allergies.courses(id,name, type, ingredients) values(21,"Formaggio caciotta","second","latte,fermenti,caglio,sale");
insert into meals_and_allergies.courses(id,name, type, ingredients) values(22,"Yogurt","dessert","latte");
insert into meals_and_allergies.courses(id,name, type, ingredients) values(23,"Brownies fatti in casa","dessert","cacao,burro,cioccolato,farina,lievito");
insert into meals_and_allergies.courses(id,name, type, ingredients) values(24,"Crostatina alla fragola","dessert","farina,burro,zucchero,uova,limone,latte,pana,vaniglia,fragole");
insert into meals_and_allergies.courses(id,name, type, ingredients) values(25,"Strudel","dessert","limone,cannella,uvetta,mela,rum,pangrattato,pinoli,olio di semi di girasole ");
insert into meals_and_allergies.courses(id,name, type, ingredients) values(26,"Budino al cioccolato","dessert","cacao,farina,burro,latte,vaniglia,rum");
insert into meals_and_allergies.courses(id,name, type, ingredients) values(27,"Macedonia di fruit","fruit","banana,mela,arancia,uva bianca");
insert into meals_and_allergies.courses(id,name, type, ingredients) values(28,"Mela","fruit","mela");
insert into meals_and_allergies.courses(id,name, type, ingredients) values(29,"Banana","fruit","banana");
insert into meals_and_allergies.courses(id,name, type, ingredients) values(30,"Pera","fruit","pera");
insert into meals_and_allergies.courses(id,name, type, ingredients) values(31,"Smoothie di fruit mista","fruit","arancia,sedano,cetriolo,menta,zucchero di canna");

/* menus */

insert into meals_and_allergies.menus(date, name, first, second, dessert)
values("2018-05-08","",3,13,25);
insert into meals_and_allergies.menus(date, name, first, second, fruit)
values("2018-05-12","pranzo di lunedì",11,16,27);


/* =========== Queries =========== */

/* getUsers*/
select u.name, u.surname, u.type, u.phone, 
	   concat(a.street, ",", a.postcode, ",", a.city) as address, 
       concat(p1.name, " ", p1.surname, " ", p1.phone, ",", 
			  p2.name, " ", p2.surname, " ", p2.phone) as parents,
       u.allergies
from users as u 
left join addresses as a on u.address = a.id 
left join parents as p1 on u.parent1 = p1.id
left join parents as p2 on u.parent2 = p2.id
;

/* getMenus */ 
select m.date, m.name, 
		c1.name as 'first', 
		c2.name as 'second', 
        c3.name as 'dessert',
		c4.name as 'fruit'
from menus as m
left join courses as c1 on c1.id = m.first
left join courses as c2 on c2.id = m.second
left join courses as c3 on c3.id = m.dessert
left join courses as c4 on c4.id = m.fruit
;

/* getCourses */
select * from courses
;


/* getCourseInfo */
/*
select c.name, c.type, c.ingredients
from courses as c
where c.name like 'Crema di %'
;
*/
select c.name, c.type, c.ingredients
from courses as c
where c.name = 'Crema di fagioli con pasta'
;




