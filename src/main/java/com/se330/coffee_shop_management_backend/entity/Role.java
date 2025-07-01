package com.se330.coffee_shop_management_backend.entity;

import com.se330.coffee_shop_management_backend.util.Constants;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name"}, name = "uk_roles_name")
}, indexes = {
    @Index(columnList = "name", name = "idx_roles_name")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role extends AbstractBaseEntity {
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<User> users = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "name", length = 16)
    @NaturalId
    private Constants.RoleEnum name;

    public Role(final Constants.RoleEnum name) {
        this.name = name;
    }
}
