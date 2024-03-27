package SJToyProject.ToDoApp.Member.domain;

import SJToyProject.ToDoApp.Member.dto.SignupDTO;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class Member {

    @Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String nickname;
    private String role;
    public Member (SignupDTO signupDTO){
        this.setEmail(signupDTO.getEmail());
        this.setPassword(signupDTO.getPassword());
        this.setNickname(signupDTO.getNickname());
    }

    public Member() {
    }
}
