package com.libraryrest.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.libraryrest.enums.UserRole;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "role")
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH,CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<User> users = new HashSet<User>();

    @Enumerated(EnumType.STRING)
    @Column(name = "title")
    private UserRole roleName;

    public Role() {

    }

    public Role(UserRole roleName) {
        this.roleName = roleName;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public UserRole getRoleName() {
        return roleName;
    }

    public void setRoleName(UserRole roleName) {
        this.roleName = roleName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "id: " + id + "name: "+roleName;
    }
}