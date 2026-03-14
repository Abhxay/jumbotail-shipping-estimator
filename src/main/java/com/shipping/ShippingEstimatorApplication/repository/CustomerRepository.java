package com.shipping.ShippingEstimatorApplication.repository;

import com.shipping.ShippingEstimatorApplication.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // findById() from JpaRepository is all we need here
}