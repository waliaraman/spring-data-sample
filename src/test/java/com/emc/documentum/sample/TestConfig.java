package com.emc.documentum.sample;

//import com.emc.documentum.sample.controller.ContactController;
import com.emc.documentum.springdata.repository.config.EnableDctmRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

/**
 * Basic configuration class for running Spring Data repository test WITHOUT loading main MVC Application
 */
@Configuration
@ComponentScan(basePackages = {"com.emc.documentum.springdata", "com.emc.documentum.sample"}, excludeFilters={
		@ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE, value=Application.class)
		})
@EnableDctmRepositories
@PropertySource("classpath:application.properties")
public class TestConfig {
	
	@Autowired
    private Environment environment;

	/**
	 * Repository name bean
	 *
	 * @return repository name
	 */
    @Bean
    @Qualifier("repositoryName")
    public String getRepositoryName() {
    	return environment.getProperty("repository.name");
    }

	/**
	 * Repository username bean
	 *
	 * @return repository username
	 */
    @Bean
	@Qualifier("repositoryUsername")
	public String getRepositoryUsername() {
    	return environment.getProperty("repository.username");
    }

	/**
	 * Repository password bean
	 *
	 * @return repository password
	 */
	@Bean
	@Qualifier("repositoryPassword")
	public String getRepositoryPassword() {
		return environment.getProperty("repository.password");
	}
}
