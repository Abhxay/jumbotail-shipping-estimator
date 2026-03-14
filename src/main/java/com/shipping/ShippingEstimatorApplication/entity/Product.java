package com.shipping.ShippingEstimatorApplication.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double price;

  //for calculation
    @Column(nullable = false)
    private Double weightKg;

    @Column(nullable = false)
    private Double dimensionLengthCm;

    @Column(nullable = false)
    private Double dimensionWidthCm;

    @Column(nullable = false)
    private Double dimensionHeightCm;

    private String category;

    private boolean available;


    @ManyToOne                          // Many products CAN belong to one seller
    @JoinColumn(
            name = "seller_id",
            nullable = false
    )
    private Seller seller;
}
