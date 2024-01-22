package edu.dental.dto;

import edu.dental.entities.User;
import edu.dental.security.AuthenticationService;

import java.util.Objects;

public class UserDto {

    private int id;
    private String name;
    private String email;
    private String created;
    private String jwt;

    private static final AuthenticationService.TokenUtils tokenUtils;

    static {
        tokenUtils = AuthenticationService.getInstance().tokenUtils();
    }

    public UserDto() {}

    public UserDto(int id, String name, String email, String created, String jwt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.created = created;
        this.jwt = jwt;
    }

    public UserDto(User user) {
        this.jwt = tokenUtils.generateJwtFromEntity(user);
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.created = user.getCreated().toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return id == userDto.id &&
                Objects.equals(name, userDto.name) &&
                Objects.equals(email, userDto.email) &&
                Objects.equals(created, userDto.created) &&
                Objects.equals(jwt, userDto.jwt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, created);
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
