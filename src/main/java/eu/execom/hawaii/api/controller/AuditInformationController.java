package eu.execom.hawaii.api.controller;

import eu.execom.hawaii.dto.AuditInformationDto;
import eu.execom.hawaii.service.AuditInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/audit")
public class AuditInformationController {

  private AuditInformationService auditInformationService;

  @Autowired
  public AuditInformationController(AuditInformationService auditInformationService) {
    this.auditInformationService = auditInformationService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<AuditInformationDto> getAuditInformation(@PathVariable Long id) {
    var auditInformation = auditInformationService.getById(id);
    var auditInformationDto = new AuditInformationDto(auditInformation);

    return new ResponseEntity<>(auditInformationDto, HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<List<AuditInformationDto>> getAllAuditInformations() {
    var allAuditInformation = auditInformationService.getAll();
    var allAuditInformationDto = allAuditInformation.stream()
                                                    .map(AuditInformationDto::new)
                                                    .collect(Collectors.toList());
    return new ResponseEntity<>(allAuditInformationDto, HttpStatus.OK);
  }

}
