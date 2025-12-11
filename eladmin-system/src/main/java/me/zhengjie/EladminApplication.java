package me.zhengjie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author admin
 * @date 2025-06-27
 */
@SpringBootApplication
@ComponentScan(basePackages = {"me.zhengjie"})
@EnableJpaRepositories(basePackages = {"me.zhengjie.repository", "me.zhengjie.modules.*.repository"})
public class EladminApplication {

    public static void main(String[] args) {
        SpringApplication.run(EladminApplication.class, args);
    }
}
