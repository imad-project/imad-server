package com.ncookie.imad.service;

import com.ncookie.imad.domain.type.AuthProvider;
import com.ncookie.imad.dto.request.TokenRequest;
import com.ncookie.imad.dto.response.SignInResponse;
import com.ncookie.imad.dto.response.TokenResponse;
import com.ncookie.imad.exception.BadRequestException;
import com.ncookie.imad.repository.UserAccountRepository;
import com.ncookie.imad.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final KakaoRequestService kakaoRequestService;
    private NaverRequestService naverRequestService;
    private GoogleRequestService googleRequestService;
    private final UserAccountRepository userAccountRepository;
    private final SecurityUtil securityUtil;

    public SignInResponse redirect(TokenRequest tokenRequest) {
        String registrationId = tokenRequest.getRegistrationId();

        if(AuthProvider.KAKAO.getAuthProvider().equals(registrationId)){
            return kakaoRequestService.redirect(tokenRequest);
        }

//        else if(AuthProvider.NAVER.getAuthProvider().equals(registrationId)){
//            return naverRequestService.redirect(tokenRequest);
//        } else if(AuthProvider.GOOGLE.getAuthProvider().equals(registrationId)) {
//            return googleRequestService.redirect(tokenRequest);
//        } else if (AuthProvider.APPLE.getAuthProvider().equals(registrationId)) {
//            return null;
//        }

        throw new BadRequestException("지원되지 않는 oauth provider 입니다");
    }

    public SignInResponse refreshToken(TokenRequest tokenRequest){
        String userId = (String) securityUtil.get(tokenRequest.getRefreshToken()).get("userId");
        String provider = (String) securityUtil.get(tokenRequest.getRefreshToken()).get("provider");
        String oldRefreshToken = (String) securityUtil.get(tokenRequest.getRefreshToken()).get("refreshToken");

        if(!userAccountRepository.existsByUserIdAndAuthProvider(userId, AuthProvider.findByCode(provider))){
            throw new BadRequestException("CANNOT_FOUND_USER");
        }

        TokenResponse tokenResponse = null;
        if(AuthProvider.KAKAO.getAuthProvider().equals(provider.toLowerCase())){
            tokenResponse = kakaoRequestService.getRefreshToken(provider, oldRefreshToken);
        }

//        else if(AuthProvider.NAVER.getAuthProvider().equals(provider.toLowerCase())){
//            tokenResponse = naverRequestService.getRefreshToken(provider, oldRefreshToken);
//        } else if(AuthProvider.GOOGLE.getAuthProvider().equals(provider.toLowerCase())){
//            tokenResponse = googleRequestService.getRefreshToken(provider, oldRefreshToken);
//        }

        assert tokenResponse != null;
        String accessToken = securityUtil.createAccessToken(
                userId, AuthProvider.findByCode(provider.toLowerCase()), tokenResponse.getAccessToken());

        return SignInResponse.builder()
                .authProvider(AuthProvider.findByCode(provider.toLowerCase()))
                .kakaoUserInfo(null)
                .accessToken(accessToken)
                .refreshToken(null)
                .build();
    }

}
