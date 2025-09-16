package bo.com.edu.diplomado.tuSaliud.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("**") // Permitir todos los orígenes
                .allowedMethods("**") // Permitir todos los métodos HTTP
                .allowedHeaders("**"); // Permitir todos los encabezados
    }
}
