package com.shipping.ShippingEstimatorApplication.repository;


import com.shipping.ShippingEstimatorApplication.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {

}