package com.backend.arthere.details.application;

import com.backend.arthere.arts.domain.Arts;
import com.backend.arthere.arts.domain.ArtsRepository;
import com.backend.arthere.arts.exception.ArtsNotFoundException;
import com.backend.arthere.arts.exception.InvalidCategoryException;
import com.backend.arthere.details.domain.Details;
import com.backend.arthere.details.domain.DetailsRepository;
import com.backend.arthere.details.dto.request.ArtRequest;
import com.backend.arthere.details.dto.response.ArtMapResponse;
import com.backend.arthere.details.dto.response.ArtResponse;
import com.backend.arthere.details.dto.response.ArtSaveResponse;
import com.backend.arthere.details.exception.DetailsNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.backend.arthere.fixture.EntireArtsFixtures.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DetailsServiceTest {
    @Mock
    private DetailsRepository detailsRepository;

    @Mock
    private ArtsRepository artsRepository;

    @InjectMocks
    private DetailsService detailsService;

    @Test
    @DisplayName("작품 정보를 저장한다.")
    public void 작품_저장() throws Exception {
        //given
        Arts arts = 작품();
        Details details = 작품_세부정보(arts);
        ArtRequest artRequest = 작품_저장_요청();

        given(artsRepository.save(any()))
                .willReturn(arts);
        given(detailsRepository.save(any()))
                .willReturn(details);

        //when
        ArtSaveResponse artSaveResponse = detailsService.save(artRequest);

        //then
        assertThat(artSaveResponse.getArtName()).isEqualTo(arts.getArtName());
    }

    @Test
    @DisplayName("잘못된 카테고리 이름으로 작품 정보 저장하려고 할때 예외가 발생한다.")
    public void 잘못된_카테고리_이름으로_작품_저장시_예외_발생() throws Exception {
        //given
        ArtRequest artRequest = 작품_저장_요청("테스트");

        //when //then
        assertThatThrownBy(() -> detailsService.save(artRequest))
                .isInstanceOf(InvalidCategoryException.class);
    }

    @Test
    @DisplayName("작품 전체 정보를 조회한다.")
    public void 작품_전체_정보_조회() throws Exception {
        //given
        Arts arts = 작품();
        Details details = 작품_세부정보(arts);

        given(artsRepository.findById(any()))
                .willReturn(Optional.of(arts));
        given(detailsRepository.findByArts(arts))
                .willReturn(Optional.of(details));

        //when
        ArtResponse artResponse = detailsService.findArt(2L);

        //then
        assertAll(
                () -> assertThat(artResponse.getId()).isEqualTo(details.getId()),
                () -> assertThat(artResponse.getInfo()).isEqualTo(details.getInfo()),
                () -> assertThat(artResponse.getRoadAddress()).isEqualTo(arts.getAddress().getRoadAddress())
        );
    }

    @Test
    @DisplayName("저장되어 있지 않은 arts 아이디로 조회하려고 하면 예외가 발생한다.")
    public void 저장되어_있지_않은_아이디로_작품_전체_조회시_예외_발생() throws Exception {
        //given
        given(artsRepository.findById(any()))
                .willThrow(ArtsNotFoundException.class);

        //when //then
        assertThatThrownBy(() -> detailsService.findArt(2L))
                .isInstanceOf(ArtsNotFoundException.class);
    }

    @Test
    @DisplayName("details에 저장되어 있지 않은 arts로 조회하려고 하면 예외가 발생한다.")
    public void 저장되어_있지_않은_작품으로_작품_전체_조회시_예외_발생() throws Exception {
        //given
        Arts arts = 작품();

        given(artsRepository.findById(any()))
                .willReturn(Optional.of(arts));
        given(detailsRepository.findByArts(arts))
                .willThrow(DetailsNotFoundException.class);

        //when //then
        assertThatThrownBy(() -> detailsService.findArt(2L))
                .isInstanceOf(DetailsNotFoundException.class);
    }

    @Test
    @DisplayName("맵 화면에서 작품 정보를 조회한다.")
    public void 맵_작품_정보_조회() throws Exception {
        //given
        Arts arts = 작품();
        Details details = 작품_세부정보(arts);

        given(artsRepository.findById(any()))
                .willReturn(Optional.of(arts));
        given(detailsRepository.findByArts(arts))
                .willReturn(Optional.of(details));

        //when
        ArtMapResponse artMapResponse = detailsService.findArtOnMap(2L);

        //then
        assertAll(
                () -> assertThat(artMapResponse.getAuthorName()).isEqualTo(details.getAuthorName()),
                () -> assertThat(artMapResponse.getInfo()).isEqualTo(details.getInfo()),
                () -> assertThat(artMapResponse.getArtName()).isEqualTo(arts.getArtName()),
                () -> assertThat(artMapResponse.getRoadAddress()).isEqualTo(arts.getAddress().getRoadAddress())
        );
    }

    @Test
    @DisplayName("저장되어 있지 않은 arts 아이디로 맵 화면 작품 조회시 예외가 발생한다.")
    public void 저장되어_있지_않은_아이디로_맵_화면_작품_조회시_예외_발생() throws Exception {
        //given
        given(artsRepository.findById(any()))
                .willThrow(ArtsNotFoundException.class);
        //when //then
        assertThatThrownBy(() -> detailsService.findArtOnMap(2L))
                .isInstanceOf(ArtsNotFoundException.class);
    }
}