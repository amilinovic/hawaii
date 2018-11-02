package eu.execom.hawaii.security;

import eu.execom.hawaii.service.IdTokenVerifier;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static eu.execom.hawaii.security.IdTokenVerifierFilter.ID_TOKEN_HEADER;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiSecurityTests {
    private static final String SAMPLE_ID_TOKEN = "-- id token --";

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @MockBean
    private IdTokenVerifier idTokenVerifier;

    private String testUrl;

    @Before
    public void setUp() {
        testUrl = "http://localhost:" + port + "/security/test";
    }

    @Test
    public void shouldReturnUnauthorizedStatusCodeWhenIdTokenHeaderIsNotSet() {
        ResponseEntity<String> response = restTemplate.getForEntity(testUrl, String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

    @Test
    public void shouldReturnUnauthorizedStatusCodeWhenIdTokenIsInvalid() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(ID_TOKEN_HEADER, SAMPLE_ID_TOKEN);
        ResponseEntity<String> response = restTemplate.exchange(testUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }
}
