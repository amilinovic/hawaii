package eu.execom.hawaii.security;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiSecurityTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Test
    public void shouldReturnUnauthorizedStatusCodeWhenIdTokenHeaderIsNotSet() {
        String url = "http://localhost:" + port + "/security/test";

        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);

        Assert.assertThat(response.getStatusCode(), Matchers.is(HttpStatus.UNAUTHORIZED));
    }
}
