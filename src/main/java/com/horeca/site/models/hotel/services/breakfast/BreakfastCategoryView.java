package com.horeca.site.models.hotel.services.breakfast;

import java.util.Set;

public class BreakfastCategoryView {

    private Long id;

    private BreakfastCategory.Category category;

    private Set<BreakfastItemView> items;

    private Set<BreakfastGroupView> groups;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BreakfastCategory.Category getCategory() {
        return category;
    }

    public void setCategory(BreakfastCategory.Category category) {
        this.category = category;
    }

    public Set<BreakfastItemView> getItems() {
        return items;
    }

    public void setItems(Set<BreakfastItemView> items) {
        this.items = items;
    }

    public Set<BreakfastGroupView> getGroups() {
        return groups;
    }

    public void setGroups(Set<BreakfastGroupView> groups) {
        this.groups = groups;
    }
}
