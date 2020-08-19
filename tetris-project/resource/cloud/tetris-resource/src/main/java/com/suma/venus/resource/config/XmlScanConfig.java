package com.suma.venus.resource.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource(locations={"classpath:ldap.xml"})
public class XmlScanConfig {

}
