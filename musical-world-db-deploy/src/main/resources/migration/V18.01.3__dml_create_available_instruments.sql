INSERT INTO `type` (`id`,`name`) VALUES (1,'strings');
INSERT INTO `type` (`id`,`name`) VALUES (2,'percussion');
INSERT INTO `type` (`id`,`name`) VALUES (3,'woodwinds');
INSERT INTO `type` (`id`,`name`) VALUES (4,'brass');
INSERT INTO `type` (`id`,`name`) VALUES (5,'keyboards');


INSERT INTO `instrument` (`id`,`type_id`,`name`) VALUES (1,1,'viola');
INSERT INTO `instrument` (`id`,`type_id`,`name`) VALUES (2,1,'violin');
INSERT INTO `instrument` (`id`,`type_id`,`name`) VALUES (3,1,'cello');
INSERT INTO `instrument` (`id`,`type_id`,`name`) VALUES (4,1,'double bass');
INSERT INTO `instrument` (`id`,`type_id`,`name`) VALUES (5,1,'harp');

INSERT INTO `instrument` (`id`,`type_id`,`name`) VALUES (6,2,'Bass Drum');
INSERT INTO `instrument` (`id`,`type_id`,`name`) VALUES (7,2,'Castanets');
INSERT INTO `instrument` (`id`,`type_id`,`name`) VALUES (8,2,'Chimes');
INSERT INTO `instrument` (`id`,`type_id`,`name`) VALUES (9,2,'Conga Drum');
INSERT INTO `instrument` (`id`,`type_id`,`name`) VALUES (10,2,'Glockenspiel');
INSERT INTO `instrument` (`id`,`type_id`,`name`) VALUES (11,2,'Gong');
INSERT INTO `instrument` (`id`,`type_id`,`name`) VALUES (12,2,'Guiro');
INSERT INTO `instrument` (`id`,`type_id`,`name`) VALUES (13,2,'Snare Drum');
INSERT INTO `instrument` (`id`,`type_id`,`name`) VALUES (14,2,'Tenor Drum');
INSERT INTO `instrument` (`id`,`type_id`,`name`) VALUES (15,2,'Tambourine');
INSERT INTO `instrument` (`id`,`type_id`,`name`) VALUES (16,2,'Triangle');
INSERT INTO `instrument` (`id`,`type_id`,`name`) VALUES (17,2,'Wood Block');
INSERT INTO `instrument` (`id`,`type_id`,`name`) VALUES (18,2,'Wood Xylophone');

INSERT INTO `instrument` (`id`,`type_id`,`name`) VALUES (19,3,'Flute');
INSERT INTO `instrument` (`id`,`type_id`,`name`) VALUES (20,3,'Piccolo');
INSERT INTO `instrument` (`id`,`type_id`,`name`) VALUES (21,3,'Oboe');
INSERT INTO `instrument` (`id`,`type_id`,`name`) VALUES (22,3,'English Horn');
INSERT INTO `instrument` (`id`,`type_id`,`name`) VALUES (23,3,'Clarinet');
INSERT INTO `instrument` (`id`,`type_id`,`name`) VALUES (24,3,'Saxophone');
INSERT INTO `instrument` (`id`,`type_id`,`name`) VALUES (25,3,'Bassoon');
INSERT INTO `instrument` (`id`,`type_id`,`name`) VALUES (26,3,'Contrabassoon');


INSERT INTO `instrument` (`id`,`type_id`,`name`) VALUES (27,4,'French Horn');
INSERT INTO `instrument` (`id`,`type_id`,`name`) VALUES (28,4,'Trumpet');
INSERT INTO `instrument` (`id`,`type_id`,`name`) VALUES (29,4,'Trombone');
INSERT INTO `instrument` (`id`,`type_id`,`name`) VALUES (30,4,'Tuba');

INSERT INTO `instrument` (`id`,`type_id`,`name`) VALUES (31,5,'Harpsichord');
INSERT INTO `instrument` (`id`,`type_id`,`name`) VALUES (32,5,'Piano');
INSERT INTO `instrument` (`id`,`type_id`,`name`) VALUES (33,5,'Organ');
