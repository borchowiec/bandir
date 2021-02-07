package com.borchowiec.userrepository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class UserRepositoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserRepositoryApplication.class, args);
    }

}
