package com.horeca.site.models.hotel.services;

import com.horeca.site.models.hotel.translation.Translatable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@MappedSuperclass
public abstract class StandardServiceModel<T extends StandardServiceCategoryModel> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Translatable
    private String description;

    @Translatable
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    @OrderColumn(name = "category_order")
    private List<T> categories = new ArrayList<>();

    protected StandardServiceModel() {}

    public StandardServiceModel(String description, List<T> categories) {
        this.description = description;
        this.categories = categories;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<T> getCategories() {
        return categories;
    }

    public void setCategories(List<T> categories) {
        this.categories = categories;
    }
}
