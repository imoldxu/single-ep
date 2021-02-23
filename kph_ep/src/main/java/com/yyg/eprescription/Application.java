package com.yyg.eprescription;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan("com.*")
@Configuration
@EnableTransactionManagement
@EnableCaching
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }

}
