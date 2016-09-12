package com.horeca.site.services;

public interface PinGeneratorService {

    static final int PIN_LENGTH = 6;

    String generatePin();
}
