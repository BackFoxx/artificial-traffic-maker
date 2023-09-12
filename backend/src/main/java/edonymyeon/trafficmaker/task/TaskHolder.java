package edonymyeon.trafficmaker.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Objects;

public enum TaskHolder {

    REGISTRATION("REGISTRATION", new RegistrationTask(new ObjectMapper())),
    LOGIN("LOGIN", new LoginTask(new ObjectMapper()));

    private final String name;

    private final Task task;

    TaskHolder(final String name, final Task task) {
        this.name = name;
        this.task = task;
    }

    public static Task getTask(String taskName) {

        return Arrays.stream(values()).filter(task -> Objects.equals(task.name, taskName))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Not Existing task name."))
                .task;
    }
}
