package com.backend.arthere.auth.application;

import com.backend.arthere.auth.domain.Token;
import com.backend.arthere.auth.domain.TokenRepository;
import com.backend.arthere.auth.dto.response.TokenResponse;
import com.backend.arthere.auth.jwt.JwtTokenProvider;
import com.backend.arthere.member.domain.Member;
import com.backend.arthere.member.domain.MemberRepository;
import com.backend.arthere.member.domain.Role;
import com.backend.arthere.member.domain.SocialType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FakeLoginService {
    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponse login() {

        int number = (int)(Math.random()*100);
        String email = number + "@gmail.com";

        Member member = findMember(email);

        String accessToken = jwtTokenProvider.createAccessToken(String.valueOf(member.getId()));
        tokenRepository.save(new Token(accessToken, member));
        return new TokenResponse(accessToken);
    }

    public Member findMember(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if(member.isPresent()) {
            return member.get();
        }
        return memberRepository.save(Member.builder()
                .name("회원")
                .email(email)
                .socialType(SocialType.GOOGLE)
                .role(Role.USER)
                .profile("사진")
                .build());
    }
}
