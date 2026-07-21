package com.disaster.shelter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ShelterServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShelterServiceApplication.class, args);
    }
}
