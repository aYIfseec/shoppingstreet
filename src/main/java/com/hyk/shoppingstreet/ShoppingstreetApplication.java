package com.hyk.shoppingstreet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import tk.mybatis.spring.annotation.MapperScan;

@EnableAsync
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@MapperScan(value = "com.hyk.shoppingstreet.dao")
public class ShoppingstreetApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingstreetApplication.class, args);
	}

}
