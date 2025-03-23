package com.amigoscode.customer;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository
        extends JpaRepository<Customer, Integer> {

    boolean existsCustomerByEmail(String email);
    boolean existsCustomerById(Integer id);
    Optional<Customer> findCustomerByEmail(String email);

    @Modifying
    @Query("UPDATE Customer c SET c.profileImageId = ?1 WHERE c.id = ?2")

    int updateProfileImageId(String profileImageId, Integer customerId);
}
