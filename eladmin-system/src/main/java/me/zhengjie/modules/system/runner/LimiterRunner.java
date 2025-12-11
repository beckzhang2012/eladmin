package me.zhengjie.modules.system.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author 郑杰
 * @date 2025-06-25
 * 限流表初始化Runner
 */
@Slf4j
@Component
public class LimiterRunner implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        log.info("开始初始化限流表...");
        String sqlFile = "d:\\work\\test\\el\\b3\\eladmin\\sql\\limiter.sql";
        try (BufferedReader br = new BufferedReader(new FileReader(sqlFile))) {
            StringBuilder sql = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("--")) {
                    continue;
                }
                sql.append(line);
                if (line.endsWith(";") || line.endsWith("COMMIT")) {
                    try {
                        jdbcTemplate.execute(sql.toString());
                        sql = new StringBuilder();
                    } catch (Exception e) {
                        log.error("执行SQL失败: {}", sql.toString(), e);
                        sql = new StringBuilder();
                    }
                }
            }
            log.info("限流表初始化完成！");
        } catch (IOException e) {
            log.error("读取SQL文件失败: {}", sqlFile, e);
        }
    }
}
