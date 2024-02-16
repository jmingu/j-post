package com.post.common.configuration.filter;
import com.post.common.configuration.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


// OncePerRequestFilter => 매 요청때마다 필터를 씌울것이다
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    @Value("${jwt.secret-key}")
    private String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        // 헤더 정보
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.debug("header ==> {}", header);
        // 토큰있는 헤더시작은 Bearer 로 시작한다.
        if (header == null || !header.startsWith("Bearer ")) { // 띄어쓰기 있음
            log.error("Error header");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 토큰 가져오기 (Bearer를 뺸다)
            final String token = header.split(" ")[1].trim();

            // 토큰유효 확인
            if (jwtUtil.isExpired(token, secretKey)) {
                log.error("Error token expired");
                filterChain.doFilter(request, response);
                return;
            }

//            // 토큰에서 유저네임 가저온다
//            String userName = JwtTokenUtils.getUserName(token, secretKey);
//
//            // 유저네임이 유효한다 확인한다.
//            User user = userService.loadUserbyUserName(userName);


            // 토큰, 유저 다 유효한다.
            // principal => 유저가 누구인지 넣어준다
            // credentials => 비밀번호
            // authorities => 권한

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    null, null, null
            );

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//            // 여기에 context에 넣어 controller까지 넘겨 사용할 수 있음
            SecurityContextHolder.getContext().setAuthentication(authentication); // 여기까지 설정해야 시큐리티 정상으로 넘어간다.


        } catch (RuntimeException e) {
            log.error("Error token");
            filterChain.doFilter(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
