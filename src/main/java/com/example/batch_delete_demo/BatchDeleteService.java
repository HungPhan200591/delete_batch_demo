package com.example.batch_delete_demo;

import com.example.batch_delete_demo.mapper.TestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Slf4j
public class BatchDeleteService {

    private final JdbcTemplate jdbcTemplate;
    private final TestMapper testMapper;

    //    @Transactional
    public void test() {

        String endDate = "20250310";

        int batchSize = 10000;

        int totalToDelete = jdbcTemplate.query("SELECT COUNT(*) FROM tb_log_esb WHERE std_ymd < ?", rs -> {
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

        log.info("Thời gian chạy: {} ms", (System.currentTimeMillis() - startTime));
    }

    //    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int deleteBatch(Supplier<Integer> deleteFunction,
                           int iteration) {
        if (iteration == 3) {
            throw new RuntimeException("Mô phỏng lỗi tại iteration 3");
        }
        return deleteFunction.get();
    }

    public void test2() {
        log.info("Starting batch deletion");
        long startTime = System.currentTimeMillis();

        String endDate = "20250310";

        int batchSize = 1000;

        List<Integer> total = testMapper.selectLogEsbSeqLessThan(endDate);

        log.info("=== Total records: {}, thời gian chạy: {} ms", total.size(), System.currentTimeMillis() - startTime);

        Function<List<Integer>, Integer> deleteConsumer = testMapper::deleteLogEsbByLogEsbSeqIn;

        int totalToDeleted = doBatching(batchSize, total, deleteConsumer);
        log.info("Total deleted: {}", totalToDeleted);

        log.info("Thời gian chạy: {} ms", (System.currentTimeMillis() - startTime));
    }


    public <T> int doBatching(int batchSize,
                              List<T> collection,
                              Function<List<T>, Integer> function) {

        int totalProcessed = 0;

        if (collection == null || collection.isEmpty()) {
            log.info("No elements to process.");
            return totalProcessed;
        }

        log.info("Start batch processing with collection size: [{}], batchSize: [{}]", collection.size(), batchSize);

        final int COLLECTION_SIZE = collection.size();
        for (int startBatchIdx = 0; startBatchIdx < COLLECTION_SIZE; startBatchIdx += batchSize) {

            int endBatchIdx = Math.min(startBatchIdx + batchSize, COLLECTION_SIZE);
            log.info("batch processing from element number: {} - {}", startBatchIdx, endBatchIdx - 1);

            final List<T> batchList = collection.subList(startBatchIdx, endBatchIdx);

            try {
                int processed = apply(batchList, function, startBatchIdx);
                totalProcessed += processed;
            } catch (Exception ex) {
                log.error("Error processing batch [{} - {}]: {}", startBatchIdx, endBatchIdx - 1, ex.getMessage());
                return totalProcessed;
            }
        }
        return totalProcessed;
    }

    public <T> int apply(
            List<T> batchList,
            Function<List<T>, Integer> function,
            int startBatchIdx
    ) {
//        if (startBatchIdx > 1) {
//            throw new RuntimeException("Mô phỏng lỗi");
//        }

        return function.apply(batchList);

//        try {
//            return function.apply(batchList);
//        } catch (Exception ex) {
//            log.error("Exception: {}", ex.getMessage());
//            return 0;
//        }
    }
}
