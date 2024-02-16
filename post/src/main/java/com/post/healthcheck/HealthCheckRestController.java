package com.post.healthcheck;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.Charset;

@RestController
@RequestMapping("/healthcheck")
public class HealthCheckRestController {
    @Value("${service.j-user-service}")
    private String jUserService;

    @GetMapping("/ready")
    public String readinessProbe() {
        return "ready";
    }

    @GetMapping("/live")
    public String livenessProbe() {
        return "ok";
    }

    @GetMapping("/user")
    public String home() {
        URI uri = UriComponentsBuilder
                .fromUriString(jUserService)
                .path("/user/healthcheck/ready")
                .encode(Charset.defaultCharset())
                .build()
                .toUri();
        System.out.println(uri.toString());

        RestTemplate restTemplate = new RestTemplate();

        //String result = restTemplate.getForObject(uri, String.class);
        //getForEntity는 응답을 ResponseEntity로 받을 수 있도록 해준다 .
        //파라미터 첫번째는 요청 URI 이며 , 2번째는 받을 타입
        ResponseEntity<String> forEntity = restTemplate.getForEntity(uri, String.class);


        System.out.println(forEntity);
        System.out.println(forEntity.getBody());


        return "SUCCESS";
    }

}
