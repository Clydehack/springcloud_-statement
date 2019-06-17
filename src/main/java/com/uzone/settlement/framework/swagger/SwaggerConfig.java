package com.uzone.settlement.framework.swagger;/**
 * @author
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *@ClassName SwaggerConfig
 *@Description TODO
 *@Author 郗富琦
 *@Date 2018/12/31 22:43
 *Version 1.0
 **/
@Configuration
@ConditionalOnProperty(
        name = {"core.swagger.enabled"},
        matchIfMissing = true
)
@EnableSwagger2
@EnableConfigurationProperties({SwaggerProperties.class})
public class SwaggerConfig {
    @Autowired
    private SwaggerProperties swaggerProperties;

    public SwaggerConfig() {
    }
    @Value("${uzone.swagger.show}")
    private boolean swaggerShow;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(swaggerShow)
                .apiInfo(this.apiInfo())
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage(this
                                .swaggerProperties
                                .getBasePackage()))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(this.swaggerProperties.getTitle())
                .contact(new Contact(this
                        .swaggerProperties.getContactName(),
                        this.swaggerProperties.getContactUrl(),
                        this.swaggerProperties.getContactEmail()))
                .version(this.swaggerProperties.getVersion())
                .description(this.swaggerProperties.getDescription())
                .build();
    }
}
