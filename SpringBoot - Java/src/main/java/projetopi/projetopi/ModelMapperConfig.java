package projetopi.projetopi;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.context.annotation.Bean;


import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.context.annotation.Bean;


@org.springframework.context.annotation.Configuration
public class ModelMapperConfig {

        @Bean
        public ModelMapper modelMapper(){

            var modelMapper = new ModelMapper();

            modelMapper.getConfiguration()
                    .setFieldMatchingEnabled(true)
                    .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);

            return modelMapper;
        }
}

