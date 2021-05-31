package com.jpa_lombok_codecook_again.demo.repository;

import com.jpa_lombok_codecook_again.demo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
