package com.horeca.site.models.hotel.services.roomservice;

import com.horeca.site.models.Translatable;
import com.horeca.site.models.Viewable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class RoomItem extends Translatable<RoomItemTranslation> implements Viewable<RoomItemView> {

    @Id
    @GeneratedValue
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public RoomItemView toView(String preferredLanguage, String defaultLanguage) {
        RoomItemTranslation translation = getTranslation(preferredLanguage, defaultLanguage);
        RoomItemView view = new RoomItemView();
        view.setId(getId());
        view.setText(translation.getText());

        return view;
    }
}
