package eu.execom.hawaii.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component public class GoogleTokenIdentityVerifier implements TokenIdentityVerifier {
    private final GoogleIdTokenVerifier googleVerifier;

    public GoogleTokenIdentityVerifier(GoogleIdTokenVerifier googleVerifier) {
        this.googleVerifier = googleVerifier;
    }

    @Override public Optional<String> tryToGetIdentityOf(String token) {
        GoogleIdToken idToken = null;
        try {
            idToken = googleVerifier.verify(token);
        } catch (Exception e) {

        }

        return Optional.empty();
    }
}
