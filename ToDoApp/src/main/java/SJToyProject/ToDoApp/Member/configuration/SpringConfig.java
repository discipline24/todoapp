package SJToyProject.ToDoApp.Member.configuration;

import SJToyProject.ToDoApp.Member.repository.MemberRepository;
import SJToyProject.ToDoApp.Member.repository.MySqlMemberRepository;
import SJToyProject.ToDoApp.Member.service.MemberService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class SpringConfig {
    private final EntityManager em;
    private final JavaMailSender javaMailSender;

    @Autowired
    public SpringConfig(EntityManager em, JavaMailSender javaMailSender) {
        this.em = em;
        this.javaMailSender = javaMailSender;
    }
    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository(), javaMailSender);
    }


    @Bean
    public MemberRepository memberRepository() {
        return new MySqlMemberRepository(em);
    }


}
