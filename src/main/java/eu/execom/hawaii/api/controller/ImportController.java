package eu.execom.hawaii.api.controller;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/import")
public class ImportController {

  private JobLauncher jobLauncher;
  private Job job;

  @Autowired
  public ImportController(JobLauncher jobLauncher, Job job) {
    this.jobLauncher = jobLauncher;
    this.job = job;
  }

  @PutMapping
  public ResponseEntity<BatchStatus> load()
      throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException,
      JobInstanceAlreadyCompleteException {

    Map<String, JobParameter> maps = new HashMap<>();
    maps.put("time", new JobParameter(System.currentTimeMillis()));
    JobParameters parameters = new JobParameters(maps);
    JobExecution jobExecution = jobLauncher.run(job, parameters);

    return new ResponseEntity<>(jobExecution.getStatus(), HttpStatus.OK);
  }

}
