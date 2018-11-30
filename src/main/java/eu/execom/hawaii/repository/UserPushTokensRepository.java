package eu.execom.hawaii.repository;

import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.UserPushToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPushTokensRepository extends JpaRepository<UserPushToken, Long> {
  UserPushToken findOneByPushToken(String pushToken);

  UserPushToken findOneByUserAndPushToken(User user, String pushToken);
}
