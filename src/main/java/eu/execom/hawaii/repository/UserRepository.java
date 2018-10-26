package eu.execom.hawaii.repository;

import eu.execom.hawaii.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  User findOneByEmail(String email);

  List<User> findAllByActive(boolean active);

  Page<User> findAllByActiveAndEmailContainingOrFullNameContaining(boolean active, String email, String fullName,
      Pageable pageable);

}
