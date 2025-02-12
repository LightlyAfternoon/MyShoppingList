package ru.rsue.Karnaukhova.entity;

import java.util.UUID;

public class User {
    UUID mUuid;
    String mLogin;
    String mPassword;
    String mNickname;

    public User(UUID uuid) { mUuid = uuid; }

    public UUID getUuid() { return mUuid; }

    public String getLogin() { return mLogin; }
    public void setLogin(String login) { mLogin = login; }

    public String getPassword() { return mPassword; }
    public void setPassword(String password) { mPassword = password; }

    public String getNickname() { return mNickname; }
    public void setNickname(String nickname) { mNickname = nickname; }
}