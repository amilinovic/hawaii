package eu.execom.hawaii.repository;

import eu.execom.hawaii.model.Request;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.enumerations.AbsenceType;
import eu.execom.hawaii.model.enumerations.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

  List<Request> findAllByRequestStatus(RequestStatus requestStatus);

  List<Request> findAllByAbsence_AbsenceType(AbsenceType absenceType);

  List<Request> findAllByUser(User user);

}
