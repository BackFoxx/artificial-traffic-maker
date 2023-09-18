package edonymyeon.trafficmaker.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edonymyeon.trafficmaker.task.dto.LoginRequest;
import edonymyeon.trafficmaker.task.exception.LoginFailedException;
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
@Component
@RequiredArgsConstructor
public class LoginTask extends Task {

    public static final String JSESSIONID = "JSESSIONID";
    private final ObjectMapper objectMapper;

    public static final String REGISTRATION_PATH = "/login";

    @Override
    public Map<String, Object> execute(final Map<String, Object> resource) {
        final RestTemplate restTemplate = new RestTemplate();
        final HttpHeaders httpHeaders = makeHeader();
        final String requestBody = makeRequestBody(resource);

        return sendRequest(restTemplate, httpHeaders, requestBody);
    }

    private static Map<String, Object> sendRequest(final RestTemplate restTemplate,
                                                          final HttpHeaders httpHeaders, final String requestBody) {
        try {
            final HttpEntity<String> entity = new HttpEntity<>(requestBody, httpHeaders);
            final ResponseEntity<String> response = restTemplate.exchange(
                    SERVER_URL_PREFIX + REGISTRATION_PATH,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            final HttpStatus httpStatus = HttpStatus.valueOf(response.getStatusCode().value());
            if (httpStatus.is2xxSuccessful()) {
                return Map.of(
                        STATUS, httpStatus,
                        JSESSIONID, response.getHeaders().get("Set-Cookie").get(0).replace("JSESSIONID=", "")
                );
            }

            throw new LoginFailedException();
        } catch (HttpClientErrorException e) {
            log.error(e.getMessage(), e);
            return Map.of(STATUS, HttpStatus.valueOf(e.getStatusCode().value()));
        }
    }

    private String makeRequestBody(final Map<String, Object> resource) {
        try {
            final LoginRequest loginRequest = new LoginRequest(
                    (String) resource.get(EMAIL),
                    (String) resource.get(PASSWORD),
                    (String) resource.get(DEVICE_TOKEN)
            );
            return objectMapper.writeValueAsString(loginRequest);
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
