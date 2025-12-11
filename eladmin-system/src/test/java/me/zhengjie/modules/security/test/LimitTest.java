package me.zhengjie.modules.security.test;

import me.zhengjie.modules.security.rest.AuthController;
import me.zhengjie.modules.security.service.dto.AuthUserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

/**
 * 限流功能测试类
 * @author /
 */
@SpringBootTest
public class LimitTest {

    @Autowired
    private AuthController authController;

    /**
     * 测试登录接口限流
     */
    @Test
    public void testLoginLimit() {
        AuthUserDto authUserDto = new AuthUserDto();
        authUserDto.setUsername("admin");
        authUserDto.setPassword("123456");
        authUserDto.setCode("123456");
        authUserDto.setUuid("123456");
        authUserDto.setPassword("123456");

        // 连续请求11次，第11次应该被限流
        for (int i = 0; i < 11; i++) {
            try {
                ResponseEntity<Object> response = authController.login(authUserDto, null);
                System.out.println("第" + (i + 1) + "次请求，状态码：" + response.getStatusCode());
            } catch (Exception e) {
                System.out.println("第" + (i + 1) + "次请求，异常：" + e.getMessage());
            }
        }
    }

    /**
     * 测试验证码接口限流
     */
    @Test
    public void testCodeLimit() {
        // 连续请求11次，第11次应该被限流
        for (int i = 0; i < 11; i++) {
            try {
                ResponseEntity<Object> response = authController.code();
                System.out.println("第" + (i + 1) + "次请求，状态码：" + response.getStatusCode());
            } catch (Exception e) {
                System.out.println("第" + (i + 1) + "次请求，异常：" + e.getMessage());
            }
        }
    }
}
