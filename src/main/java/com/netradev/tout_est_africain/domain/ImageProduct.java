package com.netradev.tout_est_africain.domain;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "ImageProducts")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class ImageProduct {

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

//    @Column
//    private Long productId;

    // Relation avec le produit au lieu d'un simple ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column
    private String image;

    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary = false;

    @Column
    private String alt;

    // Ordre d'affichage des images
    @Column
    private Integer displayOrder;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
