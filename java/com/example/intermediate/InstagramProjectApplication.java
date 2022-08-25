package com.example.intermediate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class InstagramProjectApplication {

  public static void main(String[] args) {
    SpringApplication.run(InstagramProjectApplication.class, args);
  }

}
