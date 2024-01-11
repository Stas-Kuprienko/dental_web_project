package edu.dental.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * The account class, containing user information and is used for authentication.
 */
public class User implements Serializable, IDHaving {

    private int id;

    private String name;

    private String email;

    private byte[] password;

    private LocalDate created;


    /**
     * Create a new User object.
     * @param name     The user name.
     * @param email    Login for the user authorization.
     * @param password The password for access to the account
     */
    public User(String name, String email, byte[] password) {
        if ((name == null || name.isEmpty())
                || (email == null || email.isEmpty())
                || (password == null || password.length == 0)) {
            throw new IllegalArgumentException();
        }
        this.created = LocalDate.now();
        this.name = name;
        this.email = email;
        this.password = password;
    }

    /**
     * Constructor for data access object.
     */
    public User() {}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(name, user.name) &&
                Objects.equals(email, user.email) && Objects.equals(created, user.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, created);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }


    /*                           ||
            Getters and setters  \/
     */

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

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

}
