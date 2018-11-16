package eu.execom.hawaii.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class GoogleTokenIdentityVerifier implements TokenIdentityVerifier {
  private static final String EXECOM_DOMAIN = "execom.eu";

  private final GoogleIdTokenVerifier googleVerifier;

  public GoogleTokenIdentityVerifier(GoogleIdTokenVerifier googleVerifier) {
    this.googleVerifier = googleVerifier;
  }

  @Override
  public Optional<String> getIdentityOf(String token) {
    GoogleIdToken idToken = null;
    try {
      idToken = googleVerifier.verify(token);
    } catch (Exception e) {
      log.error("Google unable to verify id token: " + token, e);
    }

    if (idToken == null) {
      log.error("Id token is not valid: {}", token);
      return Optional.empty();
    }

    if (!EXECOM_DOMAIN.equalsIgnoreCase(idToken.getPayload().getHostedDomain())) {
      log.error("User with invalid hosted domain tried to sign in. User: {}", idToken.getPayload().getEmail());
      return Optional.empty();
    }

    return Optional.of(idToken.getPayload().getEmail());
  }
}
