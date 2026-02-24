package full_k8s_project.todo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class TodoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoApplication.class, args);
	}

}

@Component
class ApplicationStartupListener {
	private static final Logger logger = LoggerFactory.getLogger(ApplicationStartupListener.class);

	private final Environment environment;

	public ApplicationStartupListener(Environment environment) {
		this.environment = environment;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void onApplicationReady() {
		logger.info("=".repeat(80));
		logger.info("Todo Application Started Successfully!");
		logger.info("Database URL: {}", environment.getProperty("spring.datasource.url"));
		logger.info("Database Username: {}", environment.getProperty("spring.datasource.username"));
		logger.info("Application Profile(s): {}", String.join(",", environment.getActiveProfiles()));
		logger.info("Server Port: {}", environment.getProperty("server.port"));
		logger.info("=".repeat(80));
	}
}

