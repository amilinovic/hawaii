package eu.execom.hawaii.service;

public interface IdTokenVerifier {
    boolean verify(String token);
}
