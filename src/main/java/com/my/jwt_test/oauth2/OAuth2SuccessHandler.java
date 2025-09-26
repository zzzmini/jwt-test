package com.my.jwt_test.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.jwt_test.dto.CustomOAuth2User;
import com.my.jwt_test.myjwt.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();

        String email = oauthUser.getEmail();  // CustomOAuth2User에서 정의 필요
        String role = oauthUser.getRole();    // CustomOAuth2User에서 정의 필요

        String jwtToken = jwtUtil.createJwt(email, role, 60 * 60 * 1000L); // 1시간

        Map<String, String> tokenResponse = Map.of("token", jwtToken);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(tokenResponse));

        // 리액트 페이지로 보내기
        // ✅ 여기부터 HTML 응답
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = response.getWriter();

        writer.println("<html><body>");
        writer.println("<script>");
        writer.println("window.opener.postMessage(" +
                "'" + jwtToken + "', 'http://localhost:3000');");
        writer.println("window.close();");
        writer.println("</script>");
        writer.println("</body></html>");
    }
}
