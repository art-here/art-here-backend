package com.backend.arthere.satisfactions.domain;

import com.backend.arthere.arts.domain.*;
import com.backend.arthere.global.config.JpaConfig;
import com.backend.arthere.global.config.QueryDslConfig;
import com.backend.arthere.member.domain.Member;
import com.backend.arthere.member.domain.MemberRepository;
import com.backend.arthere.member.domain.Role;
import com.backend.arthere.member.domain.SocialType;
import com.backend.arthere.satisfactions.dto.response.SatisfactionsListResponse;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;

import static com.backend.arthere.fixture.MemberFixtures.회원;

@DataJpaTest
@Import({QueryDslConfig.class, JpaConfig.class})
class SatisfactionsRepositoryTest {

    @Autowired
    SatisfactionsRepository satisfactionsRepository;

    @Autowired
    ArtsRepository artsRepository;

    @Autowired
    MemberRepository memberRepository;


    private Long artsSaveData() {

        String artName = "모래작품";
        String imageURL = "image/sand";
        Address address = new Address("loadAddress");

        return artsRepository.save(
                Arts.builder()
                        .artName(artName)
                        .imageURL(imageURL)
                        .location(new Location(37.564878339197044, 126.9758637182802))
                        .address(address)
                        .category(Category.PICTURE).build()).getId();

    }

    private void memberSaveData() {
        for (int i = 1; i <= 5; i++) {
            memberRepository.save(
                    new Member(Long.parseLong(String.valueOf(i)), "email", "name", "profile", Role.USER, SocialType.GOOGLE));
        }
    }

}