package eu.execom.hawaii.service;

import java.util.Optional;

public interface IdTokenVerifier {
    Optional<String> tryToGetIdentityOf(String token);
}
