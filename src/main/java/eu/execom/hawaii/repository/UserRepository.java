package eu.execom.hawaii.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import eu.execom.hawaii.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

}
