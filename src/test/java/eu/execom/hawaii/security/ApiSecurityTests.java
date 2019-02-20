package eu.execom.hawaii.security;

import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.UserPushToken;
import eu.execom.hawaii.model.enumerations.UserRole;
import eu.execom.hawaii.model.enumerations.UserStatusType;
import eu.execom.hawaii.service.EntityBuilder;
import eu.execom.hawaii.service.TokenIdentityVerifier;
import eu.execom.hawaii.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static eu.execom.hawaii.security.IdTokenVerifierFilter.ID_TOKEN_HEADER;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiSecurityTests {
  private static final String SAMPLE_PROTECTED_URL_PATH = "/api/security/test";
  private static final String SAMPLE_UNPROTECTED_URL_PATH = "/";
  private static final String SAMPLE_ID_TOKEN = "-- id token --";
  private static final String SAMPLE_PROTECTED_HR_MANAGER_URL_PATH = "/api/users";
  private static final String SAMPLE_USER = "Olivera";

  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private TokenIdentityVerifier tokenIdentityVerifier;

  @MockBean
  private UserService userService;

  @Test
  public void shouldReturnUnauthorizedStatusCodeForProtectedUrlRequestsWhenIdTokenHeaderIsNotSet() {
    ResponseEntity<String> response = restTemplate.getForEntity(SAMPLE_PROTECTED_URL_PATH, String.class);

    assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void shouldReturnUnauthorizedStatusCodeForProtectedUrlRequestsWhenIdTokenHeaderIsSetToEmptyValue() {
    ResponseEntity<String> response = restTemplate.exchange(SAMPLE_PROTECTED_URL_PATH, HttpMethod.GET,
        new HttpEntity<>(createIdTokenHeader("   ")), String.class);

    assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void shouldReturnUnauthorizedStatusCodeForProtectedUrlRequestsWhenIdTokenIsInvalid() {
    ResponseEntity<String> response = restTemplate.exchange(SAMPLE_PROTECTED_URL_PATH, HttpMethod.GET,
        new HttpEntity<>(createIdTokenHeader(SAMPLE_ID_TOKEN)), String.class);

    assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void shouldReturnUnauthorizedStatusCodeForProtectedUrlRequestsWhenIdTokenIsValidButUserIsNotFound() {
    given(tokenIdentityVerifier.getIdentityOf(SAMPLE_ID_TOKEN)).willReturn(Optional.of(SAMPLE_USER));

    ResponseEntity<String> response = restTemplate.exchange(SAMPLE_PROTECTED_URL_PATH, HttpMethod.GET,
        new HttpEntity<>(createIdTokenHeader(SAMPLE_ID_TOKEN)), String.class);

    assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void shouldReturnForbiddenStatusCodeForProtectedUrlRequestsWhenIdTokenIsValidButUserIsInactive() {
    given(tokenIdentityVerifier.getIdentityOf(SAMPLE_ID_TOKEN)).willReturn(Optional.of(SAMPLE_USER));
    given(userService.findByEmail(SAMPLE_USER)).willReturn(new User());

    ResponseEntity<String> response = restTemplate.exchange(SAMPLE_PROTECTED_URL_PATH, HttpMethod.GET,
        new HttpEntity<>(createIdTokenHeader(SAMPLE_ID_TOKEN)), String.class);

    assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
  }

  @Test
  public void shouldReturnOkResponseForProtectedUrlRequestsWhenIdTokenIsValidAndUserIsActive() {
    User user = new User();
    user.setUserStatusType(UserStatusType.ACTIVE);
    user.setUserRole(UserRole.USER);
    given(tokenIdentityVerifier.getIdentityOf(SAMPLE_ID_TOKEN)).willReturn(Optional.of(SAMPLE_USER));
    given(userService.findByEmail(SAMPLE_USER)).willReturn(user);

    ResponseEntity<String> response = restTemplate.exchange(SAMPLE_PROTECTED_URL_PATH, HttpMethod.GET,
        new HttpEntity<>(createIdTokenHeader(SAMPLE_ID_TOKEN)), String.class);

    assertThat(response.getStatusCode(), is(HttpStatus.OK));
    assertThat(response.getBody(), is("Security test action reached."));
  }

  @Test
  public void shouldNotRequireIdTokenHeaderForUnprotectedUrlRequests() {
    ResponseEntity<String> response = restTemplate.getForEntity(SAMPLE_UNPROTECTED_URL_PATH, String.class);

    assertThat(response.getStatusCode(), is(not(HttpStatus.UNAUTHORIZED)));
    assertThat(response.getStatusCode(), is(not(HttpStatus.FORBIDDEN)));
  }

  @Test
  public void shouldReturnUnauthorizedStatusCodeForProtectedUrlRequestsWhichRequireHrManagerAuthorityWhenIdTokenHeaderIsNotSet() {
    ResponseEntity<String> response = restTemplate.getForEntity(SAMPLE_PROTECTED_HR_MANAGER_URL_PATH, String.class);

    assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void shouldReturnUnauthorizedStatusCodeForProtectedUrlRequestsWhichRequireHrManagerAuthorityWhenIdTokenHeaderIsSetToEmptyValue() {
    ResponseEntity<String> response = restTemplate.exchange(SAMPLE_PROTECTED_HR_MANAGER_URL_PATH, HttpMethod.GET,
        new HttpEntity<>(createIdTokenHeader("   ")), String.class);

    assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void shouldReturnUnauthorizedStatusCodeForProtectedUrlRequestsWhichRequireHrManagerAuthorityWhenIdTokenIsInvalid() {
    ResponseEntity<String> response = restTemplate.exchange(SAMPLE_PROTECTED_HR_MANAGER_URL_PATH, HttpMethod.GET,
        new HttpEntity<>(createIdTokenHeader(SAMPLE_ID_TOKEN)), String.class);

    assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void shouldReturnUnauthorizedStatusCodeForProtectedUrlRequestsWhichRequireHrManagerAuthorityWhenIdTokenIsValidButUserIsNotFound() {
    given(tokenIdentityVerifier.getIdentityOf(SAMPLE_ID_TOKEN)).willReturn(Optional.of(SAMPLE_USER));

    ResponseEntity<String> response = restTemplate.exchange(SAMPLE_PROTECTED_HR_MANAGER_URL_PATH, HttpMethod.GET,
        new HttpEntity<>(createIdTokenHeader(SAMPLE_ID_TOKEN)), String.class);

    assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
  }

  @Test
  public void shouldReturnForbiddenStatusCodeForProtectedUrlRequestsWhichRequireHrManagerAuthorityWhenIdTokenIsValidButUserIsInactive() {
    given(tokenIdentityVerifier.getIdentityOf(SAMPLE_ID_TOKEN)).willReturn(Optional.of(SAMPLE_USER));
    given(userService.findByEmail(SAMPLE_USER)).willReturn(new User());

    ResponseEntity<String> response = restTemplate.exchange(SAMPLE_PROTECTED_HR_MANAGER_URL_PATH, HttpMethod.GET,
        new HttpEntity<>(createIdTokenHeader(SAMPLE_ID_TOKEN)), String.class);

    assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
  }

  @Test
  public void shouldReturnForbiddenStatusCodeForProtectedUrlRequestsWhichRequireHrManagerAuthorityWhenActiveUserIsNotHrManager() {
    User user = new User();
    user.setUserStatusType(UserStatusType.ACTIVE);
    user.setUserRole(UserRole.USER);
    given(tokenIdentityVerifier.getIdentityOf(SAMPLE_ID_TOKEN)).willReturn(Optional.of(SAMPLE_USER));
    given(userService.findByEmail(SAMPLE_USER)).willReturn(user);

    ResponseEntity<String> response = restTemplate.exchange(SAMPLE_PROTECTED_HR_MANAGER_URL_PATH, HttpMethod.GET,
        new HttpEntity<>(createIdTokenHeader(SAMPLE_ID_TOKEN)), String.class);

    assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
  }

  @Test
  public void shouldReturnOkResponseForProtectedUrlRequestsWhichRequireHrManagerAuthorityWhenActiveUserIsHrManager() {
    User user = EntityBuilder.user(EntityBuilder.team());
    Pageable pageable = PageRequest.of(0, 30);
    List<UserStatusType> userStatusTypes = new ArrayList<>();
    List<User> users = new ArrayList<>();
    users.add(user);
    user.setUserPushTokens(List.of(new UserPushToken()));
    Page<User> pageableUsers = new PageImpl<>(users, pageable, users.size());
    userStatusTypes.add(UserStatusType.ACTIVE);

    given(tokenIdentityVerifier.getIdentityOf(SAMPLE_ID_TOKEN)).willReturn(Optional.of(SAMPLE_USER));
    given(userService.findByEmail(SAMPLE_USER)).willReturn(user);
    given(userService.findAllByUserStatusTypePage(userStatusTypes, pageable)).willReturn(pageableUsers);

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
