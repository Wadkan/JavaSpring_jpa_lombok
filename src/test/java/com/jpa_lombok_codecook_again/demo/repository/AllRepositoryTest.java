package com.jpa_lombok_codecook_again.demo.repository;

import com.jpa_lombok_codecook_again.demo.entity.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.bind.DatatypeConverterInterface;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class AllRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void saveOneSimple() {
        Student john = Student.builder()
                .email("john@codecool.com")
                .name("John")
                .build();
        studentRepository.save(john);

        List<Student> studentList = studentRepository.findAll();
        assertTrue(studentList.size() == 1);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void saveUniqueFieldTwice() {
        Student student = Student.builder()
                .email("john@lcodecoo.com")
                .name("John")
                .build();

        studentRepository.save(student);

        Student student2 = Student.builder()
                .email("john@lcodecoo.com")
                .name("Peter")
                .build();

        studentRepository.saveAndFlush(student2);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void emailShouldBeNotNull() {
        Student student2 = Student.builder()
                .name("Peter")
                .build();

        studentRepository.saveAndFlush(student2);
    }

    @Test
    public void transientIsNotSaved() {
        Student student = Student.builder()
                .birthDate(LocalDate.of(1987, 02, 12))
                .email("john@lcodecoo.com")
                .name("Peter")
                .build();
        student.calculateAge();
        assertTrue(student.getAge() >= 31);

        studentRepository.save(student);
        entityManager.clear();

        List<Student> students = studentRepository.findAll();
        assertTrue(students.stream().allMatch(student1 -> student1.getAge() == 0L));
    }
}