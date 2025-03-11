CREATE TABLE tb_log_esb
(
    log_esb_seq       bigint AUTO_INCREMENT COMMENT '로그시퀀스' PRIMARY KEY,
    std_ymd           varchar(8)   NULL COMMENT '기준일자',
    uuid              varchar(50)  NULL COMMENT 'ESB 식별자',
    esb_id            varchar(50)  NULL COMMENT 'ESB I/F 식별자',
    input_data        json         NULL COMMENT '요청 파라미터',
    input_header      json         NULL COMMENT '요청 헤더',
    request_datetime  datetime(3)  NULL COMMENT '요청 일시',
    response_datetime datetime(3)  NULL COMMENT '응답 일시',
    time_taken        float(10, 3) NULL COMMENT '통신시간(?)',
    status            varchar(7)   NULL COMMENT '성공여부 구분(SUC,ERR_RFC,ERR_ESB)',
    error_type        varchar(50)  NULL COMMENT '오류 타입	',
    error_code        varchar(50)  NULL COMMENT '오류 코드	',
    reason            text         NULL COMMENT '오류 사유	'
) COMMENT 'ESB 로그정보';

CREATE INDEX idx_tb_log_esb_by_std_ymd ON tb_log_esb (std_ymd DESC);

CREATE INDEX idx_tb_log_esb_by_uuid ON tb_log_esb (uuid);

-- Insert sample data
USE test_delete;


SHOW INDEX FROM tb_log_esb;

------------------------------------------------------------------------------------------------

INSERT INTO tb_log_esb (std_ymd, uuid, esb_id, input_data, input_header, request_datetime, response_datetime ) 
VALUES ('20240924', UUID(), 'DSA_SAP_SD_SRS_0001', '{"I_CMITM": "1361", "I_GUBUN": "L", 
"I_PERNR": "20468925", "I_YYYYMM": "202410"}', 
'{"IF_ID": "DSA_SAP_SD_SRS_0004", "RST_CD": "", "IF_DATE": "20241029180646", "IF_HOST": "b5e51f6f06d9", "IF_UUID": "f8bdaac7-139b-410b-8c57-4e31043fe3d2", "RST_MSG": "", "IF_ADDITIONAL_DATA": ""}',
    NOW(3), NOW(3)
)


INSERT INTO tb_log_esb (
    std_ymd, uuid, esb_id, input_data, input_header, request_datetime, response_datetime, 
    time_taken, status, error_type, error_code, reason
)
SELECT 
    '20250310', 
    UUID(), 
    'DSA_SAP_SD_SRS_0001', 
    '{"I_CMITM": "1361", "I_GUBUN": "L", "I_PERNR": "20468925", "I_YYYYMM": "202410"}',
    '{"IF_ID": "DSA_SAP_SD_SRS_0004", "RST_CD": "", "IF_DATE": "20241029180646", "IF_HOST": "b5e51f6f06d9", "IF_UUID": "f8bdaac7-139b-410b-8c57-4e31043fe3d2", "RST_MSG": "", "IF_ADDITIONAL_DATA": ""}',
    NOW(3), NOW(3), 0, 'SUC', NULL, NULL, NULL
FROM (
    SELECT 
      a.N + b.N * 10 + c.N * 100 + d.N * 1000 + e.N * 10000 + f.N * 100000 + g.N * 1000000 + h.N * 10000000 AS num
    FROM 
      (SELECT 0 AS N UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) a,
      (SELECT 0 AS N UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) b,
      (SELECT 0 AS N UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) c,
      (SELECT 0 AS N UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) d,
      (SELECT 0 AS N UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) e,
      (SELECT 0 AS N UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) f,
      (SELECT 0 AS N UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) g,
      (SELECT 0 AS N UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) h
) numbers
WHERE num < 14000000;

UPDATE tb_log_esb SET std_ymd = '20250309' WHERE log_esb_seq <= 5000000;

------------------------------------------------------------------------------------------------



TRUNCATE TABLE tb_log_esb;

SELECT COUNT(*) FROM tb_log_esb;

SELECT COUNT(*) FROM tb_log_esb WHERE std_ymd = '20250310';

SELECT COUNT(*) FROM tb_log_esb WHERE std_ymd < '20250310';

SELECT COUNT(*) FROM tb_log_esb WHERE std_ymd = '20250309';

SELECT COUNT(*) FROM tb_log_esb WHERE log_esb_seq <= 5000000;

SELECT * FROM tb_log_esb  ORDER BY request_datetime LIMIT 10;

SELECT MAX (log_esb_seq) FROM tb_log_esb;

SELECT MIN (log_esb_seq) FROM tb_log_esb;

DELETE FROM tb_log_esb WHERE std_ymd = '20250309';

DELETE FROM tb_log_esb WHERE std_ymd < '20250310';

DELETE FROM tb_log_esb WHERE log_esb_seq < 14300000;


DELETE FROM tb_log_esb LIMIT 1000000;

SELECT MIN(log_esb_seq) FROM tb_log_esb WHERE std_ymd = '20250310'

CREATE TABLE tb_log_esb_50000 LIKE tb_log_esb;
INSERT INTO tb_log_esb_50000 SELECT * FROM tb_log_esb;

DROP TABLE tb_log_esb_100000;

------------


INSERT INTO tb_log_esb (
    std_ymd, uuid, esb_id, input_data, input_header, request_datetime, response_datetime, 
    time_taken, status, error_type, error_code, reason
)
SELECT 
    '20250310', 
    UUID(), 
    'DSA_SAP_SD_SRS_0001', 
    '{"I_CMITM": "1361", "I_GUBUN": "L", "I_PERNR": "20468925", "I_YYYYMM": "202410"}',
    '{"IF_ID": "DSA_SAP_SD_SRS_0004", "RST_CD": "", "IF_DATE": "20241029180646", "IF_HOST": "b5e51f6f06d9", "IF_UUID": "f8bdaac7-139b-410b-8c57-4e31043fe3d2", "RST_MSG": "", "IF_ADDITIONAL_DATA": ""}',
    NOW(3), NOW(3), 0, 'SUC', NULL, NULL, NULL
FROM (
    SELECT 
      a.N + b.N * 10 AS num
    FROM 
      (SELECT 0 AS N UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) a,
      (SELECT 0 AS N UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) b
) numbers
WHERE num < 100;







