package edu.dental.beans;

import java.util.Objects;

public final class User {

    private final int id;
    private final String name;
    private final String email;
    private final String created;

    public User(int id, String name, String email, String created) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.created = created;
    }

    public User(edu.dental.domain.entities.User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.created = user.getCreated().toString();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCreated() {
        return created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(name, user.name) && Objects.equals(email, user.email) && Objects.equals(created, user.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, created);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", created='" + created + '\'' +
                '}';
    }
}
