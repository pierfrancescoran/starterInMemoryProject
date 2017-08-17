package com.peter.main;

import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("com.peter.controller")
@ComponentScan("com.peter.config")
@ComponentScan
@PropertySource("classpath:/properties/system-${spring.profiles.active}.properties")
public class Application extends SpringBootServletInitializer {

    @Autowired
    private Environment env;

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
    
    @Bean
    EmbeddedServletContainerCustomizer containerCustomizer(
            @Value("") String keystoreFile) throws Exception {

        return (ConfigurableEmbeddedServletContainer container) -> {
            TomcatEmbeddedServletContainerFactory tomcat = (TomcatEmbeddedServletContainerFactory) container;
            tomcat.addConnectorCustomizers(
                    connector -> {
                        connector.setPort(Integer.parseInt(env.getProperty("application.port")));
                        connector.setSecure(true);
                        connector.setScheme("https");

                        Http11NioProtocol proto = (Http11NioProtocol) connector.getProtocolHandler();
                        proto.setSSLEnabled(true);
                        proto.setKeystoreFile(env.getProperty("application.ssl.filepath"));
                        proto.setKeystorePass(env.getProperty("application.ssl.keypass"));
                        proto.setKeystoreType(env.getProperty("application.ssl.keytype"));
                        proto.setKeyAlias(env.getProperty("application.ssl.keyalias"));
                    }
            );

        };
    }

}
