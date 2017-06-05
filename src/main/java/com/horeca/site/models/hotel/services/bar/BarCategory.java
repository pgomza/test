package com.horeca.site.models.hotel.services.bar;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(indexes = @Index(name = "bar_id", columnList = "bar_id"))
public class BarCategory {

    public enum Category {
        @JsonProperty("DRINK")
        DRINK,
        @JsonProperty("SNACK")
        SNACK
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Category category;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "bar_category_id")
    private Set<BarItem> items;

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

    public Set<BarItem> getItems() {
        return items;
    }

    public void setItems(Set<BarItem> items) {
        this.items = items;
    }
}
