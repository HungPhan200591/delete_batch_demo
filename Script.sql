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
WITH RECURSIVE dates AS (
    -- Tạo 50 ngày, bắt đầu từ '2025-03-10' và giảm dần 1 ngày mỗi lần
    SELECT 0 AS day_offset, DATE('2025-03-10') AS dt
    UNION ALL
    SELECT day_offset + 1, DATE_SUB(dt, INTERVAL 1 DAY)
    FROM dates
    WHERE day_offset < 49
), numbers AS (
    -- Sinh 100.000 số (từ 0 đến 99.999)
    SELECT a.n + b.n*10 + c.n*100 + d.n*1000 + e.n*10000 AS num
    FROM 
      (SELECT 0 AS n UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 
       UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) a,
      (SELECT 0 AS n UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 
       UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) b,
      (SELECT 0 AS n UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 
       UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) c,
      (SELECT 0 AS n UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 
       UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) d,
      (SELECT 0 AS n UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 
       UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) e
    LIMIT 100000
)
INSERT INTO tb_log_esb (
    std_ymd,
    uuid,
    esb_id,
    input_data,
    input_header,
    request_datetime,
    response_datetime
)
SELECT 
    DATE_FORMAT(d.dt, '%Y%m%d') AS std_ymd,
    UUID() AS uuid,
    'DSA_SAP_SD_SRS_0001' AS esb_id,
    '{"I_CMITM": "1361", "I_GUBUN": "L", "I_PERNR": "20468925", "I_YYYYMM": "202410"}' AS input_data,
    '{"IF_ID": "DSA_SAP_SD_SRS_0004", "RST_CD": "", "IF_DATE": "20241029180646", "IF_HOST": "b5e51f6f06d9", "IF_UUID": "f8bdaac7-139b-410b-8c57-4e31043fe3d2", "RST_MSG": "", "IF_ADDITIONAL_DATA": ""}' AS input_header,
    NOW(3) AS request_datetime,
    NOW(3) AS response_datetime
FROM dates d
CROSS JOIN numbers;




INSERT INTO tb_log_esb (std_ymd, uuid, esb_id, input_data, input_header, request_datetime, response_datetime ) 
VALUES ('20240924', UUID(), 'DSA_SAP_SD_SRS_0001', '{"I_CMITM": "1361", "I_GUBUN": "L", 
"I_PERNR": "20468925", "I_YYYYMM": "202410"}', 
'{"IF_ID": "DSA_SAP_SD_SRS_0004", "RST_CD": "", "IF_DATE": "20241029180646", "IF_HOST": "b5e51f6f06d9", "IF_UUID": "f8bdaac7-139b-410b-8c57-4e31043fe3d2", "RST_MSG": "", "IF_ADDITIONAL_DATA": ""}',
    NOW(3), NOW(3)
)



SELECT COUNT(*) FROM tb_log_esb;

SELECT * FROM tb_log_esb;




