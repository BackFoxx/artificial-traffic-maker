package edonymyeon.trafficmaker.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edonymyeon.trafficmaker.task.dto.RegistrationRequest;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class RegistrationTask extends Task {

    private static final String SERVER_URL_PREFIX = "http://43.201.69.141:8080";

    @Override
    public Map<String, Object> execute(final Map<String, Object> resource) {

        final RestTemplate restTemplate = new RestTemplate();

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAcceptCharset(List.of(StandardCharsets.UTF_8));

        final ObjectMapper objectMapper = new ObjectMapper();
        final RegistrationRequest registrationRequest = new RegistrationRequest(
                (String) resource.get("email"),
                (String) resource.get("password"),
                (String) resource.get("nickname"),
                (String) resource.get("deviceToken")
        );

        final HttpEntity<String> entity;
        try {
            entity = new HttpEntity<>(objectMapper.writeValueAsString(registrationRequest), httpHeaders);
            final ResponseEntity<String> response = restTemplate.exchange(
                    SERVER_URL_PREFIX + "/join",
                    HttpMethod.POST,
                    entity,
                    String.class
            );
            return Map.of("status", HttpStatus.valueOf(response.getStatusCode().value()));
        } catch (JsonProcessingException e) {
            return Map.of("status", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (HttpClientErrorException e) {
            log.error(e.getMessage(), e);
            return Map.of("status", HttpStatus.valueOf(e.getStatusCode().value()));
        }
    }
}
