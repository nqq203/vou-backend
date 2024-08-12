package com.vou.auth_service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LogoutRequest {
    Long idUser;

    public LogoutRequest() {

    }

    public LogoutRequest(@JsonProperty("idUser") Long idUser) {
        this.idUser = idUser;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }
}
