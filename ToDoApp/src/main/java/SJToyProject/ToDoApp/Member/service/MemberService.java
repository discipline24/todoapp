package SJToyProject.ToDoApp.Member.service;

import SJToyProject.ToDoApp.Member.dto.LoginDTO;
import SJToyProject.ToDoApp.Member.dto.MemberDetails;
import SJToyProject.ToDoApp.Member.dto.SignupDTO;
import SJToyProject.ToDoApp.Member.domain.Member;
import SJToyProject.ToDoApp.Member.repository.MemberRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@Transactional
public class MemberService implements UserDetailsService{
    private final MemberRepository memberRepository;
    private final JavaMailSender javaMailSender;
    private static final String senderEmail = "dnlqhdbs@gmail.com";
    private static int number;
    private static String memberEmail;
    private void AuthNumber() {
        number = (int)(Math.random()*(90000))+10000;
    }
    @Autowired
    private PasswordEncoder encoder;

    public MemberService(MemberRepository memberRepository, JavaMailSender javaMailSender) {
        this.memberRepository = memberRepository;
        this.javaMailSender = javaMailSender;
    }

    public boolean login(LoginDTO loginDTO) {
        if(loginDTO.getEmail().isEmpty())
            throw new NullPointerException("이메일 작성해주세요!");
        if(loginDTO.getPassword().isEmpty())
            throw new NullPointerException("비밀번호 작성해주세요!");

        Member guest = memberRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new NullPointerException("저장소에 사용자가 존재하지 않습니다."));

        if (!encoder.matches(loginDTO.getPassword(), guest.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 잘못 입력되었습니다.");
        }
        return true;
    }

    public void signup(SignupDTO signupDTO) {
        //공백, 이메일 인증, 중복 이메일 등 회원가입 예외 처리
        checkExceptions(signupDTO);
        isOnlyEmailExists(signupDTO.getEmail());

        //비밀번호 암호화
        signupDTO.setPassword(encoder.encode(signupDTO.getPassword()));

        Member newMember = new Member(signupDTO);
        memberRepository.save(newMember);
    }

    public int sendEmail(String email) {
        isOnlyEmailExists(email);
        javaMailSender.send(createEmail(email));
        memberEmail = email;
        return number;
    }
    private MimeMessage createEmail (String email) {
        MimeMessage message = javaMailSender.createMimeMessage();
        AuthNumber();
        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("TodoList 회원가입 인증 메일 입니다.");
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h1>" + number + "</h1>";
            message.setText(body,"UTF-8","html");
            body += "<h3>" + "감사합니다." + "</h3>";
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return message;
    }

    //이메일 중복, 공백 예외 처리
    private void isOnlyEmailExists(String email) {
        memberRepository.findByEmail(email)
                .ifPresent(m -> {
                    throw new IllegalArgumentException("이미 가입된 이메일입니다.");
                });
        if(email.isEmpty())
            throw new NullPointerException("이메일 작성해주세요!");
    }

    //공백, 인증번호 예외 처리
    private void checkExceptions(SignupDTO signupDTO) {
        if(signupDTO.getPassword().isEmpty())
            throw new NullPointerException("비밀번호 작성해주세요!");
        if(signupDTO.getNickname().isEmpty())
            throw new NullPointerException("닉네임 작성해주세요!");
        if(signupDTO.getEmailAuth() != number) {
            throw new NullPointerException("올바른 인증번호를 작성해주세요!");
        }
        //인증번호 보낸 메일이 아닌 예외
        if(!signupDTO.getEmail().equals(memberEmail)) {
            throw new IllegalArgumentException("인증번호를 요청한 이메일이 아닙니다.");
        }
    }

    @Override  //스프링 시큐리티 UserDetails 오버라이드 메소드
    public MemberDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member guest = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("There is no member with this email."));
        return new MemberDetails(guest);
    }

}
