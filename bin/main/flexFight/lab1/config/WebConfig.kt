package flexFight.lab1.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebMvc
class WebConfig : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry
            .addMapping("/**")
            .allowedOriginPatterns("http://localhost:5173")
            .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true)
    }
}