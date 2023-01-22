package dev.thrako.receptaria.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Getter

@NoArgsConstructor
@Component
@SessionScope
public class CurrentUser {

    private Long id;
    private String email;
    private String displayName;

    public Boolean isLoggedIn () {

        return email != null;
    }

    public void clear () {

        id = null;
        email = null;
        displayName = null;
    }

    public void setId (Long id) {

        this.id = id;
    }

    public void setEmail (String email) {

        this.email = email;
    }

    public void setDisplayName (String displayName) {

        this.displayName = displayName;
    }
}

