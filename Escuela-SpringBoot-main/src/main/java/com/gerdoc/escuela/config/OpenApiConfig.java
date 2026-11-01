package com.gerdoc.escuela.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig
{
    @Bean
    public OpenAPI escuelaOpenAPI()
    {
        return new OpenAPI( )
                .info( new Info( )
                        .title("API REST - Sistema Escolar")
                        .version("1.0.0")
                        .description("Documentación de los servicios REST para la administración de alumnos, profesores y materias.")
                        .contact( new Contact( ).name("Equipo de Desarrollo") )
                );

    }
}
