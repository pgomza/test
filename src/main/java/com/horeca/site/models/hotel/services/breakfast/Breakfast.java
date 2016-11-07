package com.horeca.site.models.hotel.services.breakfast;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.horeca.site.models.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
public class Breakfast extends Translatable<BreakfastTranslation> implements Viewable<BreakfastView> {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @NotNull
    @Embedded
    private Price price;

    private Date fromHour;

    private Date toHour;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn
    private Set<BreakfastCategory> categories;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Date getFromHour() {
        return fromHour;
    }

    public void setFromHour(Date fromHour) {
        this.fromHour = fromHour;
    }

    public Date getToHour() {
        return toHour;
    }

    public void setToHour(Date toHour) {
        this.toHour = toHour;
    }

    public Set<BreakfastCategory> getCategories() {
        return categories;
    }

    public void setCategories(Set<BreakfastCategory> categories) {
        this.categories = categories;
    }

    @Override
    public BreakfastView toView(String preferredLanguage, String defaultLanguage) {
        BreakfastView view = new BreakfastView();
        view.setPrice(getPrice());
        view.setFromHour(getFromHour());
        view.setToHour(getToHour());
        view.setDescription(getTranslation(preferredLanguage, defaultLanguage).getDescription());

        Set<BreakfastCategoryView> categoryViews = new HashSet<>();
        for (BreakfastCategory category : categories) {
            categoryViews.add(category.toView(preferredLanguage, defaultLanguage));
        }
        view.setMenu(categoryViews);

        return view;
    }
}
