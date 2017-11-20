package com.horeca.site.models.hotel.translation;

import java.util.List;

public class HotelTranslationView {
    private LanguageCode languageCode;
    private List<TranslationEntry> usedEntries;
    private List<TranslationEntry> unusedEntries;

    HotelTranslationView() {
    }

    public HotelTranslationView(LanguageCode languageCode, List<TranslationEntry> usedEntries, List<TranslationEntry> unusedEntries) {
        this.languageCode = languageCode;
        this.usedEntries = usedEntries;
        this.unusedEntries = unusedEntries;
    }

    public LanguageCode getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(LanguageCode languageCode) {
        this.languageCode = languageCode;
    }

    public List<TranslationEntry> getUsedEntries() {
        return usedEntries;
    }

    public void setUsedEntries(List<TranslationEntry> usedEntries) {
        this.usedEntries = usedEntries;
    }

    public List<TranslationEntry> getUnusedEntries() {
        return unusedEntries;
    }

    public void setUnusedEntries(List<TranslationEntry> unusedEntries) {
        this.unusedEntries = unusedEntries;
    }
}
