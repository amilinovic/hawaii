package eu.execom.hawaii.repository;

import eu.execom.hawaii.model.UserPushToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPushTokensRepository extends JpaRepository<UserPushToken, Long> {
}
