package com.example.bath_delete_demo;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
@RequiredArgsConstructor
public class BathDeleteDemoApplication implements CommandLineRunner {

	private final JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		SpringApplication.run(BathDeleteDemoApplication.class, args);
	}

	@Override
	public void run(String... args) {
		long startTime = System.currentTimeMillis();
		//11851857
		int deletedRows = jdbcTemplate.update("DELETE FROM tb_log_esb WHERE log_esb_seq < 12000000");

		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;

		System.out.println("Số dòng đã xóa: " + deletedRows);
		System.out.println("Thời gian chạy: " + duration + " ms");
	}
}
