package prjcb04.amaiproject2024.business.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String password;
    private String confirmPassword;


    @Email(message = "Email should be valid.")
    @NotBlank(message = "Email must not be empty.")
    private String email;
    private Boolean isSubscribed;
    private Boolean isEnabled;
    private String username;
    private String fullName;
    private List<String> role;
    private String jwt;
    private String siteURL;
}