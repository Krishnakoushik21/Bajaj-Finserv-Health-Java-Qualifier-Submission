package com.bfh.javaqualifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class Application implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Application.class);
    private final AppProperties props;
    private final HiringClient client;
    private final SqlSolver solver;

    public Application(AppProperties props, HiringClient client) {
        this.props = props;
        this.client = client;
        this.solver = new SqlSolver();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("Starting Qualifier flow for regNo={} name={} email={}", props.getRegNo(), props.getName(), props.getEmail());

        // 1) Generate webhook + token
        GenerateWebhookResponse resp = client.generateWebhook();

        // 2) Build final SQL based on regNo
        String finalSql = solver.generateFinalQuery(props.getRegNo());
        log.info("Final SQL (first 120 chars): {}{}", System.lineSeparator(), preview(finalSql, 120));

        // 3) Submit final SQL using token and webhook
        client.submitFinalQuery(resp.getAccessToken(), resp.getWebhook(), finalSql);

        log.info("Flow complete.");
    }

    private String preview(String s, int max) {
        return s.length() <= max ? s : s.substring(0, max) + "...";
    }
}
