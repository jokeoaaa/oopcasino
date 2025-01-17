package org.example.groupassignment;

import org.springframework.boot.SpringApplication;

public class TestGroupassignmentApplication {

    public static void main(String[] args) {
        SpringApplication.from(GroupassignmentApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
