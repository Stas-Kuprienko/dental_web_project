package edu.dental.dto;

import edu.dental.entities.User;
import edu.dental.security.AuthenticationService;

import java.util.Objects;

public record UserDto(int id, String name, String email, String created, String jwt) {

    public static UserDto parse(User user) {
        String jwt = AuthenticationService.JwtUtils.generateJwtFromEntity(user);
        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getCreated().toString(), jwt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(name, userDto.name) && Objects.equals(email, userDto.email) && Objects.equals(jwt, userDto.jwt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, jwt);
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", jwt='" + jwt + '\'' +
                '}';
    }
}
