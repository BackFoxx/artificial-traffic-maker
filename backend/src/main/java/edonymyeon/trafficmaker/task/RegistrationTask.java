package edonymyeon.trafficmaker.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edonymyeon.trafficmaker.chatgpt.ChatService;
import edonymyeon.trafficmaker.task.dto.RegistrationRequest;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Component
public class RegistrationTask extends Task {

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    private final ChatService chatService;

    private static final String REGISTRATION_PATH = "/join";

    @Override
    public Map<String, Object> execute(final Map<String, Object> resource) {
        final HttpHeaders httpHeaders = makeHeader();
        makeJoinRequest(resource);
        final String requestBody = makeRequestBody(resource);

        try {
            final HttpEntity<String> entity = new HttpEntity<>(requestBody, httpHeaders);
            final ResponseEntity<String> response = restTemplate.exchange(
                    SERVER_URL_PREFIX + REGISTRATION_PATH,
                    HttpMethod.POST,
                    entity,
                    String.class
            );
            return Map.of(STATUS, HttpStatus.valueOf(response.getStatusCode().value()));

        } catch (HttpClientErrorException e) {
            log.error(e.getMessage(), e);
            return Map.of(STATUS, HttpStatus.valueOf(e.getStatusCode().value()));
        }
    }

    private void makeJoinRequest(final Map<String, Object> resource) {
        final String nickname = chatService.getChatResponse("형용사 1개 + 동물 이름 1개로 이루어진 닉네임을 다른 수식어 없이 닉네임만 알려줘").trim();
        final String password = "testPassword123!";
        final String email = chatService.getChatResponse(
                String.format("%s를 띄어쓰기 없는 영어로 바꾸고, 그 뒤에 @Gmail.com을 붙여줘. 설명을 붙이지 말고 이메일만 알려줘.", nickname)).trim();
        final String deviceToken = "testDeviceToken";

        resource.put(NICKNAME, nickname);
        resource.put(PASSWORD, password);
        resource.put(EMAIL, email);
        resource.put(DEVICE_TOKEN, deviceToken);
    }

    private String makeRequestBody(final Map<String, Object> resource) {
        try {
            final RegistrationRequest registrationRequest = new RegistrationRequest(
                    (String) resource.get(EMAIL),
                    (String) resource.get(PASSWORD),
                    (String) resource.get(NICKNAME),
                    (String) resource.get(DEVICE_TOKEN)
            );
            return objectMapper.writeValueAsString(registrationRequest);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static HttpHeaders makeHeader() {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAcceptCharset(List.of(StandardCharsets.UTF_8));
        return httpHeaders;
    }
}
