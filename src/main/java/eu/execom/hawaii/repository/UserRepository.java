package eu.execom.hawaii.repository;

import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.enumerations.UserStatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  User findOneByEmail(String email);

  List<User> findAllByUserStatusTypeIn(List<UserStatusType> userStatusType);

  Page<User> findAllByUserStatusTypeAndEmailContainingOrFullNameContaining(UserStatusType userStatusType, String email,
      String fullName, Pageable pageable);

}
