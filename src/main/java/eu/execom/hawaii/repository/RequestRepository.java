package eu.execom.hawaii.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import eu.execom.hawaii.model.Request;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.enumerations.AbsenceType;
import eu.execom.hawaii.model.enumerations.RequestStatus;

public interface RequestRepository extends JpaRepository<Request, Long> {

  List<Request> findAllByUser(User user);

  List<Request> findAllByApprover(User approver);

  List<Request> findAllByRequestStatus(RequestStatus requestStatus);

  List<Request> findAllByAbsence_AbsenceType(AbsenceType absenceType);

  List<Request> findAllByRequestStatusNot(RequestStatus requestStatus);

}
