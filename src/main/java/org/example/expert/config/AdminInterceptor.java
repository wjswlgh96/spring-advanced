package org.example.expert.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.auth.exception.AdminException;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
public class AdminInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String bearerJwt = request.getHeader("Authorization");

        String jwt = jwtUtil.substringToken(bearerJwt);
        Claims claims = jwtUtil.extractClaims(jwt);

        UserRole userRole = UserRole.valueOf(claims.get("userRole", String.class));
        if (!UserRole.ADMIN.equals(userRole)) {
            log.info("일반 유저 접근 시도 차단, 요청 시각: {}, URL: {}", LocalDateTime.now(), request.getRequestURI());
            throw new AdminException("관리자 권한이 없습니다.");
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("어드민 유저 접근, 요청 시각: {}, URL: {}", LocalDateTime.now(), requestURI);
    }
}
