package SJToyProject.ToDoApp.Member.configuration;

import SJToyProject.ToDoApp.Member.filter.JwtAuthenticationFilter;
import SJToyProject.ToDoApp.Member.filter.JwtLoginFilter;
import SJToyProject.ToDoApp.Member.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JwtUtil jwtUtil) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                //csrf 공격 방어 (stateless 라서 안먹힘, 세션방식에선 해야됨)
                .csrf(AbstractHttpConfigurer::disable)
                //Form 로그인 방식
                .formLogin(AbstractHttpConfigurer::disable)
                //http basic 인증 방식
                .httpBasic(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/user/login","/api/user/signup")
                        .permitAll()
                        .anyRequest().authenticated())
                // JWT 로그인 필터
                .addFilterAt(new JwtLoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class)
                // JWT 인증 필터
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil), JwtLoginFilter.class)
                //세션 설정 (제일 중요, stateless)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }


}
