package com.groom.orbit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class OrbitApplication {

  public static void main(String[] args) {
    SpringApplication.run(OrbitApplication.class, args);
  }
}
