package com.example.bloodbankmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BloodBankManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(BloodBankManagementApplication.class, args);
    }

}
