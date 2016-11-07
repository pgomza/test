package com.horeca.site.models.hotel.services.breakfast;

import java.util.Set;

public class BreakfastCategoryView {

    private Long id;

    private BreakfastCategory.Category name;

    private Set<BreakfastItemView> items;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BreakfastCategory.Category getName() {
        return name;
    }

    public void setName(BreakfastCategory.Category name) {
        this.name = name;
    }

    public Set<BreakfastItemView> getItems() {
        return items;
    }

    public void setItems(Set<BreakfastItemView> items) {
        this.items = items;
    }
}
