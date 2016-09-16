package com.horeca.site.services;

import com.horeca.site.models.hotel.roomdirectory.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class RoomDirectoryService {

    public RoomDirectoryView getView(RoomDirectory roomDirectory, String preferredLanguage, String defaultLanguage) {
        Set<Section> sections = roomDirectory.getSections();
        RoomDirectoryView roomDirectoryView = new RoomDirectoryView();

        List<SectionView> sectionViews = new ArrayList<>();
        for (Section section : sections) {
            SectionTranslation sectionTranslation = section.getTranslation(preferredLanguage, defaultLanguage);
            SectionView sectionView = new SectionView();
            sectionView.setHeading(sectionTranslation.getHeading());
            sectionView.setText(sectionTranslation.getText());
            sectionViews.add(sectionView);
        }

        roomDirectoryView.setSections(sectionViews);
        return roomDirectoryView;
    }
}
