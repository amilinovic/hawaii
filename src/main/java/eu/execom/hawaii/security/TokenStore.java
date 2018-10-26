package eu.execom.hawaii.security;

import eu.execom.hawaii.model.User;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TokenStore {

  private static final Map<String, User> tokens = new ConcurrentHashMap<>();

  public String createToken(User user) {
    String token = UUID.randomUUID().toString();
    tokens.put(token, user);
    return token;
  }

  public User getUser(String token) {
    return tokens.get(token);
  }

  public void removeTokensForUser(User user) {
    tokens.keySet()
          .stream()
          .filter(token -> Objects.equals(tokens.get(token).getId(), user.getId()))
          .forEach(tokens::remove);
  }
}
