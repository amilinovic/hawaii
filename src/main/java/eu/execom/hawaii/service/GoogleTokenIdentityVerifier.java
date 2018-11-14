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

    @Override public Optional<String> tryToGetIdentityOf(String token) {
        GoogleIdToken idToken = null;
        try {
            idToken = googleVerifier.verify(token);
        } catch (Exception e) {
            log.error("Google unable to verify id token", e);
        }

        if (idToken == null) {
            return Optional.empty();
        }

        if (!EXECOM_DOMAIN.equalsIgnoreCase(idToken.getPayload().getHostedDomain())) {
            return Optional.empty();
        }

        return Optional.of(idToken.getPayload().getEmail());
    }
}
