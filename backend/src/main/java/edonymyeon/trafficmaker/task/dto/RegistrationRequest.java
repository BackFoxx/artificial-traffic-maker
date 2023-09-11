package edonymyeon.trafficmaker.task.dto;

public record RegistrationRequest(String email, String password, String nickname, String deviceToken) {
}
