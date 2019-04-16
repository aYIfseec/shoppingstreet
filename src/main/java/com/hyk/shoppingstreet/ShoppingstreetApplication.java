package com.hyk.shoppingstreet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ShoppingstreetApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingstreetApplication.class, args);
	}

}
