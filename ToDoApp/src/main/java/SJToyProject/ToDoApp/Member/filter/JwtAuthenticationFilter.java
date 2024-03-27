package SJToyProject.ToDoApp.Member.filter;

import SJToyProject.ToDoApp.Member.domain.Member;
import SJToyProject.ToDoApp.Member.dto.MemberDetails;
import SJToyProject.ToDoApp.Member.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURL().toString().equalsIgnoreCase("http://localhost:8080/api/user/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorization = request.getHeader("Authorization");
        if(authorization == null || !authorization.startsWith("Bearer ")) {
            System.out.println("token null");
            filterChain.doFilter(request, response);

            return; // 메소드 종료
        }

        String token = authorization.split(" ")[1];

        if (jwtUtil.isExpired(token)) {
            System.out.println("token expired");
            filterChain.doFilter(request, response);

            return; // 메소드 종료
        }
        String email = jwtUtil.getEmail(token);
        String role = jwtUtil.getRole(token);

        Member member = new Member();
        member.setEmail(email);
        member.setPassword("asdf");
        member.setRole(role);
        MemberDetails memberDetails = new MemberDetails(member);

        Authentication authToken = new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());

        SecurityContextHolder
                .getContext()
                .setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }



}
