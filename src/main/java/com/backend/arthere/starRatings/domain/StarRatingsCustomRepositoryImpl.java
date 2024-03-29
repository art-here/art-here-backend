package com.backend.arthere.starRatings.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.backend.arthere.starRatings.domain.QStarRatings.starRatings;


@Repository
@RequiredArgsConstructor
public class StarRatingsCustomRepositoryImpl implements StarRatingsCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Integer findStarRatingsId(Long artId, Long userId) {

        return jpaQueryFactory.select(starRatings.starRating.coalesce(0).as("starRating"))
                .from(starRatings)
                .where(starRatings.arts.id.eq(artId), starRatings.member.id.eq(userId))
                .fetchOne();
    }

    @Override
    public StarRatings findStarRatings(Long artId, Long userId) {

        return jpaQueryFactory.select(starRatings)
                .from(starRatings)
                .where(starRatings.arts.id.eq(artId), starRatings.member.id.eq(userId))
                .fetchOne();
    }
}
