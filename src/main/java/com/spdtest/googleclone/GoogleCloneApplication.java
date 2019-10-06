package com.spdtest.googleclone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class GoogleCloneApplication {

    static ApplicationContext context;

    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(GoogleCloneApplication.class);
        context = app.run(args);
    }

    public static void setContext(ApplicationContext context) {
        GoogleCloneApplication.context = context;
    }
}
