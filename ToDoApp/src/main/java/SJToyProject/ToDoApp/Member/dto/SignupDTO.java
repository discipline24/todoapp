package SJToyProject.ToDoApp.Member.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class SignupDTO {
    @Email
    private String email;
    private String password;
    private String nickname;
    public int emailAuth;

    public SignupDTO() {
    }

    public SignupDTO(String email) {
        this.email = email;
    }
    public SignupDTO(String email, String password, String nickname, int emailAuth) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.emailAuth = emailAuth;
    }

}
