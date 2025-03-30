package org.ngcvfb.eventhubkz.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.time.LocalDateTime;
import java.util.*;

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
@EqualsAndHashCode(exclude = {"likes","contacts"})
public class UserModel implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, unique = true)
    private String username;


    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private String description = "No description provided";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String avatarUrl;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_contacts", joinColumns = @JoinColumn(name = "user_id"))
    @MapKeyColumn(name = "type") // Тип контакта (Telegram, WhatsApp)
    @Column(name = "value") // Значение (ссылка)
    private Map<String, String> contacts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<EventLike> likes =  new HashSet<>();

    @Column(name = "verification_code")
    private String verificationCode;
    @Column(name = "verification_expiration")
    private LocalDateTime verificationCodeExpiresAt;
    @Column(name = "enabled")
    private boolean enabled;

    public UserModel(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
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
        return enabled;
    }



    // Геттер, возвращающий копию Set<EventLike>
    public Set<EventLike> getLikes() {
        return likes == null ? Collections.emptySet() : new HashSet<>(likes);
    }

    // Сеттер для лайков
    public void setLikes(Set<EventLike> likes) {
        this.likes = likes == null ? new HashSet<>() : new HashSet<>(likes);
    }
}


