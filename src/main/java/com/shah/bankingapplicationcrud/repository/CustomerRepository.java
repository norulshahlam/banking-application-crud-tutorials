package com.shah.bankingapplicationcrud.repository;

import com.shah.bankingapplicationcrud.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, UUID> {

    Optional<Customer> findById(UUID id);
}
