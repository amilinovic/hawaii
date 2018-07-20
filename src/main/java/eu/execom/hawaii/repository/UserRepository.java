package eu.execom.hawaii.repository;

import eu.execom.hawaii.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  List<User> findAllByActive(boolean active);

}
