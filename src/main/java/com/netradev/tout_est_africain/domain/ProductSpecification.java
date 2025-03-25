package com.netradev.tout_est_africain.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "ProductSpecifications")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class ProductSpecification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String value;
}
