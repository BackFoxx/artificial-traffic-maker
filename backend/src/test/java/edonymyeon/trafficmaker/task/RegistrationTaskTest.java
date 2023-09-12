package edonymyeon.trafficmaker.task;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.Random;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class RegistrationTaskTest {

    @Test
    @DisplayName("회원가입 작업을 수행하는 테스크를 정의할 수 있다.")
    void taskDefine1() {
        final Task task = TaskHolder.getTask("REGISTRATION");
        assertThat(task).isInstanceOf(RegistrationTask.class);
    }

    @Test
    @DisplayName("회원가입 테스크를 실행하면 회원가입 작업을 실제로 수행한다")
    void taskWork1() {
        final Task task = TaskHolder.getTask("REGISTRATION");
        final Map<String, Object> joinRequestBody = Map.of(
                Task.EMAIL, String.format("random%d@gmail.com", new Random().nextInt(999999999)),
                Task.PASSWORD, "password123!",
                Task.NICKNAME, String.format("randomNickname%d", new Random().nextInt(99)),
                Task.DEVICE_TOKEN, "testDeviceToken"
        );

        final Map<String, Object> response = task.execute(joinRequestBody);

        SoftAssertions
                .assertSoftly(softAssertions -> softAssertions.assertThat(response.get(Task.STATUS))
                        .isEqualTo(HttpStatus.CREATED));
    }
}
