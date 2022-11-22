package de.whs.dbi.wise2223.praktikum.p3;

public class DatabaseAccessDataBuilder {
    private String url;
    private String username;
    private String password;

    public DatabaseAccessDataBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    public DatabaseAccessDataBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public DatabaseAccessDataBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public DatabaseAccessData createDatabaseAccessData() {
        return new DatabaseAccessData(url, username, password);
    }
}