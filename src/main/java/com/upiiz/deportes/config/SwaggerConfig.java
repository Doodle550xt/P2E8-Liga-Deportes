package com.upiiz.deportes.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(title = "API REST de Gestión de Ligas Deportivas",

        description = "API REST para la administración de entidades deportivas (Ligas, Equipos, Jugadores y Entrenadores). La API implementa autenticación y autorización mediante JWT (JSON Web Tokens) para proteger los endpoints de escritura.",

        version = "1.0.0", contact = @Contact(name = "Cristian García Nieves", url = "ninguna.com", email = "crisgnh01@gmail.com"), license = @License(name = "MINT", url = "ninguna.com/licencia"), termsOfService = "ninguna.com/terminos"), servers = {
                @Server(url = "http://localhost:8080/", description = "Servidor de pruebas"),
                @Server(url = "https://liga-deportes-mpg3.onrender.com", description = "Servidor de producción")
        },

        security = @SecurityRequirement(name = "BearerAuth"))

public class SwaggerConfig {
}