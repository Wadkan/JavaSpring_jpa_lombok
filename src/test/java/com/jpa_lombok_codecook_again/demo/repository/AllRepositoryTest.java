package com.jpa_lombok_codecook_again.demo.repository;

import com.jpa_lombok_codecook_again.demo.entity.Address;
import com.jpa_lombok_codecook_again.demo.entity.Location;
import com.jpa_lombok_codecook_again.demo.entity.School;
import com.jpa_lombok_codecook_again.demo.entity.Student;
import net.bytebuddy.asm.Advice;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class AllRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Test
    public void saveOneSimple() {
        Student john = Student.builder()
                .email("john@codecool.com")
                .name("John")
                .build();
        studentRepository.save(john);

        List<Student> studentList = studentRepository.findAll();
        assertEquals(1, studentList.size());
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
                .birthDate(LocalDate.of(1987, 2, 12))
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

    @Test
    public void addressIsPersistedWithStudent() {
        Address address = Address.builder()
                .country("Hungary")
                .city("Budapest")
                .address("Nagymező street 44")
                .zipCode(1065)
                .build();

        Student student = Student.builder()
                .email("temp@codecool.com")
                .address(address)
                .build();

        studentRepository.save(student);

        List<Address> addresses = addressRepository.findAll();

        assertTrue(addresses.size() == 1 &&
                addresses.stream().allMatch(address1 -> address1.getId() > 0L)
        );
    }

    @Test
    public void studentsArePersistedAndDeletedWithNewSchool() {
        Set<Student> students = IntStream.range(1, 10)
                .boxed()
                .map(integer -> Student.builder().email("student" + integer + "@codecool.com").build())
                .collect(Collectors.toSet());
        School school = School.builder()
                .students(students)
                .location(Location.BUDAPEST)
                .build();

        schoolRepository.save(school);

        assertTrue(studentRepository.findAll().size() == 9 &&
                studentRepository.findAll().stream().anyMatch(student -> student.getEmail().equals("student9@codecool.com")));

        schoolRepository.deleteAll();

        assertEquals(0, studentRepository.findAll().size());
    }

    @Test
    public void findByNameStartingWithOrBirthDateBetween() {
        Student john = Student.builder()
                .email("john@codecool.com")
                .name("John")
                .build();

        Student jane = Student.builder()
                .email("jane@codecool.com")
                .name("Jane")
                .build();

        Student martha = Student.builder()
                .email("matha@codecool.com")
                .name("Matha")
                .build();

        Student peter = Student.builder()
                .email("peter@codecool.com")
                .birthDate(LocalDate.of(2010, 10, 3))
                .build();

        Student steve = Student.builder()
                .email("steve@codecool.com")
                .birthDate(LocalDate.of(2011, 12, 5))
                .build();

        studentRepository.saveAll(Lists.newArrayList(john, jane, martha, peter, steve));

        List<Student> filteredStudents = studentRepository.findByNameStartingWithOrBirthDateBetween("J",
                LocalDate.of(2009, 1, 1),
                LocalDate.of(2011, 1, 1));

        assertTrue(
                filteredStudents.contains(john)
                        && filteredStudents.contains(jane)
                        && filteredStudents.contains(peter)
        );
    }

    @Test
    public void findAllCountry() {
        Student first = Student.builder()
                .email("first@codecool.com")
                .address(Address.builder().country("Hungary").build())
                .build();
        Student second = Student.builder()
                .email("second@codecool.com")
                .address(Address.builder().country("Poland").build())
                .build();
        Student third = Student.builder()
                .email("third@codecool.com")
                .address(Address.builder().country("Poland").build())
                .build();
        Student forth = Student.builder()
                .email("forth@codecool.com")
                .address(Address.builder().country("Hungary").build())
                .build();

        studentRepository.saveAll(Lists.newArrayList(first, second, third, forth));

        List<String> allCountry = studentRepository.findAllCountry();

        assertEquals(allCountry.size(), 2);
        assertTrue(allCountry.contains("Poland"));
        assertTrue(allCountry.contains("Hungary"));
    }
}