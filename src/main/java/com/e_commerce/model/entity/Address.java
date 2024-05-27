package com.e_commerce.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "address_line_1")
    private String AddressLine1;

    @Column(name = "address_line_2")
    private String AddressLine2;

    @Column(nullable = false, name = "city")
    private String City;

    @Column(nullable = false, name = "country")
    private String Country;

    @Column(nullable = false, name = "postal_code")
    private String PostalCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}
