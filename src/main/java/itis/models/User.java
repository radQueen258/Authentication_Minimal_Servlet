package itis.models;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@EqualsAndHashCode
public class User {
    private Long id;
    private String userName;
    private String userEmail;
    private String userPassword;
}
