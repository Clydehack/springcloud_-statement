package com.uzone.settlement.framework.swagger;/**
 * @author
 */

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *@ClassName SwaggerProperties
 *@Description TODO
 *@Author 郗富琦
 *@Date 2018/12/31 22:45
 *Version 1.0
 **/
@ConfigurationProperties(
        prefix = "core.swagger"
)
@Data
@NoArgsConstructor
public class SwaggerProperties {
    private String basePackage = "com.uzone.settlement";
    private String title = "对账服务";
    private String contactName = "有容科技";
    private String contactUrl = "";
    private String contactEmail = "有容@zztabc.com";
    private String version = "0.0.1";
    private String description = "对账业务";
}
