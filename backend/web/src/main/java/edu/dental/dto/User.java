package edu.dental.dto;

import java.util.Objects;

public record User(int id, String name, String email, String created) {

    public static User parse(edu.dental.entities.User user) {
        return new User(user.getId(), user.getName(), user.getEmail(), String.valueOf(user.getCreated()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(name, user.name)
                && Objects.equals(email, user.email) && Objects.equals(created, user.created);
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
