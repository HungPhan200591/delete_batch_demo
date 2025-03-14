package com.example.batch_delete_demo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
@MapperScan("com.example.batch_delete_demo.mapper")
public class BatchDeleteDemoApplication implements CommandLineRunner {

    private final BatchDeleteService batchDeleteService;

    public static void main(String[] args) {
        SpringApplication.run(BatchDeleteDemoApplication.class, args);
    }

    @Override
    public void run(String... args) {
//        batchDeleteService.test();
        batchDeleteService.test2();
    }
}


/*
-- 5 millions records:
- Query with std_ymd
+ batch size 1.000: 104037 ms
+ batch size 10.000: 81052 ms
+ batch size 50.000: 87801 ms
+ batch size 100.000: 159365 ms

- Query with log_esb_seq
+ batch size 1.000: 96096 ms
+ batch size 10.000: 56731 ms
+ batch size 50.000: 141859 ms
+ batch size 100.000: 71373 ms

- Using doBatching
+ batch size 500: 125596 ms
+ batch size 1.000: 78659 ms
+ batch size 5.000: 75066 ms
+ batch size 10.000: 89165 ms

 */


