package eu.execom.hawaii.service;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component public class GoogleTokenIdentityVerifier implements TokenIdentityVerifier {
    @Override public Optional<String> tryToGetIdentityOf(String token) {
        throw new UnsupportedOperationException();
    }
}
