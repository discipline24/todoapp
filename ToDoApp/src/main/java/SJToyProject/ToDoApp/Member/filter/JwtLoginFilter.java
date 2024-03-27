package SJToyProject.ToDoApp.Member.filter;

import SJToyProject.ToDoApp.Member.dto.LoginDTO;
import SJToyProject.ToDoApp.Member.dto.MemberDetails;
import SJToyProject.ToDoApp.Member.util.CloneableRequestWrapper;
import SJToyProject.ToDoApp.Member.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.weaver.tools.Trace;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private CloneableRequestWrapper requestWrapper;

    final static Logger logger = LogManager.getLogger(JwtLoginFilter.class);

    public JwtLoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        //url 안바꿔주면 /login 으로 디폴트 설정됨
        this.setFilterProcessesUrl("/api/user/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        String method = request.getMethod();
        if (!method.equals("POST")) {
            throw new AuthenticationException("Authentication method not supported: " + request.getMethod()) {
            };
        }

        LoginDTO loginDTO;
        try {
            // getInputStream() 여러번 위해 request 복제
            CloneableRequestWrapper requestWrapper = new CloneableRequestWrapper(request);
            this.requestWrapper = requestWrapper;

            ServletInputStream inputStream = requestWrapper.getInputStream();
            loginDTO = new ObjectMapper().readValue(inputStream, LoginDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword(), null);

        return authenticationManager.authenticate(authToken);


    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Authentication authentication) throws IOException, ServletException {

        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();

        String email = memberDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        String token = jwtUtil.createJwt(email, role, 60*60*1000L);

        //헤더에 Authorization 이라는 키와 jwt token 넣어서 전송
        response.addHeader("Authorization", "Bearer " + token);
        filterChain.doFilter(this.requestWrapper, response);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        System.out.println("Authentication failed");

//        response.setStatus(HttpStatus.BAD_REQUEST.value());
//        response.setContentType("application/json");
//
//        response.getWriter().write(new ObjectMapper().writeValueAsString(exception.getMessage()));
//        logger.trace(exception.getMessage());

    }


}
