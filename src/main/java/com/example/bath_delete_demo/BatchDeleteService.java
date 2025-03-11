package com.example.bath_delete_demo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Slf4j
public class BatchDeleteService {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void test() {

        String endDate = "20250310";

        int batchSize = 10000;

        int  totalToDelete = jdbcTemplate.query("SELECT COUNT(*) FROM tb_log_esb WHERE std_ymd < ?", rs -> {
            rs.next();
            return rs.getInt(1);
        }, endDate);



//        int minId = jdbcTemplate.query(
//                "SELECT MIN(log_esb_seq) FROM tb_log_esb WHERE std_ymd = ?",
//                rs -> {
//                    rs.next();
//                    return rs.getInt(1);
//                },
//                endDate
//        );
//
//        int totalToDelete = jdbcTemplate.query("SELECT COUNT(*) FROM tb_log_esb WHERE log_esb_seq < ?", rs -> {
//                    rs.next();
//                    return rs.getInt(1);
//                }, minId
//        );

//        Function<Integer, Integer> deleteFunction = (count) -> jdbcTemplate.update("DELETE FROM tb_log_esb WHERE log_esb_seq < ? LIMIT ?", minId, count);

        Supplier<Integer> deleteFunction = () -> jdbcTemplate.update("DELETE FROM tb_log_esb WHERE std_ymd < ? LIMIT ?", endDate, batchSize);

        batchDeleteWithCount(batchSize,
                totalToDelete,
                deleteFunction
        );
    }

    public void batchDeleteWithCount(int batchSize,
                                     int totalToDelete,
                                     Supplier<Integer> deleteFunction) {
        long startTime = System.currentTimeMillis();
        log.info("Starting batch deletion. Total records to delete: [{}], batch size: [{}]",
                totalToDelete, batchSize);

        if (totalToDelete == 0) {
            log.info("No records to delete");
            return;
        }

        int totalDeleted = 0;
        int iteration = 0;

        while (totalDeleted < totalToDelete) {
            int deletedInBatch = 0;
            try {
                deletedInBatch = deleteBatch(deleteFunction, iteration);
                log.info("Iteration {}: deleted {} records", iteration, deletedInBatch);
            } catch (Exception ex) {
                log.error("Exception tại iteration {}: {}", iteration, ex.getMessage());
            }

            totalDeleted += deletedInBatch;
            log.info("Batch deletion progress: {}/{} records, iteration {}", totalDeleted, totalToDelete, iteration);
            iteration++;

            if (deletedInBatch < batchSize) {
                break;
            }
        }

        long endTime = System.currentTimeMillis();
        log.info("Thời gian chạy: {} ms", (endTime - startTime));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int deleteBatch(Supplier<Integer> deleteFunction,
                           int iteration) {
        if (iteration == 3) {
            throw new RuntimeException("Mô phỏng lỗi tại iteration 3");
        }
        return deleteFunction.get();
    }
}
