package com.jpa_lombok_codecook_again.demo.repository;

import com.jpa_lombok_codecook_again.demo.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
