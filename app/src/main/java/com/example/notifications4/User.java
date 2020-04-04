package com.example.notifications4;

public class User {

        public String email;
        public String token;
        public String name;
        public float desiredTemp;

    public User() {

    }

    public User(String email, String token) {
            this.email = email;
            this.token = token;
            this.name = null;
            this.desiredTemp = (float) 100.00;
        }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDesiredTemp() {
        return desiredTemp;
    }

    public void setDesiredTemp(float desiredTemp) {
        this.desiredTemp = desiredTemp;
    }
}
