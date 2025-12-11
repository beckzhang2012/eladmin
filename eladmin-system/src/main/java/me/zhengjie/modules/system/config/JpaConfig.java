package me.zhengjie.modules.system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author admin
 * @date 2025-06-27
 */
@Configuration
@EnableJpaRepositories(basePackages = "me.zhengjie.modules.system.repository")
@EnableTransactionManagement
public class JpaConfig {
}
