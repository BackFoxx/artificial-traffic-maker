package edonymyeon.trafficmaker.task;

import java.util.Map;
import java.util.Optional;

public enum TaskHolder {

    REGISTRATION("REGISTRATION", new RegistrationTask());

    private final String name;

    private final Task task;

    TaskHolder(final String name, final Task task) {
        this.name = name;
        this.task = task;
    }

    private static final Map<String, Task> tasks = Map.of(
            REGISTRATION.name, REGISTRATION.task
    );

    public static Task getTask(String taskName) {
        return Optional.ofNullable(tasks.get(taskName))
                .orElseThrow(() -> new IllegalArgumentException("Not Existing task name."));
    }
}
