package edu.dental.beans;

import java.util.Objects;

public class UserBean {

    private int id;
    private String name;
    private String email;
    private String created;
    private String jwt;

    public UserBean() {}

    public UserBean(int id, String name, String email, String created, String jwt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.created = created;
        this.jwt = jwt;
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
        UserBean userBean = (UserBean) o;
        return Objects.equals(name, userBean.name) && Objects.equals(email, userBean.email) &&
                Objects.equals(created, userBean.created) && Objects.equals(jwt, userBean.jwt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(created, jwt);
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", created='" + created + '\'' +
                '}';
    }
}
