package com.netradev.tout_est_africain.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "Products")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Product {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.IDENTITY,
            generator = "primary_sequence"
    )
    private Long id;

    @Column
    private String libelle;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column
    private Integer stock;


    // Nouveaux champs
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private String origin;

    @Column
    private String materials;

    @Column
    private String dimensions;

    @Column
    private Double weight;

    @Column
    private Boolean isHandmade = false;

    @Column
    private Boolean isOrganic = false;

    @Column
    private Boolean isFairTrade = false;

    @Column
    private Double averageRating;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductSpecification> specifications;

    // Existant
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private User seller;

    @ManyToMany(mappedBy = "products")
    private Set<Order> orders;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ImageProduct> images;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
