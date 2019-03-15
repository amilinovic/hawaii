package eu.execom.hawaii.configuration;

import eu.execom.hawaii.model.User;
import eu.execom.hawaii.model.batch.UserImport;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

  @Bean
  public DataSource dataSource() {
    final DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName("com.mysql.jdbc.Driver");
    dataSource.setUrl("jdbc:mysql://localhost:3306/hawaii?useSSL=false&useUnicode=true&characterEncoding=UTF-8");
    dataSource.setUsername("root");
    dataSource.setPassword("root");

    return dataSource;
  }

  @Bean
  public Job job(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
      ItemReader<UserImport> itemReader, ItemProcessor<UserImport, User> itemProcessor, ItemWriter<User> itemWriter) {

    Step step = stepBuilderFactory.get("File-load")
                                  .<UserImport, User>chunk(100)
                                  .reader(itemReader)
                                  .processor(itemProcessor)
                                  .writer(itemWriter)
                                  .build();

    return jobBuilderFactory.get("ReadCSVFileJob")
                            .incrementer(new RunIdIncrementer())
                            .start(step).build();
  }

  @Bean
  public FlatFileItemReader<UserImport> fileItemReader() {
    FlatFileItemReader<UserImport> flatFileItemReader = new FlatFileItemReader<>();
    flatFileItemReader.setResource(new ClassPathResource("export user-a Appogee leaves.csv"));
    flatFileItemReader.setName("CSV Reader");
    flatFileItemReader.setLinesToSkip(1);
    flatFileItemReader.setLineMapper(lineMapper());

    return flatFileItemReader;
  }

  @Bean
  public LineMapper<UserImport> lineMapper() {
    DefaultLineMapper<UserImport> defaultLineMapper = new DefaultLineMapper<>();

    DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
    lineTokenizer.setDelimiter(",");
    lineTokenizer.setStrict(false);
    lineTokenizer.setNames("FirstName", "LastName", "Email", "PayrollNo", "ContinuousStartDate", "StartDate",
        "EndDate", "Status", "Team", "VirtualTeam", "LeaveProfile");

    BeanWrapperFieldSetMapperCustom<UserImport> fieldSetMapper = new BeanWrapperFieldSetMapperCustom<>();
    fieldSetMapper.setTargetType(UserImport.class);

    defaultLineMapper.setLineTokenizer(lineTokenizer);
    defaultLineMapper.setFieldSetMapper(fieldSetMapper);

    return defaultLineMapper;
  }

}
