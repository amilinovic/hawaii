package eu.execom.hawaii.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CustomizedTeamRepository<Team, Long> extends JpaRepository<Team, Long> {
  Team create(Team team);
}