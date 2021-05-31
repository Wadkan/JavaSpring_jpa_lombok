package com.jpa_lombok_codecook_again.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Student {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private LocalDate birthDate;

    @Transient
    private long age;

    public void calculateAge() {
        if (birthDate != null) {
            age = ChronoUnit.YEARS.between(birthDate, LocalDate.now());
        }
    }
}
