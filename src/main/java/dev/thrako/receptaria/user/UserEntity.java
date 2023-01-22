package dev.thrako.receptaria.user;

import dev.thrako.receptaria.constants.Roles;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column
        private String firstName;

        @Column
        private String lastName;

        @Column
        private String displayName;

        @Column(unique = true)
        @Nonnull
        private String email;

        @Column
        @Nonnull
        private String password;

        @Column
        private boolean active;

        @Column
        @Enumerated(EnumType.STRING)
        private Roles role;

}
