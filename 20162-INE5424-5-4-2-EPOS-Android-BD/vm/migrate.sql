-- Fill user
INSERT INTO `sodb_novo`.`user` (`iduser`, `username`, `password`) SELECT `iduser`, `username`, `password` FROM `sodb`.`user`;

-- Fill smartobject
INSERT INTO `sodb_novo`.`smartobject` (`idsmartobject`, `serverurl`, `idsomodbus`, `name`) SELECT `idsmartobject`, `serverurl`, `idsomodbus`, `cat`.`name` AS `name` FROM `sodb`.`smartobject` AS `s` INNER JOIN `sodb`.`category` AS `cat` ON `cat`.`idcategory`=`s`.`idcategory`;

-- Fill souserjoin
INSERT INTO `sodb_novo`.`souserjoin` (`idsmartobject`, `iduser`) SELECT `idsmartobject`, `iduser` FROM `sodb`.`souserjoin`;

-- Fill service
INSERT INTO `sodb_novo`.`service` (`idsmartobject`, `name`) SELECT `so`.`idsmartobject`, `s`.`name` FROM `sodb`.`service` AS `s` INNER JOIN `sodb`.`category` AS `c` ON `c`.`idcategory`=`s`.`idcategory` INNER JOIN `sodb`.`smartobject` AS `so` ON `so`.`idcategory`=`c`.`idcategory`;

-- Fill parameter
INSERT INTO sodb_novo.parameter (`idservice`, `idsmartobject`, `register_modbus`, `name`, `type`, `minvalue`, `maxvalue`, `options`) SELECT `ns`.`idservice`, `ns`.`idsmartobject`, HEX(UNHEX(`s`.`idregistermodbus`)+`p`.`idparameter`), `p`.`name`, `p`.`type`, `p`.`minvalue`, `p`.`maxvalue`, `p`.`options` FROM `sodb`.`parameter` AS `p` INNER JOIN `sodb`.`serviceparameter` AS `sp` ON `sp`.`idparameter`=`p`.`idparameter` INNER JOIN `sodb`.`service` AS `s` ON `s`.`idservice`=`sp`.`idservice` INNER JOIN `sodb`.`category` AS `c` ON `c`.`idcategory`=`s`.`idcategory` INNER JOIN `sodb`.`smartobject` AS `so` ON `so`.`idcategory`=`c`.`idcategory` inner join `sodb_novo`.`service` AS `ns` ON `ns`.`idsmartobject`=`so`.`idsmartobject` AND `ns`.`name`=`s`.`name`;
