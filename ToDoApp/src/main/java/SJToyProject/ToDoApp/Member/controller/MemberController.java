package SJToyProject.ToDoApp.Member.controller;

import SJToyProject.ToDoApp.Member.dto.LoginDTO;
import SJToyProject.ToDoApp.Member.dto.SignupDTO;
import SJToyProject.ToDoApp.Member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/api/user/signup")
    public ResponseEntity<String> MailSend(@RequestBody SignupDTO signupDTO){
        try {
            int number = memberService.sendEmail(signupDTO.getEmail());
            return ResponseEntity.ok("해당 이메일에 인증 번호를 보냈습니다.");
        } catch (IllegalArgumentException | NullPointerException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/api/user/signup")
    public ResponseEntity<String> create(@RequestBody SignupDTO signupDTO) {
        try {
            memberService.signup(signupDTO);
            return ResponseEntity.ok("회원가입에 성공하였습니다.");
        }
        catch (IllegalArgumentException | NullPointerException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

        //기존 로그인 컨트롤러 - 로그인 필터에서 처리
    @PostMapping(value = "/api/user/login")
    public ResponseEntity<Object> loginService(@RequestBody LoginDTO loginDTO) {
        System.out.println("login");
        try {
            memberService.login(loginDTO);
            return ResponseEntity.ok(loginDTO);
        }
        catch (IllegalArgumentException | NullPointerException | UsernameNotFoundException e)  {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}


