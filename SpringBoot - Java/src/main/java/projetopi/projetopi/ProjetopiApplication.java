package projetopi.projetopi;

import com.sun.tools.javac.Main;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.io.InputStream;
import java.util.Properties;

@Configuration
@SpringBootApplication
public class ProjetopiApplication {


	public static void main(String[] args) {
		SpringApplication.run(ProjetopiApplication.class, args);
	}


}
