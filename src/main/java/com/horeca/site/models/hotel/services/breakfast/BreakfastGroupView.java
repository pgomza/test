package com.horeca.site.models.hotel.services.breakfast;

import java.util.Set;

public class BreakfastGroupView {

    private Long id;

    private String name;

//    @JsonSerialize(using = CustomBreakfastGroupViewSerializer.class)
    private Set<BreakfastItemView> items;

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

    public Set<BreakfastItemView> getItems() {
        return items;
    }

    public void setItems(Set<BreakfastItemView> items) {
        this.items = items;
    }
}
