package com.horeca.site.models.cubilis;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Audited
public class CubilisConnectionStatus {

    public enum Status {
        DISABLED("Integration with Cubilis has not been enabled"),
        SUCCESS("Connection with Cubilis has been successfuly established"),
        AUTHENTICATION_ERROR("The login and/or password is incorrect. Your login will be blocked after 5 failed attempts"),
        UNKNOWN_ERROR("An unknown error has occurred");

        private final String text;

        Status(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public CubilisConnectionStatusView toView() {
        return new CubilisConnectionStatusView(getStatus());
    }
}
