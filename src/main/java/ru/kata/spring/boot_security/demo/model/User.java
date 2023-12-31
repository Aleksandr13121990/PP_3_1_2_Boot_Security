package ru.kata.spring.boot_security.demo.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username")
    @NotBlank
    @Pattern(regexp = "[A-Za-z0-9]+", message = "Только латинские буквы и цифры")
    private String username;

    @Column(name = "password")
    @NotBlank
    @Size(min = 6, message = "Не меньше 6 знаков")
    @Pattern(regexp = "[a-zA-Zа-яА-Я0-9!@#$%^&*()-_+=;:,./\\\\|`~\\[\\]{}]+",
            message = "Допускаются только русские и английские буквы, цифры и специальные символы  ! @ # $ % ^ & * ( ) - _ + = ; : , ./ ? \\ | ` ~ [ ] { }")
    private String password;

    @Column(name = "name")
    @NotBlank
    @Pattern(regexp = "[a-zA-Zа-яА-Я]+", message = "Допускаются только русские и английские буквы")
    private String name;

    @Column(name = "surname")
    @NotBlank
    @Pattern(regexp = "[a-zA-Zа-яА-Я]+", message = "Допускаются только русские и английские буквы")
    private String surname;

    @Column(name = "email")
    @NotBlank
    @Email(message = "Hе валидный email")
    private String email;

    @NotEmpty(message = "Роль не может быть пустой")
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;

    public User() {
    }

    public User(String username, String password, String name, String surname, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(name, user.name) && Objects.equals(surname, user.surname) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, email);
    }
}
