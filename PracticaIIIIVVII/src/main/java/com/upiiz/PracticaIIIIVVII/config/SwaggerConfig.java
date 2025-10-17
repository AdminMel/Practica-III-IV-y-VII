package com.upiiz.PracticaIIIIVVII.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    info=@Info(
        title="Documentación de la API de Logística",
        description="Nos permite gestionar la información del sistema de logística de las rutas",
        // Versión - Funcionalodades menores - Parches
        version="1.0.1",
        contact = @Contact(
            name="Melanie Aileen Roman Espitia",
            url="www.logistica.com",
            email="mromane1900@alumno.ipn.mx"
        ),
        license=@License(
            name="MINT",
            url="ninguna.com/licencia"
        ),
        termsOfService="terminos.com/terminos"
    ),
    servers = {
            @Server(url="http://localhost:8080/", description = "Servidor de prueba"),
            @Server(url="https://practica-iii-iv-y-vii.onrender.com/swagger-ui/index.html", description = "Servidor en producción")
    }
)
public class SwaggerConfig {
}
