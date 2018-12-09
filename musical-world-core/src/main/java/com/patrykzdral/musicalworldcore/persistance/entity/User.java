package com.patrykzdral.musicalworldcore.persistance.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
@EqualsAndHashCode
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private Long id;

    @NotNull
    @Size(min = 2, max = 15)
    @Column(name = "username")
    @Getter
    private String username;

    @NotNull
    @Column(name = "password")
    @Getter
    private String password;

    @NotNull
    @Email
    @Column(name = "email")
    @Getter
    private String email;

    @Size(max = 255)
    @Column(name = "description")
    @Getter
    private String description;

    @Column(name = "phone_number")
    @Getter
    private String phoneNumber;

    @Size(max = 15)
    @Column(name = "first_name")
    @Getter
    private String firstName;

    @Size(max = 20)
    @Column(name = "last_name")
    @Getter
    private String lastName;

    @NotNull
    @Column(name = "confirmed")
    @Getter
    private boolean confirmed;

    @Column(name = "to_be_deleted")
    @Getter
    private boolean toBeDeleted;

    @Column(name = "remember_me")
    @Getter
    private boolean rememberMe;

    @OneToOne
    @JoinColumn(name = "address_id")
    @Getter
    private Address address;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "picture_id")
    @Getter
    private Picture picture;

    @NotNull
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
            , inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    @Getter
    private Collection<Role> roles;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL})
    private List<Concert> concerts;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL})
    private List<Friendship> friendships;

    @OneToMany(mappedBy = "friend", cascade = {CascadeType.ALL})
    private List<Friendship> myFriendships;

    @OneToMany(mappedBy = "userFrom", cascade = {CascadeType.ALL})
    private List<UserReference> userFromReference;

    @OneToMany(mappedBy = "userTo", cascade = {CascadeType.ALL})
    private List<UserReference> userToReference;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL})
    private List<ConcertApplication> concertApplications;

}
