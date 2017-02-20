package com.horeca.site.models.hotel.services.breakfast;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(indexes = @Index(name = "breakfast_id", columnList = "breakfast_id"))
public class BreakfastCategory {

    public enum Category { DISH, DRINK }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Category category;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "breakfast_category_id")
    private Set<BreakfastItem> items;

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

    public Set<BreakfastItem> getItems() {
        return items;
    }

    public void setItems(Set<BreakfastItem> items) {
        this.items = items;
    }
}
