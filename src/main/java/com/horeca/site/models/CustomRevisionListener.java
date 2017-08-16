package com.horeca.site.models;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


public class CustomRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        CustomRevisionEntity revision = (CustomRevisionEntity) revisionEntity;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            revision.setUsername("INTERNAL");
        }
        else if (authentication instanceof AnonymousAuthenticationToken) {
            revision.setUsername("ANONYMOUS");
        }
        else {
            revision.setUsername(authentication.getName());
        }
    }
}
