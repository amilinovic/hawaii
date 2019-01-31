package eu.execom.hawaii.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import eu.execom.hawaii.model.AuditInformation;
import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.enumerations.AuditedEntity;
import eu.execom.hawaii.model.enumerations.OperationPerformed;
import eu.execom.hawaii.repository.AuditInformationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class AuditInformationService {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private AuditInformationRepository auditInformationRepository;

  @Autowired
  public AuditInformationService(AuditInformationRepository auditInformationRepository) {
    this.auditInformationRepository = auditInformationRepository;
  }

  public AuditInformation getById(Long id) {
    return auditInformationRepository.getOne(id);
  }

  public List<AuditInformation> getAll() {
    return auditInformationRepository.findAll();
  }

  /**
   * Build audit information and save it.
   *
   * @param operationPerformed what operation was performed (CREATE, REMOVE, UPDATE, DELETE, ACTIVATE).
   * @param entity             on witch entity was the operation performed (USER, TEAM, REQUEST).
   * @param modifiedByUser     user that made change on that entity.
   * @param modifiedUser       user on witch change was performed.
   * @param previousState      state of the object before the change.
   * @param currentState       state of the object after the change.
   */
  public void buildAuditInformation(OperationPerformed operationPerformed, AuditedEntity entity, User modifiedByUser,
      User modifiedUser, Object previousState, Object currentState) {

    AuditInformation auditInformation = new AuditInformation();

    auditInformation.setOperationPerformed(operationPerformed);
    auditInformation.setAuditedEntity(entity);
    auditInformation.setModifiedDateTime(LocalDateTime.now());
    auditInformation.setModifiedByUser(modifiedByUser);
    auditInformation.setModifiedUser(modifiedUser);
    auditInformation.setPreviousValue(convertToJSON(previousState));
    auditInformation.setCurrentValue(convertToJSON(currentState));

    auditInformationRepository.save(auditInformation);
  }

  /**
   * Convert object to string formatted as JSON.
   *
   * @param objectForSerialization previous or current state of the object.
   */
  private String convertToJSON(Object objectForSerialization) {
    OBJECT_MAPPER.registerModule(new JavaTimeModule());
    OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    String currentValue = null;

    try {
      currentValue = OBJECT_MAPPER.writeValueAsString(objectForSerialization);
    } catch (JsonProcessingException e) {
      log.error("Failed to map object of class '{}' as string", objectForSerialization.getClass());
      e.printStackTrace();
    }

    return currentValue;
  }

}
