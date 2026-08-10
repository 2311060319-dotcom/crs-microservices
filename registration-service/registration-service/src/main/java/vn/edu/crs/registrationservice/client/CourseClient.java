package vn.edu.crs.registrationservice.client;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class CourseClient {

    private final RestTemplate restTemplate;

    @Value("${course-service.base-url}")
    private String baseUrl;

    // TRỪ 1 CHỖ
    public void reserveSeat(Long courseId) {

        String url =
                baseUrl
                        + "/internal/courses/"
                        + courseId
                        + "/reserve-seat";

        restTemplate.exchange(
                url,
                HttpMethod.PATCH,
                null,
                Object.class
        );
    }

    // HOÀN 1 CHỖ
    public void releaseSeat(Long courseId) {

        String url =
                baseUrl
                        + "/internal/courses/"
                        + courseId
                        + "/release-seat";

        restTemplate.exchange(
                url,
                HttpMethod.PATCH,
                null,
                Object.class
        );
    }
}