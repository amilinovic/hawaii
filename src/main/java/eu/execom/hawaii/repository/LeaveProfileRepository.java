package eu.execom.hawaii.repository;

import eu.execom.hawaii.model.LeaveProfile;
import eu.execom.hawaii.model.enumerations.LeaveProfileType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveProfileRepository extends JpaRepository<LeaveProfile, Long> {
  LeaveProfile findOneByLeaveProfileType(LeaveProfileType leaveProfileType);
  boolean existsByLeaveProfileType(LeaveProfileType leaveProfileType);
}
