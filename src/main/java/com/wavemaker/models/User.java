package com.wavemaker.models;

import org.json.JSONObject;

import java.util.Objects;

public class User {
    private String emailId, password;

    public User(String emailId, String password) {
        this.emailId = emailId;
        this.password = password;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(getEmailId(), user.getEmailId()) && Objects.equals(getPassword(), user.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmailId(), getPassword());
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("emailId", emailId);
        jsonObject.put("password", password);
        return jsonObject.toString();
    }
}
