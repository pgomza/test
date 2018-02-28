package com.horeca.site.models.hotel.services;

import com.horeca.site.models.hotel.translation.Translatable;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@MappedSuperclass
public abstract class StandardServiceCategoryModel<T> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Translatable
    @NotEmpty
    private String name;

    @Translatable
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "category_id")
    @OrderColumn(name = "item_order")
    private List<T> items = new ArrayList<>();

    protected StandardServiceCategoryModel() {}

    public StandardServiceCategoryModel(String name, List<T> items) {
        this.name = name;
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
