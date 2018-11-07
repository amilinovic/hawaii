package eu.execom.hawaii.security;

import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.enumerations.UserRole;
import eu.execom.hawaii.service.IdTokenVerifier;
import eu.execom.hawaii.service.UserService;
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

import java.util.Optional;

import static eu.execom.hawaii.security.IdTokenVerifierFilter.ID_TOKEN_HEADER;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class) @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) public class ApiSecurityTests {
    private static final String SAMPLE_PROTECTED_URL_PATH = "/api/security/test";
    private static final String SAMPLE_UNPROTECTED_URL_PATH = "/";
    private static final String SAMPLE_ID_TOKEN = "-- id token --";
    private static final String SAMPLE_PROTECTED_HR_MANAGER_URL_PATH = "/api/users";
    private static final String SAMPLE_USER = "Olivera";

    @Autowired private TestRestTemplate restTemplate;

    @MockBean private IdTokenVerifier idTokenVerifier;

    @MockBean private UserService userService;

    @Test public void shouldReturnUnauthorizedStatusCodeForProtectedUrlRequestsWhenIdTokenHeaderIsNotSet() {
        ResponseEntity<String> response = restTemplate.getForEntity(SAMPLE_PROTECTED_URL_PATH, String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

    @Test public void shouldReturnUnauthorizedStatusCodeForProtectedUrlRequestsWhenIdTokenHeaderIsSetToEmptyValue() {
        ResponseEntity<String> response = restTemplate.exchange(SAMPLE_PROTECTED_URL_PATH, HttpMethod.GET,
                new HttpEntity<>(createIdTokenHeader("   ")), String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

    @Test public void shouldReturnUnauthorizedStatusCodeForProtectedUrlRequestsWhenIdTokenIsInvalid() {
        ResponseEntity<String> response = restTemplate.exchange(SAMPLE_PROTECTED_URL_PATH, HttpMethod.GET,
                new HttpEntity<>(createIdTokenHeader(SAMPLE_ID_TOKEN)), String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

    @Test public void shouldReturnUnauthorizedStatusCodeForProtectedUrlRequestsWhenIdTokenIsValidButUserIsNotFound() {
        given(idTokenVerifier.tryToGetIdentityOf(SAMPLE_ID_TOKEN)).willReturn(Optional.of(SAMPLE_USER));

        ResponseEntity<String> response = restTemplate.exchange(SAMPLE_PROTECTED_URL_PATH, HttpMethod.GET,
                new HttpEntity<>(createIdTokenHeader(SAMPLE_ID_TOKEN)), String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

    @Test public void shouldReturnForbiddenStatusCodeForProtectedUrlRequestsWhenIdTokenIsValidButUserIsInactive() {
        given(idTokenVerifier.tryToGetIdentityOf(SAMPLE_ID_TOKEN)).willReturn(Optional.of(SAMPLE_USER));
        given(userService.findByEmail(SAMPLE_USER)).willReturn(new User());

        ResponseEntity<String> response = restTemplate.exchange(SAMPLE_PROTECTED_URL_PATH, HttpMethod.GET,
                new HttpEntity<>(createIdTokenHeader(SAMPLE_ID_TOKEN)), String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test public void shouldReturnOkResponseForProtectedUrlRequestsWhenIdTokenIsValidAndUserIsActive() {
        User user = new User();
        user.setActive(true);
        user.setUserRole(UserRole.USER);
        given(idTokenVerifier.tryToGetIdentityOf(SAMPLE_ID_TOKEN)).willReturn(Optional.of(SAMPLE_USER));
        given(userService.findByEmail(SAMPLE_USER)).willReturn(user);

        ResponseEntity<String> response = restTemplate.exchange(SAMPLE_PROTECTED_URL_PATH, HttpMethod.GET,
                new HttpEntity<>(createIdTokenHeader(SAMPLE_ID_TOKEN)), String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is("Security test action reached."));
    }

    @Test public void shouldNotRequireIdTokenHeaderForUnprotectedUrlRequests() {
        ResponseEntity<String> response = restTemplate.getForEntity(SAMPLE_UNPROTECTED_URL_PATH, String.class);

        assertThat(response.getStatusCode(), is(not(HttpStatus.UNAUTHORIZED)));
        assertThat(response.getStatusCode(), is(not(HttpStatus.FORBIDDEN)));
    }

    @Test public void shouldReturnUnauthorizedStatusCodeForProtectedUrlRequestsWhichRequireHrManagerAuthorityWhenIdTokenHeaderIsNotSet() {
        ResponseEntity<String> response = restTemplate.getForEntity(SAMPLE_PROTECTED_HR_MANAGER_URL_PATH, String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

    @Test public void shouldReturnUnauthorizedStatusCodeForProtectedUrlRequestsWhichRequireHrManagerAuthorityWhenIdTokenHeaderIsSetToEmptyValue() {
        ResponseEntity<String> response = restTemplate.exchange(SAMPLE_PROTECTED_HR_MANAGER_URL_PATH, HttpMethod.GET,
                new HttpEntity<>(createIdTokenHeader("   ")), String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

    @Test public void shouldReturnUnauthorizedStatusCodeForProtectedUrlRequestsWhichRequireHrManagerAuthorityWhenIdTokenIsInvalid() {
        ResponseEntity<String> response = restTemplate.exchange(SAMPLE_PROTECTED_HR_MANAGER_URL_PATH, HttpMethod.GET,
                new HttpEntity<>(createIdTokenHeader(SAMPLE_ID_TOKEN)), String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

    @Test public void shouldReturnUnauthorizedStatusCodeForProtectedUrlRequestsWhichRequireHrManagerAuthorityWhenIdTokenIsValidButUserIsNotFound() {
        given(idTokenVerifier.tryToGetIdentityOf(SAMPLE_ID_TOKEN)).willReturn(Optional.of(SAMPLE_USER));

        ResponseEntity<String> response = restTemplate.exchange(SAMPLE_PROTECTED_HR_MANAGER_URL_PATH, HttpMethod.GET,
                new HttpEntity<>(createIdTokenHeader(SAMPLE_ID_TOKEN)), String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

    @Test public void shouldReturnForbiddenStatusCodeForProtectedUrlRequestsWhichRequireHrManagerAuthorityWhenIdTokenIsValidButUserIsInactive() {
        given(idTokenVerifier.tryToGetIdentityOf(SAMPLE_ID_TOKEN)).willReturn(Optional.of(SAMPLE_USER));
        given(userService.findByEmail(SAMPLE_USER)).willReturn(new User());

        ResponseEntity<String> response = restTemplate.exchange(SAMPLE_PROTECTED_HR_MANAGER_URL_PATH, HttpMethod.GET,
                new HttpEntity<>(createIdTokenHeader(SAMPLE_ID_TOKEN)), String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test public void shouldReturnForbiddenStatusCodeForProtectedUrlRequestsWhichRequireHrManagerAuthorityWhenActiveUserIsNotHrManager() {
        User user = new User();
        user.setActive(true);
        user.setUserRole(UserRole.USER);
        given(idTokenVerifier.tryToGetIdentityOf(SAMPLE_ID_TOKEN)).willReturn(Optional.of(SAMPLE_USER));
        given(userService.findByEmail(SAMPLE_USER)).willReturn(user);

        ResponseEntity<String> response = restTemplate.exchange(SAMPLE_PROTECTED_HR_MANAGER_URL_PATH, HttpMethod.GET,
                new HttpEntity<>(createIdTokenHeader(SAMPLE_ID_TOKEN)), String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    @Test public void shouldReturnOkResponseForProtectedUrlRequestsWhichRequireHrManagerAuthorityWhenActiveUserIsHrManager() {
        User user = new User();
        user.setActive(true);
        user.setUserRole(UserRole.HR_MANAGER);
        given(idTokenVerifier.tryToGetIdentityOf(SAMPLE_ID_TOKEN)).willReturn(Optional.of(SAMPLE_USER));
        given(userService.findByEmail(SAMPLE_USER)).willReturn(user);

        ResponseEntity<String> response = restTemplate.exchange(SAMPLE_PROTECTED_HR_MANAGER_URL_PATH, HttpMethod.GET,
                new HttpEntity<>(createIdTokenHeader(SAMPLE_ID_TOKEN)), String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    private HttpHeaders createIdTokenHeader(String value) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(ID_TOKEN_HEADER, value);
        return headers;
    }
}
