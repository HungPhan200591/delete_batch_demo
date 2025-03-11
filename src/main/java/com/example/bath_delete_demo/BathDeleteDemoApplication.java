package com.example.bath_delete_demo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class BathDeleteDemoApplication implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(BathDeleteDemoApplication.class, args);
    }

    @Override
    public void run(String... args) {
//		long startTime = System.currentTimeMillis();
//
////		String sql = "DELETE FROM tb_log_esb WHERE log_esb_seq < 16000000";
//		String sql = "DELETE FROM tb_log_esb WHERE std_ymd < '20250310'";
//
//		int deletedRows = jdbcTemplate.update(sql);
//
//		long endTime = System.currentTimeMillis();
//		long duration = endTime - startTime;
//
//		System.out.println("Số dòng đã xóa: " + deletedRows);
//		System.out.println("Thời gian chạy: " + duration + " ms");

        String endDate = "20250310";

        Supplier<Integer> supplier = () -> jdbcTemplate.query("SELECT COUNT(*) FROM tb_log_esb WHERE std_ymd < ?", rs -> {
            rs.next();
            return rs.getInt(1);
        }, endDate);

        Function<Integer, Integer> deleteFunction = (count) -> jdbcTemplate.update("DELETE FROM tb_log_esb WHERE std_ymd < ? LIMIT ?", endDate, count);

        batchDeleteWithCount(1000,
                supplier,
                deleteFunction
        );
    }

    public static int batchDeleteWithCount(int batchSize,
                                           Supplier<Integer> countFunction,
                                           Function<Integer, Integer> deleteFunction) {
        long startTime = System.currentTimeMillis();

        int totalToDelete = countFunction.get();
        log.info("Starting batch deletion. Total records to delete: [{}], batch size: [{}]",
                totalToDelete, batchSize);

        if (totalToDelete == 0) {
            log.info("No records to delete");
            return 0;
        }

        int totalDeleted = 0;
        int iteration = 0;
        int deletedInBatch;

        do {
            // Execute delete operation with the specified batch size
            deletedInBatch = deleteFunction.apply(batchSize);
            totalDeleted += deletedInBatch;
            iteration++;

            log.info("Batch deletion progress: {}/{} records, iteration {}",
                    totalDeleted, totalToDelete, iteration);

        } while (deletedInBatch > 0);

        if (totalDeleted < totalToDelete) {
            log.warn("Deletion incomplete. Expected to delete {} records but deleted {} records.",
                    totalToDelete, totalDeleted);
        } else {
            log.info("Successfully deleted all {} records in {} iterations",
                    totalDeleted, iteration);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("Thời gian chạy: " + duration + " ms");

        return totalDeleted;
    }

}


/*
-- 5 millions records:
- Query with std_ymd
+ batch size 1.000: 104037 ms
+ batch size 10.000:
+ batch size 50.000:
+ batch size 100.000:

- Query with log_esb_seq
+ batch size 1.000:
+ batch size 10.000:
+ batch size 50.000:
+ batch size 100.000:

 */


