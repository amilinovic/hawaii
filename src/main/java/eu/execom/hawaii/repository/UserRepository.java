package eu.execom.hawaii.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import eu.execom.hawaii.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

  User findByEmail(String email);

}
