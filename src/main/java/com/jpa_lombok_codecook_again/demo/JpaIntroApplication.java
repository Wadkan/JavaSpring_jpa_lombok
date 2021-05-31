package com.jpa_lombok_codecook_again.demo;

import com.jpa_lombok_codecook_again.demo.entity.Address;
import com.jpa_lombok_codecook_again.demo.entity.Location;
import com.jpa_lombok_codecook_again.demo.entity.School;
import com.jpa_lombok_codecook_again.demo.entity.Student;
import com.jpa_lombok_codecook_again.demo.repository.AddressRepository;
import com.jpa_lombok_codecook_again.demo.repository.SchoolRepository;
import com.jpa_lombok_codecook_again.demo.repository.StudentRepository;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.util.Arrays;

@SpringBootApplication
public class JpaIntroApplication {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    public static void main(String[] args) {
        SpringApplication.run(JpaIntroApplication.class, args);
    }

    @Bean
    @Profile("production")
    public CommandLineRunner init() {
        return args -> {
            Address address = Address.builder()
                    .address("afa lkas lk ")
                    .city("Budapest")
                    .country("Hungary")
                    .build();
            Address address2 = Address.builder()
                    .address("afa aasd lkas lk ")
                    .city("Budapest")
                    .country("Hungary")
                    .build();

            Student john = Student.builder()
                    .name("John")
                    .email("john@codecool.com")
                    .birthDate(LocalDate.of(1987, 2, 1))
                    .address(address)
                    .phoneNumbers(Arrays.asList("555-5432", "555-1234"))
                    .build();
            Student barbara = Student.builder()
                    .name("Barbara")
                    .email("barbara@codecool.com")
                    .birthDate(LocalDate.of(1985, 3, 2))
                    .address(address2)
                    .phoneNumbers(Arrays.asList("123-4567", "987-6543"))
                    .build();

            School school = School.builder()
                    .location(Location.BUDAPEST)
                    .name("CodeCool Budapest")
                    .student(john)
                    .student(barbara)
                    .build();

            barbara.setSchool(school);
            john.setSchool(school);

            schoolRepository.save(school);


//            Student wadkan = Student.builder()
//                    .email("szavasz@tavasz.hu")
//                    .name("Wadkan")
//                    .birthDate(LocalDate.of(1980, 2, 4))
//                    .address(Address.builder().city("Mickolc").country("Hungary").build())
//                    .phoneNumber("555-6666")
//                    .phoneNumber("555-7777")
//                    .phoneNumber("555-8888")
//                    .build();
//            studentRepository.save(wadkan);
        };
    }
}
