package com.jpa_lombok_codecook_again.demo.repository;

import com.jpa_lombok_codecook_again.demo.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("UPDATE Address a set a.country = 'USA' WHERE a.id in " +
    "(SELECT s.address.id FROM Student s WHERE s.name LIKE :name)")
    @Modifying(clearAutomatically = true)
    int updateAllToUSAByStudentName(@Param("name") String name);
}
