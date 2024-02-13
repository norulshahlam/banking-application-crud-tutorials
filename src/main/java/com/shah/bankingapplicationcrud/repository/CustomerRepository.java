package com.shah.bankingapplicationcrud.repository;

import com.shah.bankingapplicationcrud.model.entity.Customer;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author NORUL
 */
@Repository
public interface CustomerRepository extends PagingAndSortingRepository<Customer, UUID>,
        JpaSpecificationExecutor<Customer>, JpaRepository<Customer, UUID> {


    Optional<Customer> findByEmail(String email);


    static Specification<Customer> firstNameLike(String name) {
        return ((root, query, builder) -> builder.like(
                root.get("firstName"), "%" + name + "%"));
    }

    static Specification<Customer> lastNameLike(String name) {
        return ((root, query, builder) -> builder.like(
                root.get("lastName"), "%" + name + "%"));
    }
}
