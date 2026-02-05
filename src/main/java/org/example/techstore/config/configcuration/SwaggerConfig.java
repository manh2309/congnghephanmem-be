package org.example.techstore.config.configcuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SwaggerConfig implements WebMvcConfigurer {
    private static final String SECURITY_SCHEME_NAME = "BearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Technology Store API")
                        .version("1.0")
                        .description("API quản lý Brand, Product, Order..."))
                // Cấu hình security global
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                );
    }

    // OperationCustomizer để thêm x-order (GET → POST → PATCH → PUT → DELETE)
    @Bean
    public OperationCustomizer operationOrderCustomizer() {
        return (operation, handlerMethod) -> {
            String httpMethod = "GET"; // default
            if (handlerMethod.getMethodAnnotation(org.springframework.web.bind.annotation.GetMapping.class) != null) httpMethod = "GET";
            else if (handlerMethod.getMethodAnnotation(org.springframework.web.bind.annotation.PostMapping.class) != null) httpMethod = "POST";
            else if (handlerMethod.getMethodAnnotation(org.springframework.web.bind.annotation.PatchMapping.class) != null) httpMethod = "PATCH";
            else if (handlerMethod.getMethodAnnotation(org.springframework.web.bind.annotation.PutMapping.class) != null) httpMethod = "PUT";
            else if (handlerMethod.getMethodAnnotation(org.springframework.web.bind.annotation.DeleteMapping.class) != null) httpMethod = "DELETE";

            int order = switch (httpMethod) {
                case "GET" -> 1;
                case "POST" -> 2;
                case "PATCH" -> 3;
                case "PUT" -> 4;
                case "DELETE" -> 5;
                default -> 6;
            };
            operation.addExtension("x-order", order);
            return operation;
        };
    }

    @Bean
    public GroupedOpenApi allApi() {
        return GroupedOpenApi.builder()
                .group("All APIs")
                .pathsToMatch("/api/**")
                .build();
    }

    // Forward trực tiếp /swagger-api/docs → /swagger-api/docs/index.html
    // Forward /swagger-api/docs → /swagger-api/docs/index.html
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/swagger-api/docs")
                .setViewName("forward:/swagger-api/docs/index.html");
    }
}
