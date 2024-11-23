package projetopi.projetopi.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API NaRégua",
                version = "1.0",
                description = "API Description"
        )
)
public class SwaggerConfig {
}
