package com.post.sns.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.post.common.properties.Oauth2NaverRegistrationProperties;
import com.post.sns.dto.response.LoginTokenDto;
import com.post.common.feign.NidNaverFeignClient;
import com.post.common.feign.OpenApiNaverFeignClient;
import com.post.sns.dto.OAuth2NaverLoginResultDto;
import com.post.sns.dto.OAuth2NaverLoginTokenDto;
import com.post.sns.entity.Oauth2UserEntity;
import com.post.sns.entity.OrganizationEntity;
import com.post.sns.repository.UserEntityRepository;
import com.post.common.configuration.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OAuth2LoginService {

    private final Oauth2NaverRegistrationProperties oauth2NaverRegistrationProperties;
    private final UserEntityRepository userEntityRepository;
    private final JWTUtil jwtUtil;
    @Value("${jwt.token.expired-time}")
    private int expiredTime;
    @Value("${jwt.token.expired-refresh-time}")
    private int refreshTime;
    private final NidNaverFeignClient nidNaverFeignClient;
    private final OpenApiNaverFeignClient openApiNaverFeignClient;
    @Transactional
    public LoginTokenDto getToken(String code, String state) throws Exception {

        ResponseEntity<String> token = nidNaverFeignClient.getToken(oauth2NaverRegistrationProperties.getAuthorizationGrantType(),
                oauth2NaverRegistrationProperties.getClientId(),
                oauth2NaverRegistrationProperties.getClientSecret(),
                code,
                state);
        log.debug("token ==> {}", token.getBody());

        // 토큰값 Json 형식으로 가져오기위해 생성
        ObjectMapper objectMapper = new ObjectMapper();
        OAuth2NaverLoginTokenDto oAuth2NaverLoginTokenDto = objectMapper.readValue(token.getBody(), OAuth2NaverLoginTokenDto.class);
        log.debug("oAuth2LoginToken ==> {} " , oAuth2NaverLoginTokenDto);

        ResponseEntity<String> result = openApiNaverFeignClient.getResult("Bearer " + oAuth2NaverLoginTokenDto.getAccessToken());
        log.debug("result ==> {} " , result);

        // 토큰을 사용하여 사용자 정보 추출
        OAuth2NaverLoginResultDto oAuth2NaverLoginResultDto = objectMapper.readValue(result.getBody(), OAuth2NaverLoginResultDto.class);
        log.debug("naver login info ==> {}" , oAuth2NaverLoginResultDto);

        // 아이디 조회 후 없을 시 회원가입
        long loginIdCnt = userEntityRepository.countByLoginId(oAuth2NaverLoginResultDto.getResponse().getId());

        log.debug("loginIdCnt ==> {}", loginIdCnt);
        if (loginIdCnt == 0) {
            log.debug("loginId is null");
            OrganizationEntity organizationEntity = OrganizationEntity.builder()
                    .organizationId(1)
                    .organizationName("기본")
                    .build();

            Oauth2UserEntity oauth2UserEntity = new Oauth2UserEntity(
                    oAuth2NaverLoginResultDto.getResponse().getId(),
                    oAuth2NaverLoginResultDto.getResponse().getName(),
                    oAuth2NaverLoginResultDto.getResponse().getEmail(),
                    oAuth2NaverLoginResultDto.getResponse().getEmail(),
                    oAuth2NaverLoginResultDto.getResponse().getGender(),
                    "NAVER",
                    organizationEntity
            );

            // 회원가입
            userEntityRepository.save(oauth2UserEntity);
        }

        Oauth2UserEntity oauth2UserEntity = userEntityRepository.findByLoginId(oAuth2NaverLoginResultDto.getResponse().getId()).get();

        // 엑세스토큰발행(1시간)
        String accessToken = jwtUtil.makeAuthToken(oauth2UserEntity, expiredTime);
        // 리프레시 토큰발행(1달)
        String refreshToken = jwtUtil.makeAuthToken(oauth2UserEntity, refreshTime);

        LoginTokenDto loginTokenDto = new LoginTokenDto(accessToken, refreshToken);

        return loginTokenDto;
    }
}
