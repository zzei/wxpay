package com.zei.it.itclass;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@MapperScan("com.zei.it.itclass.mapper")
@EnableTransactionManagement
public class ItclassApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItclassApplication.class, args);
	}

}
