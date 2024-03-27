package SJToyProject.ToDoApp.Member.filter;

import SJToyProject.ToDoApp.Member.dto.LoginDTO;
import SJToyProject.ToDoApp.Member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

public class VerifyUserFilter implements Filter {

    private final MemberService memberService;

    public VerifyUserFilter(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String method = httpServletRequest.getMethod();
        if (!method.equals("POST")) {
            throw new AuthenticationException("Authentication method not supported: " + httpServletRequest.getMethod()) {
            };
        }

        LoginDTO loginDTO;
        try {
            ServletInputStream inputStream = request.getInputStream();
            loginDTO = new ObjectMapper().readValue(inputStream, LoginDTO.class);
            if(memberService.login(loginDTO)) {
                request.setAttribute("authenticateUser", loginDTO);
            }
            chain.doFilter(request, response);
        } catch (IOException e) {
            System.out.println("Failed user verify");
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.sendError(HttpStatus.BAD_REQUEST.value());
        }
    }
}
