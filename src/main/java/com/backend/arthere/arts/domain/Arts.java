package com.backend.arthere.arts.domain;

import com.backend.arthere.global.domain.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor
@Table(indexes = @Index(name = "idx_revision_date", columnList = "revision_date, id"))
public class Arts extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "art_name")
    private String artName;

    @NotNull
    @Column(name = "image_url")
    private String imageURL;

    @Embedded
    private Location location;

    @Embedded
    private Address address;

    @NotNull
    @Column(length = 50)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Builder
    public Arts(String artName, String imageURL, Location location, Address address, Category category) {
        this.artName = artName;
        this.imageURL = imageURL;
        this.location = location;
        this.address = address;
        this.category = category;
    }

    public void update(final Arts updateArts) {
        updateArtName(updateArts.getArtName());
        updateImageURL(updateArts.getImageURL());
        updateLocation(updateArts.getLocation());
        updateAddress(updateArts.getAddress());
        updateCategory(updateArts.getCategory());
    }

    public void updateArtName(final String artName) {
        this.artName = artName;
    }

    public void updateImageURL(final String imageURL) {
        this.imageURL = imageURL;
    }

    public void updateLocation(final Location location) {
        this.location = location;
    }

    public void updateAddress(final Address address) {
        this.address = address;
    }

    public void updateCategory(final Category category) {
        this.category = category;
    }
}
