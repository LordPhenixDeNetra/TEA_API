package com.netradev.tout_est_africain.domain;

import com.netradev.tout_est_africain.model.AccountStatus;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "Users")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class User {

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

    @Column(nullable = false, unique = true)
    private String uuid;

    @Column
    private String firstname;

    @Column
    private String lastname;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String telephone;

    @ManyToMany
    @JoinTable(
            name = "TeaUserRoles",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId")
    )
    private Set<Role> roles;

    @OneToMany(mappedBy = "buyer")
    private Set<Order> orders;

    @OneToMany(mappedBy = "seller")
    private Set<Product> products;


    // Nouveaux champs pour le système de réputation
    @Column(nullable = false)
    private boolean active = true;

    @Column
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus = AccountStatus.DEACTIVATED;

    @Column(nullable = false)
//    private Double sellerRating;
    private Double sellerRating = 0.0;

    @Column(nullable = false)
//    private Double buyerRating;
    private Double buyerRating = 0.0;

    @Column(nullable = false)
    private Double shipperRating = 0.0;
//    private Double shipperRating;

    @Column(nullable = false)
//    private Integer successfulSales;
    private Integer successfulSales = 0;

    @Column(nullable = false)
//    private Integer successfulPurchases;
    private Integer successfulPurchases = 0;

    @Column(nullable = false)
//    private Integer successfulDeliveries;
        private Integer successfulDeliveries = 0;

    // Nouvelles relations
    @OneToMany(mappedBy = "user")
    private Set<UserBadge> badges;

    @OneToMany(mappedBy = "targetUser")
    private Set<Review> receivedReviews;

    @OneToMany(mappedBy = "reviewer")
    private Set<Review> givenReviews;


    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
