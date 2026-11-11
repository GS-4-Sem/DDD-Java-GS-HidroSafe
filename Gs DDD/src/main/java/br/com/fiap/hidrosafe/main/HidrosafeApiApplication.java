package br.com.fiap.hidrosafe.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(scanBasePackages = "br.com.fiap.hidrosafe", exclude = DataSourceAutoConfiguration.class)
public class HidrosafeApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(HidrosafeApiApplication.class, args);
    }
}
