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
    
	public String getBasePackage() {
		return basePackage;
	}
	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getContactUrl() {
		return contactUrl;
	}
	public void setContactUrl(String contactUrl) {
		this.contactUrl = contactUrl;
	}
	public String getContactEmail() {
		return contactEmail;
	}
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
