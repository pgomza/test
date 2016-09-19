package com.horeca.site.models;

public interface Viewable<T> {

    T toView(String preferredLanguage, String defaultLanguage);
}
