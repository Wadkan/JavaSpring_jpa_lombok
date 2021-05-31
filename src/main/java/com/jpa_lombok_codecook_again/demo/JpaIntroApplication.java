package com.jpa_lombok_codecook_again.demo;

import com.jpa_lombok_codecook_again.demo.entity.Student;
import com.jpa_lombok_codecook_again.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;

@SpringBootApplication
public class JpaIntroApplication {

    @Autowired
    private StudentRepository studentRepository;

    public static void main(String[] args) {
        SpringApplication.run(JpaIntroApplication.class, args);
    }

    @Bean
    @Profile("production")
    public CommandLineRunner init() {
        return args -> {
            Student wadkan = Student.builder()
                    .email("szavasz@tavasz.hu")
                    .name("Wadkan")
                    .birthDate(LocalDate.of(1980, 2, 4))
                    .build();
            studentRepository.save(wadkan);
        };
    }
}
