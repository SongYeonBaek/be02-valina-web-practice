package com.example.vanliaweb.product.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer idx;

    String location;

    @ManyToOne
    @JoinColumn(name = "Product_idx")
    Product product;
}
