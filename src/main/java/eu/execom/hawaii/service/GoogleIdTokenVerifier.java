package eu.execom.hawaii.service;

import org.springframework.stereotype.Component;

@Component
public class GoogleIdTokenVerifier implements IdTokenVerifier {
    @Override
    public boolean verify(String token) {
        throw new UnsupportedOperationException();
    }
}
