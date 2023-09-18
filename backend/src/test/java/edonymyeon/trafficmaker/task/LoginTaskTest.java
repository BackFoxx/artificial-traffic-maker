package edonymyeon.trafficmaker.task;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest
class LoginTaskTest {

    @Autowired
    LoginTask loginTask;

    @Test
    @DisplayName("회원가입한 정보를 가지고 로그인 작업을 실행한다")
    void loginTask() {
        final Map<String, Object> resource = Map.of(
                Task.EMAIL, "backtest@gmail.com",
                Task.PASSWORD, "password123!",
                Task.DEVICE_TOKEN,
                "cipz3aE5RJumAhbdo0IZKD:APA91bG_A0MgYuq2LePhhJPTUdlfMj2qvdY7SsdC6sjv18x1ROwf0ud8_h0LmjVPDjfmvvTQ40V7Fp5Ix2NyrdlboSFUVvYv9TYnzGA1pVl5XBs0yOzNVXbWXmzTNkPm4cmvdiE7s16l"
        );

        final Map<String, Object> response = loginTask.execute(resource);

        assertThat(response.get(Task.STATUS)).isEqualTo(HttpStatus.OK);
        assertThat(response.get(LoginTask.AUTHORIZATION)).isNotNull();
    }
}
