package com.example.bath_delete_demo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class BathDeleteDemoApplication implements CommandLineRunner {

    private final BatchDeleteService batchDeleteService;

    public static void main(String[] args) {
        SpringApplication.run(BathDeleteDemoApplication.class, args);
    }

    @Override
    public void run(String... args) {
        batchDeleteService.test();
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

 */


