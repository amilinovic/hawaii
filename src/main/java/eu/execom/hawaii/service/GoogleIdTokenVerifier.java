package eu.execom.hawaii.service;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component public class GoogleIdTokenVerifier implements IdTokenVerifier {
    @Override public Optional<String> tryToGetIdentityOf(String token) {
        throw new UnsupportedOperationException();
    }
}
