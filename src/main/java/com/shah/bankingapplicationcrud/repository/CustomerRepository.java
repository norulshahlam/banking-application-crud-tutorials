package com.shah.bankingapplicationcrud.repository;

import com.shah.bankingapplicationcrud.model.entity.Customer;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, UUID>,
        JpaSpecificationExecutor<Customer> {

    Optional<Customer> findById(UUID id);

    static Specification<Customer> firstNameLike(String name) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(
                root.get("firstName"), "%" + name + "%"));
    }

    static Specification<Customer> lastNameLike(String name) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(
                root.get("lastName"), "%" + name + "%"));
    }



}
