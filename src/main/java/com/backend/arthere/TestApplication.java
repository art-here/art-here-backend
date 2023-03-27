package com.backend.arthere;

import com.backend.arthere.global.ExcludeComponent;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = ExcludeComponent.class))
public class TestApplication {

    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application-test.yml,"
            + "/home/ec2-user/app/application-server.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(TestApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }
}
