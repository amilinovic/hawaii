package eu.execom.hawaii.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import eu.execom.hawaii.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  List<User> findAllByActive(boolean active);

  Page<User> findAllByActiveAndEmailContainingOrFullNameContaining(boolean active, String email, String fullName,
      Pageable pageable);

}
