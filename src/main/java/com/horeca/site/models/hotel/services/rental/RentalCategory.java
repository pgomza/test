package com.horeca.site.models.hotel.services.rental;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(indexes = @Index(name = "rental_id", columnList = "rental_id"))
@Audited
public class RentalCategory {

    public enum Category { MEN, WOMEN, CHILDREN }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Category category;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "rental_category_id")
    private Set<RentalItem> items;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<RentalItem> getItems() {
        return items;
    }

    public void setItems(Set<RentalItem> items) {
        this.items = items;
    }
}
