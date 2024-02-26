package com.post.common.configuration.filter;

import com.post.board.dto.UserAuthenticationResultDto;
import com.post.common.configuration.util.CryptoUtil;
import com.post.common.feign.AuthenticationFeignClient;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Value("${jwt.secret-key}")
    private String secretKey;
    private final AuthenticationFeignClient authenticationFeignClient;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 인증이 증명된 헤더
        final String header = request.getHeader("X-Auth-Status");
        final String key = header.split(" ")[0].trim(); // 암호화 랜덤키
        final String value = header.split(" ")[1].trim(); // 암호화 된 정보

        if (header == null ) { // 띄어쓰기 있음
            log.error("Error header");
            filterChain.doFilter(request, response);
            return;
        }

        try {

            String decrypt = CryptoUtil.decrypt(value, key);

            // controller에서 사용자 정보 조회할 수 있음
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    decrypt, null, null
            );

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//            // 여기에 context에 넣어 controller까지 넘겨 사용할 수 있음
            SecurityContextHolder.getContext().setAuthentication(authentication); // 여기까지 설정해야 시큐리티 정상으로 넘어간다.


        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error token");
            filterChain.doFilter(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
