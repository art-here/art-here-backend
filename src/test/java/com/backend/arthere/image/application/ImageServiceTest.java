package com.backend.arthere.image.application;

import com.amazonaws.services.s3.AmazonS3;
import com.backend.arthere.image.dto.ImageResponse;
import com.backend.arthere.image.dto.ImageUploadResponse;
import com.backend.arthere.image.util.PresignedURLUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @InjectMocks
    private ImageService imageService;

    @Mock
    private PresignedURLUtils presignedURLUtils;

    @Test
    void 이미지_공유_URL_생성() {

        //given
        String imageURL = "image/sand.jpg";
        String preSignedURL = "https://art-here-frontend.s3.ap-northeast-2.amazonaws.com/image/sand.jpg?X-Amz-Algorithm";

        given(presignedURLUtils.createImageShareURL(anyString(), any(), any())).willReturn(preSignedURL);


        //when
        ImageResponse response = imageService.createImageSharePresignedURL(imageURL);

        //then
        assertThat(response.getPreSignedURL()).isEqualTo(preSignedURL);
    }

    @Test
    void 이미지_업로드_URL_생성() {

        //given
        String preSignedURL = "https://art-here-frontend.s3.ap-northeast-2.amazonaws.com/image/sand.jpg?X-Amz-Algorithm";

        given(presignedURLUtils.createImageUploadURL(anyString(), any(), any())).willReturn(preSignedURL);

        //when
        ImageUploadResponse response = imageService.createAdminImageUploadPresignedURL();

        //then
        assertThat(response.getKey()).contains("image/", ".jpg");
        assertThat(response.getPreSignedURL()).contains(preSignedURL);
    }

    @Test
    void 이미지_삭제_URL_생성() {

        //given
        String imageURL = "image/sand.jpg";
        String preSignedURL = "https://art-here-frontend.s3.ap-northeast-2.amazonaws.com/image/sand.jpg?X-Amz-Algorithm";

        given(presignedURLUtils.createImageDeleteURL(anyString(), any(), any())).willReturn(preSignedURL);

        //when
        ImageResponse response = imageService.createAdminDeletePresignedURL(imageURL);

        //then
        assertThat(response.getPreSignedURL()).isEqualTo(preSignedURL);
    }
    
    @Test
    public void 게시물_이미지_업로드_URL_생성() throws Exception {
        //given
        String preSignedURL = "testURL";

        given(presignedURLUtils.createImageUploadURL(anyString(), any(), any())).willReturn(preSignedURL);

        //when
        ImageUploadResponse response = imageService.createUserImageUploadPresignedURL();

        //then
        assertThat(response.getKey()).contains("image/", ".jpg");
        assertThat(response.getPreSignedURL()).contains(preSignedURL);
    }
    
    @Test
    public void 게시물_이미지_삭제_URL_생성() throws Exception {
        //given
        String imageURL = "image/test.jpg";
        String preSignedURL = "testURL";

        given(presignedURLUtils.createImageDeleteURL(anyString(), any(), any())).willReturn(preSignedURL);

        //when
        ImageResponse response = imageService.createUsernDeletePresignedURL(imageURL);

        //then
        assertThat(response.getPreSignedURL()).isEqualTo(preSignedURL);
    }
}