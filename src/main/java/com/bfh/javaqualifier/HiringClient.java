package com.bfh.javaqualifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class HiringClient {

    private static final Logger log = LoggerFactory.getLogger(HiringClient.class);
    private final RestTemplate restTemplate = new RestTemplate();
    private final AppProperties props;

    public HiringClient(AppProperties props) {
        this.props = props;
    }

    public GenerateWebhookResponse generateWebhook() {
        String url = props.getGenerateUrl();
        GenerateWebhookRequest body = new GenerateWebhookRequest(props.getName(), props.getRegNo(), props.getEmail());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<GenerateWebhookRequest> entity = new HttpEntity<>(body, headers);

        log.info("POST {}", url);
        ResponseEntity<GenerateWebhookResponse> resp = restTemplate.exchange(url, HttpMethod.POST, entity, GenerateWebhookResponse.class);

        if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
            throw new IllegalStateException("Failed to generate webhook: " + resp.getStatusCode());
        }
        log.info("Received webhook={}, accessToken=***", resp.getBody().getWebhook());
        return resp.getBody();
    }

    public void submitFinalQuery(String accessToken, String webhookUrl, String finalSql) {
        String url = (webhookUrl != null && !webhookUrl.isBlank()) ? webhookUrl : props.getFallbackSubmitUrl();

        Map<String, String> payload = new HashMap<>();
        payload.put("finalQuery", finalSql);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Spec shows Authorization: <accessToken> (no Bearer)
        headers.set("Authorization", accessToken);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(payload, headers);

        log.info("Submitting SQL to {}", url);
        ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        log.info("Submission status={} body={}", resp.getStatusCode(), resp.getBody());
    }
}
