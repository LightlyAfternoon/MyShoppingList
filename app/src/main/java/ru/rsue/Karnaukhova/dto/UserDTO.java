package ru.rsue.Karnaukhova.dto;

import java.util.UUID;

public class UserDTO {
    UUID uuid;
    String login;
    String password;
    String nickname;

    public UserDTO() {}

    public UserDTO(UUID uuid) { this.uuid = uuid; }

    public UUID getUuid() { return uuid; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
}