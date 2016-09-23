package com.horeca.site.models.hotel.services.breakfast;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.horeca.site.models.Viewable;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class BreakfastCategory implements Viewable<BreakfastCategoryView> {

    public enum Category { DISH, DRINK }

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private Category category;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "category_items")
    private Set<BreakfastItem> items;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "category_groups")
    private Set<BreakfastGroup> groups;

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

    public Set<BreakfastGroup> getGroups() {
        return groups;
    }

    public void setGroups(Set<BreakfastGroup> groups) {
        this.groups = groups;
    }

    @Override
    public BreakfastCategoryView toView(String preferredLanguage, String defaultLanguage) {
        BreakfastCategoryView view = new BreakfastCategoryView();
        view.setId(getId());
        view.setCategory(getCategory());

        Set<BreakfastItemView> itemViews = new HashSet<>();
        for (BreakfastItem item : items) {
            itemViews.add(item.toView(preferredLanguage, defaultLanguage));
        }
        view.setItems(itemViews);

        Set<BreakfastGroupView> groupViews = new HashSet<>();
        for (BreakfastGroup group : groups) {
            groupViews.add(group.toView(preferredLanguage, defaultLanguage));
        }
        view.setGroups(groupViews);

        return view;
    }
}
