package eu.execom.hawaii.repository;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

public class CustomizedTeamRepositoryImpl<Team, Long> extends SimpleJpaRepository<Team, Long>
    implements CustomizedTeamRepository<Team, Long> {

  private JpaEntityInformation<Team, ?> entityInformation;
  private EntityManager entityManager;

  public CustomizedTeamRepositoryImpl(JpaEntityInformation<Team, ?> entityInformation, EntityManager entityManager) {
    super(entityInformation, entityManager);
    this.entityInformation = entityInformation;
    this.entityManager = entityManager;
  }

  @Override
  @Transactional
  public Team create(Team team) {
    if (entityInformation.isNew(team)) {
      entityManager.persist(team);
    }

    return entityManager.merge(team);
  }

}