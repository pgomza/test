package com.horeca.site.models.hotel.services.breakfast;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.horeca.site.models.Translatable;
import com.horeca.site.models.Viewable;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class BreakfastGroup extends Translatable<BreakfastGroupTranslation>
        implements Viewable<BreakfastGroupView> {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "breakfastgroup_items")
    private Set<BreakfastItem> items;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<BreakfastItem> getItems() {
        return items;
    }

    public void setItems(Set<BreakfastItem> items) {
        this.items = items;
    }

    @Override
    public BreakfastGroupView toView(String preferredLanguage, String defaultLanguage) {
        BreakfastGroupView view = new BreakfastGroupView();
        view.setId(getId());
        view.setName(getTranslation(preferredLanguage, defaultLanguage).getName());

        Set<BreakfastItemView> itemViews = new HashSet<>();
        for (BreakfastItem item : items) {
            itemViews.add(item.toView(preferredLanguage, defaultLanguage));
        }
        view.setItems(itemViews);

        return view;
    }
}
