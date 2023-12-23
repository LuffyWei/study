CREATE TABLE `user_info` (
     `id` bigint(11) NOT NULL AUTO_INCREMENT,
     `name` varchar(45) DEFAULT NULL,
     `remark` varchar(1024) DEFAULT NULL,
     `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
     `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
     `is_active` int(11) DEFAULT '1',
     PRIMARY KEY (`id`),
     UNIQUE KEY `uk_name` (`name`)
)
;