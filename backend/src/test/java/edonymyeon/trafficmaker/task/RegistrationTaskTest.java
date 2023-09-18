package edonymyeon.trafficmaker.task;

import java.util.HashMap;
import java.util.Map;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest
class RegistrationTaskTest {

    @Autowired
    private RegistrationTask registrationTask;

    @Test
    @DisplayName("회원가입 테스크를 실행하면 회원가입 작업을 실제로 수행한다")
    void taskWork1() {
        final Map<String, Object> response = registrationTask.execute(new HashMap<>());

        SoftAssertions
                .assertSoftly(softAssertions -> softAssertions.assertThat(response.get(Task.STATUS))
                        .isEqualTo(HttpStatus.CREATED));
    }
}
