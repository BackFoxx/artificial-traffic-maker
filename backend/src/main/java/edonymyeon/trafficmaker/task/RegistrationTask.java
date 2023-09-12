package edonymyeon.trafficmaker.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private final ObjectMapper objectMapper;

    private static final String SERVER_URL_PREFIX = "http://43.201.69.141:8080";
    public static final String STATUS = "status";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String NICKNAME = "nickname";
    public static final String DEVICE_TOKEN = "deviceToken";
    public static final String REGISTRATION_PATH = "/join";

    @Override
    public Map<String, Object> execute(final Map<String, Object> resource) {

        final RestTemplate restTemplate = new RestTemplate();
        final HttpHeaders httpHeaders = makeHeader();
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
