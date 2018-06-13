package eu.execom.hawaii.repository;

import org.springframework.data.repository.CrudRepository;

import eu.execom.hawaii.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
}
