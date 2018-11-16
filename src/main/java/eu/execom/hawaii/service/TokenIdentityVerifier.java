package eu.execom.hawaii.service;

import java.util.Optional;

public interface TokenIdentityVerifier {
  Optional<String> getIdentityOf(String token);
}