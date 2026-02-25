package com.example.restaurantmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class RestaurantManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestaurantManagementApplication.class, args);
    }

}
