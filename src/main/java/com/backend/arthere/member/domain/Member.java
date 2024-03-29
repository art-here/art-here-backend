package com.backend.arthere.member.domain;

import com.backend.arthere.satisfactions.domain.Satisfactions;
import com.backend.arthere.starRatings.domain.StarRatings;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "member")
@EntityListeners(AuditingEntityListener.class)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "email")
    private String email;

    @NotNull
    @Column(name = "name", length = 50)
    private String name;

    @NotNull
    @Column(name = "profile")
    private String profile;

    @CreatedDate
    @Column(name = "create_date", updatable = false)
    private LocalDateTime createDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 50)
    private Role role;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "social_type", length = 50)
    private SocialType socialType;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private final Set<Satisfactions> satisfactions= new LinkedHashSet<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private final Set<StarRatings> starRatings= new LinkedHashSet<>();

    @Builder
    public Member(final Long id, final String email, final String name, final String profile,
                  final Role role, final SocialType socialType) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.profile = profile;
        this.role = role;
        this.socialType = socialType;
    }

    public String getRoleName() {
        return this.role.getRoleName();
    }

    public void changeName(final String name) {
        this.name = name;
    }

    public boolean isSameId(final Long id) {
        return this.id.equals(id);
    }
}
