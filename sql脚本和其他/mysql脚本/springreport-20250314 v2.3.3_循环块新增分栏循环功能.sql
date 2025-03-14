ALTER TABLE luckysheet_report_cell ADD COLUMN hloop_count int(6) DEFAULT 1 COMMENT '横向循环次数'; 
ALTER TABLE luckysheet_report_cell ADD COLUMN hloop_empty_count int(6) DEFAULT 0 COMMENT '横向循环间隔空行数'; 
ALTER TABLE luckysheet_report_cell ADD COLUMN vloop_empty_count int(6) DEFAULT 0 COMMENT '纵向循环间隔空行数'; 