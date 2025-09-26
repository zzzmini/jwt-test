package com.my.jwt_test.service;

import com.my.jwt_test.dto.CustomOAuth2User;
import com.my.jwt_test.entity.UserEntity;
import com.my.jwt_test.oauth2.OAuth2UserInfo;
import com.my.jwt_test.oauth2.OAuth2UserInfoFactory;
import com.my.jwt_test.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        // 기본 유저 정보 조회(구글이면 성공한 로그인 이메일)
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 어떤 플랫폼으로 로그인했는지 (google / naver / kakao)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId,
                oAuth2User.getAttributes());

        String email = userInfo.getEmail();

        // 임의의 비밀번호 생성
        String password = UUID.randomUUID().toString();

        // 유저 조회 or 저장
        UserEntity user = userRepository.existsByEmail(email)
                ? userRepository.findByEmail(email)
                : userRepository.save(new UserEntity(email, password, "ROLE_USER"));


        // 반환할 CustomOAuth2User 객체 생성
        return new CustomOAuth2User(
                user.getEmail(),
                user.getRole(), // 예: "ROLE_USER"
                oAuth2User.getAttributes(),
                oAuth2User.getAuthorities()
        );
    }
}
