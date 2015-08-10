package com.libraryrest.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.libraryrest.enums.UserStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "user")
public class User implements UserDetails, Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "login", unique = true, nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @JsonIgnore
    @Transient
    private String matchingPassword;

    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH,CascadeType.PERSIST})
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId"))
    private Set<Role> roles = new HashSet<Role>();

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<Book> books = new HashSet<Book>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<Author> authors = new HashSet<Author>();

    @Column(name = "avatarUrl")
    private String avatarUrl;

    @Column(name = "confirm_email_token")
    String confirmEmailToken;

    @Column(name = "reset_password_token_creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date resetPasswordTokenCreationDate;

    @Column(name = "confirm_reset_password_token")
    private String confirmResetPasswordToken;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getConfirmEmailToken() {
        return confirmEmailToken;
    }

    public void setConfirmEmailToken(String confirmEmailToken) {
        this.confirmEmailToken = confirmEmailToken;
    }

    public Date getResetPasswordTokenCreationDate() {
        return resetPasswordTokenCreationDate;
    }

    public void setResetPasswordTokenCreationDate(Date resetPasswordTokenCreationDate) {
        this.resetPasswordTokenCreationDate = resetPasswordTokenCreationDate;
    }

    public String getConfirmResetPasswordToken() {
        return confirmResetPasswordToken;
    }

    public void setConfirmResetPasswordToken(String confirmResetPasswordToken) {
        this.confirmResetPasswordToken = confirmResetPasswordToken;
    }

    //
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> result = new ArrayList<SimpleGrantedAuthority>();

        for (Role role : roles) {
            result.add(new SimpleGrantedAuthority(role.getRoleName().name()));
        }

        return result;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public UserStatus getStatus() {
        return status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }


    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }
}
