package edonymyeon.trafficmaker.task;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TaskTest {
    @Test
    @DisplayName("회원가입 작업을 수행하는 테스크를 정의할 수 있다.")
    void taskDefine1() {
        final Task task = TaskHolder.getTask("REGISTRATION");
        assertThat(task).isInstanceOf(RegistrationTask.class);
    }
}
