ALTER TABLE report_tpl ADD COLUMN is_example tinyint(4) DEFAULT 2 COMMENT '是否演示示例 1是 2否'; 

ALTER TABLE doc_tpl ADD COLUMN is_example tinyint(4) DEFAULT 2 COMMENT '是否演示示例 1是 2否'; 

ALTER TABLE slide_tpl ADD COLUMN is_example tinyint(4) DEFAULT 2 COMMENT '是否演示示例 1是 2否'; 

ALTER TABLE screen_tpl ADD COLUMN is_example tinyint(4) DEFAULT 2 COMMENT '是否演示示例 1是 2否'; 