-- scheme
CREATE
DATABASE realtime CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

CREATE TABLE `trend`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'trend ID',
    `title`       varchar(64)  NOT NULL COMMENT '제목',
    `content`     varchar(512) NOT NULL COMMENT '내용',
    `seq`         int          NOT NULL COMMENT '순서',
    `created_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    `modified_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=200000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='트랜드';
