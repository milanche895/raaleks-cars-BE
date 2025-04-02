package com.project.brka_media.DTO;

import com.project.brka_media.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.util.Date;

public class UserDTO {

    private String id;
    private String name;
    private String username;
    private String password;
    private String role;

    public UserDTO() {

    }

    public UserDTO(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.name = userEntity.getName();
        this.username = userEntity.getUsername();
        this.password = userEntity.getPassword();
        this.role = userEntity.getRole();

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
