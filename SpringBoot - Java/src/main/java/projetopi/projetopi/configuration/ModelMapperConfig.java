package projetopi.projetopi.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        // Você pode configurar o ModelMapper aqui, se necessário.
        ModelMapper modelMapper = new ModelMapper();

        // Configuração opcional para ajuste de mapeamentos (caso precise):
        // modelMapper.getConfiguration().setFieldMatchingEnabled(true)
        //         .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        return modelMapper;
    }
}
