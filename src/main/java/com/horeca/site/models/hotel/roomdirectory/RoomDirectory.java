package com.horeca.site.models.hotel.roomdirectory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.horeca.site.models.Viewable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
public class RoomDirectory implements Viewable<RoomDirectoryView> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn
    private Set<Section> sections;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Section> getSections() {
        return sections;
    }

    public void setSections(Set<Section> sections) {
        this.sections = sections;
    }

    @Override
    public RoomDirectoryView toView(String preferredLanguage, String defaultLanguage) {
        Set<Section> sections = getSections();
        RoomDirectoryView roomDirectoryView = new RoomDirectoryView();

        List<SectionView> sectionViews = new ArrayList<>();
        for (Section section : sections) {
            sectionViews.add(section.toView(preferredLanguage, defaultLanguage));
        }

        roomDirectoryView.setSections(sectionViews);
        return roomDirectoryView;
    }
}