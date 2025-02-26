package org.example.expert.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.auth.exception.AdminException;
import org.example.expert.domain.auth.exception.AuthException;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String url = request.getRequestURI();
        if (url.startsWith("/auth")) {
            return true;
        }

        String bearerJwt = request.getHeader("Authorization");

        if (bearerJwt == null) {
            throw new InvalidRequestException("JWT 토큰이 필요합니다.");
        }

        String jwt = jwtUtil.substringToken(bearerJwt);

        try {
            Claims claims = jwtUtil.extractClaims(jwt);
            if (claims == null) {
                throw new InvalidRequestException("잘못된 JWT 토큰입니다.");
            }

            UserRole userRole = UserRole.valueOf(claims.get("userRole", String.class));

            request.setAttribute("userId", Long.parseLong(claims.getSubject()));
            request.setAttribute("email", claims.get("email"));
            request.setAttribute("userRole", claims.get("userRole"));

            if (url.startsWith("/admin")) {
                if (!UserRole.ADMIN.equals(userRole)) {
                    throw new AdminException("관리자 권한이 없습니다.");
                }
            }

            return true;
        } catch (AdminException e) {
            log.info("일반 유저 접근 시도 차단, 요청 시각: {}, URL: {}", LocalDateTime.now(), request.getRequestURI());
            throw e;
        }catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature, 유효하지 않은 JWT 서명 입니다.", e);
            throw new AuthException("유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.", e);
            throw new AuthException("만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.", e);
            throw new InvalidRequestException("지원되지 않는 JWT 토큰 입니다.");
        } catch (Exception e) {
            log.error("Invalid JWT token, 유효하지 않는 JWT 토큰 입니다.", e);
            throw new InvalidRequestException("유효하지 않는 JWT 토큰 입니다.");
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();

        if (requestURI.startsWith("/auth")) {
            return;
        } else if (requestURI.startsWith("/admin")) {
            log.info("어드민 유저 접근, 요청 시각: {}, URL: {}", LocalDateTime.now(), requestURI);
        }
    }
}
