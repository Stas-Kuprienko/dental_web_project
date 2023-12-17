package edu.dental.domain.entities.dto;

import edu.dental.domain.entities.User;

import java.util.Objects;

public final class UserDTO {

    private final int id;
    private final String name;
    private final String email;
    private final String created;

    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.created = user.getCreated().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return id == userDTO.id && Objects.equals(name, userDTO.name)
                && Objects.equals(email, userDTO.email) && Objects.equals(created, userDTO.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, created);
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", created='" + created + '\'' +
                '}';
    }
}
