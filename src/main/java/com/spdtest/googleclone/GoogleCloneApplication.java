package com.spdtest.googleclone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.spdtest", "com.spdtest.googleclone"})
public class GoogleCloneApplication {

    static ApplicationContext context;

    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(GoogleCloneApplication.class);
        context = app.run(args);
    }
    // use in unit tests only
    public static void setContext(ApplicationContext context) {
        GoogleCloneApplication.context = context;
    }
}
