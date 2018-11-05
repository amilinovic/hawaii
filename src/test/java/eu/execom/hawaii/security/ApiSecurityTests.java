package eu.execom.hawaii.security;

import eu.execom.hawaii.service.IdTokenVerifier;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static eu.execom.hawaii.security.IdTokenVerifierFilter.ID_TOKEN_HEADER;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiSecurityTests {
    private static final String SAMPLE_PROTECTED_URL_PATH = "/security/test";
    private static final String SAMPLE_UNPROTECTED_URL_PATH = "/";
    private static final String SAMPLE_ID_TOKEN = "-- id token --";

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private IdTokenVerifier idTokenVerifier;

    @Test
    public void shouldReturnUnauthorizedStatusCodeForProtectedUrlRequestsWhenIdTokenHeaderIsNotSet() {
        ResponseEntity<String> response = restTemplate.getForEntity(SAMPLE_PROTECTED_URL_PATH, String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

    @Test
    public void shouldReturnUnauthorizedStatusCodeForProtectedUrlRequestsWhenIdTokenHeaderIsSetToEmptyValue() {
        ResponseEntity<String> response = restTemplate.exchange(SAMPLE_PROTECTED_URL_PATH, HttpMethod.GET, new HttpEntity<>(createIdTokenHeader("   ")), String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

    @Test
    public void shouldReturnUnauthorizedStatusCodeForProtectedUrlRequestsWhenIdTokenIsInvalid() {
        ResponseEntity<String> response = restTemplate.exchange(SAMPLE_PROTECTED_URL_PATH, HttpMethod.GET, new HttpEntity<>(createIdTokenHeader(SAMPLE_ID_TOKEN)), String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

    private HttpHeaders createIdTokenHeader(String value) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(ID_TOKEN_HEADER, value);
        return headers;
    }

    @Test
    public void shouldReturnOkResponseForProtectedUrlRequestsWhenIdTokenIsValid() {
        given(idTokenVerifier.verify(SAMPLE_ID_TOKEN)).willReturn(true);

        ResponseEntity<String> response = restTemplate.exchange(SAMPLE_PROTECTED_URL_PATH, HttpMethod.GET, new HttpEntity<>(createIdTokenHeader(SAMPLE_ID_TOKEN)), String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is("Security test action reached."));
    }

    @Test
    public void shouldNotRequireIdTokenHeaderForUnprotectedUrlRequests() {
        ResponseEntity<String> response = restTemplate.getForEntity(SAMPLE_UNPROTECTED_URL_PATH, String.class);

        assertThat(response.getStatusCode(), is(not(HttpStatus.UNAUTHORIZED)));
    }
}
