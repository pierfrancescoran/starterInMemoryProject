package com.peter.model.httpbody;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * View Model object for storing a user's credentials.
 */
public class LoginVM {

    @NotNull
    @NotEmpty
    @NotBlank
    @Size(min = 6, max = 30)
    private String email;

    @NotNull
    @NotEmpty
    @NotBlank
    @Size(min = 6, max = 15)
    private String password;

    @NotNull
    private Boolean rememberMe;

    public String getUsername() {
        return email;
    }

    public void setUsername(String username) {
        this.email = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    @Override
    public String toString() {
        return "LoginVM{" +
            "password='*****'" +
            ", username='" + email + '\'' +
            ", rememberMe=" + rememberMe +
            '}';
    }
    
}
