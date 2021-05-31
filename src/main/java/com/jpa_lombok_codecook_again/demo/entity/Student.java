package com.jpa_lombok_codecook_again.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

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

    private String email;

    private LocalDate birthDate;

    private long age;
}
