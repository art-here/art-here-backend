package com.backend.arthere.details.presentation;

import com.backend.arthere.arts.domain.Arts;
import com.backend.arthere.details.application.DetailsService;
import com.backend.arthere.details.domain.Details;
import com.backend.arthere.details.dto.request.ArtRequest;
import com.backend.arthere.details.dto.response.ArtMapResponse;
import com.backend.arthere.details.dto.response.ArtResponse;
import com.backend.arthere.details.dto.response.ArtSaveResponse;
import com.backend.arthere.details.exception.DetailsNotFoundException;
import com.backend.arthere.global.BaseControllerTest;
import com.backend.arthere.global.TestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import static com.backend.arthere.fixture.EntireArtsFixtures.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({TestConfig.class})
@WebMvcTest(DetailsController.class)
class DetailsControllerTest extends BaseControllerTest {

    @MockBean
    protected DetailsService detailsService;
    @Autowired
    private ObjectMapper objectMapper;
    private final String accessToken = "Bearer testAccess";

    @Test
    @DisplayName("???????????? ?????? ????????? ????????????.")
    @WithMockUser(roles = "ADMIN")
    public void ????????????_??????_??????_??????() throws Exception {
        //given
        ArtRequest artRequest = ??????_??????_??????();
        ArtSaveResponse artSaveResponse = new ArtSaveResponse(2L, artRequest.getArtName());

        given(detailsService.save(any()))
                .willReturn(artSaveResponse);
        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/admin/art")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .content(objectMapper.writeValueAsString(artRequest))
        );
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("api/admin/art",
                                requestHeaders(
                                        headerWithName("Authorization").description("????????? ??????")
                                ),
                                requestFields(
                                        fieldWithPath("artName").type(JsonFieldType.STRING)
                                                .description("?????????"),
                                        fieldWithPath("imageURL").type(JsonFieldType.STRING)
                                                .description("????????? ??????"),
                                        fieldWithPath("latitude").type(JsonFieldType.NUMBER)
                                                .description("??????"),
                                        fieldWithPath("longitude").type(JsonFieldType.NUMBER)
                                                .description("??????"),
                                        fieldWithPath("roadAddress").type(JsonFieldType.STRING)
                                                .description("????????? ??????"),
                                        fieldWithPath("oldAddress").type(JsonFieldType.STRING)
                                                .description("??? ??????"),
                                        fieldWithPath("category").type(JsonFieldType.STRING)
                                                .description("???????????? (??????, ??????, ??????, ??????, ??????, ??????, ?????????, ??????)"),
                                        fieldWithPath("authorName").type(JsonFieldType.STRING)
                                                .description("?????? ??????"),
                                        fieldWithPath("agency").type(JsonFieldType.STRING)
                                                .description("????????????"),
                                        fieldWithPath("info").type(JsonFieldType.STRING)
                                                .description("?????? ?????? (255??? ??????)"),
                                        fieldWithPath("startDate").type(JsonFieldType.STRING)
                                                .description("????????? (yyyy-MM-dd)"),
                                        fieldWithPath("endDate").type(JsonFieldType.STRING)
                                                .description("????????? (???????????? ???????????? ????????????.) (yyyy-MM-dd)")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER)
                                                .description("?????? ?????????"),
                                        fieldWithPath("artName").type(JsonFieldType.STRING)
                                                .description("?????????")
                                )
                        ));
    }

    @Test
    @DisplayName("????????? ?????? ????????? ??????????????? ??? ??? 403 ????????? ????????????.")
    @WithMockUser(roles = "USER")
    public void ?????????_??????_??????_??????_?????????_??????_??????() throws Exception {
        //given
        ArtRequest artRequest = ??????_??????_??????();

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/admin/art")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .content(objectMapper.writeValueAsString(artRequest))
        );
        //then
        resultActions.andExpect(status().isForbidden())
                .andDo(print())
                .andDo(
                        document("api/admin/art/forbidden")
                );
    }

    @Test
    @DisplayName("?????? ???????????? ???????????? ?????? ?????? ????????? ???????????? 400 ????????? ????????????.")
    @WithMockUser(roles = "ADMIN")
    public void ??????_????????????_????????????_??????_??????_??????_?????????_??????_??????() throws Exception {
        //given
        ArtRequest artRequest = new ArtRequest();

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/admin/art")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .content(objectMapper.writeValueAsString(artRequest))
        );
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(
                        document("api/admin/art/invalid")
                );
    }

    @Test
    @DisplayName("?????? ?????? ????????? ????????????.")
    @WithMockUser
    public void ??????_??????_??????_??????() throws Exception {
        //given
        Arts arts = ??????();
        Details details = ??????_????????????(arts);
        ArtResponse artResponse = new ArtResponse(details, arts);

        given(detailsService.findArt(2L))
                .willReturn(artResponse);
        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/art")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", "2")
        );
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("api/art",
                                requestParameters(
                                        parameterWithName("id").description("?????? ?????????")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER)
                                                        .description("?????? ?????? ?????? ????????? (?????? ???????????? ??????)"),
                                        fieldWithPath("roadAddress").type(JsonFieldType.STRING)
                                                .description("????????? ??????"),
                                        fieldWithPath("oldAddress").type(JsonFieldType.STRING)
                                                .description("??? ??????"),
                                        fieldWithPath("category").type(JsonFieldType.STRING)
                                                .description("???????????? (??????, ??????, ??????, ??????, ??????, ??????, ?????????, ??????)"),
                                        fieldWithPath("authorName").type(JsonFieldType.STRING)
                                                .description("?????? ??????"),
                                        fieldWithPath("agency").type(JsonFieldType.STRING)
                                                .description("????????????"),
                                        fieldWithPath("info").type(JsonFieldType.STRING)
                                                .description("?????? ??????"),
                                        fieldWithPath("state").type(JsonFieldType.BOOLEAN)
                                                .description("?????? (?????? ????????? ??????)"),
                                        fieldWithPath("startDate").type(JsonFieldType.STRING)
                                                .description("????????? (yyyy-MM-dd)"),
                                        fieldWithPath("endDate").type(JsonFieldType.STRING)
                                                .description("????????? (yyyy-MM-dd)")
                                )
                        ));
    }

    @Test
    @DisplayName("???????????? ?????? ?????? arts ???????????? ?????? ?????? ????????? 404 ????????? ????????????.")
    @WithMockUser
    public void ????????????_??????_??????_????????????_??????_??????_?????????_??????_??????() throws Exception {
        //given
        given(detailsService.findArt(2L))
                .willThrow(new DetailsNotFoundException());

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/art")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", "2")
        );
        //then
        resultActions.andExpect(status().isNotFound())
                .andDo(print())
                .andDo(
                        document("api/art/notFound")
                );
    }

    @Test
    @DisplayName("??? ???????????? ?????? ????????? ????????????.")
    @WithMockUser
    public void ???_????????????_??????_??????_??????() throws Exception {
        //given
        ArtMapResponse artMapResponse = ??????_???_??????_??????();

        given(detailsService.findArtOnMap(2L))
                .willReturn(artMapResponse);
        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/art/map")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", "2")
        );
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("api/art/map",
                                requestParameters(
                                        parameterWithName("id").description("?????? ?????????")
                                ),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER)
                                                .description("?????? ?????????"),
                                        fieldWithPath("artName").type(JsonFieldType.STRING)
                                                .description("?????????"),
                                        fieldWithPath("roadAddress").type(JsonFieldType.STRING)
                                                .description("????????? ??????"),
                                        fieldWithPath("authorName").type(JsonFieldType.STRING)
                                                .description("?????? ??????"),
                                        fieldWithPath("agency").type(JsonFieldType.STRING)
                                                .description("????????????"),
                                        fieldWithPath("info").type(JsonFieldType.STRING)
                                                .description("?????? ??????")
                                )
                        ));
    }

    @Test
    @DisplayName("???????????? ?????? ?????? arts ???????????? ??? ?????? ?????? ????????? 404 ????????? ????????????.")
    @WithMockUser
    public void ????????????_??????_??????_????????????_???_??????_??????_?????????_??????_??????() throws Exception {
        //given
        given(detailsService.findArtOnMap(2L))
                .willThrow(new DetailsNotFoundException());
        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/art/map")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", "2")
        );
        //then
        resultActions.andExpect(status().isNotFound())
                .andDo(print())
                .andDo(
                        document("api/art/map/notFound")
                );
    }

    @Test
    @DisplayName("???????????? ?????? ????????? ????????????.")
    @WithMockUser(roles = "ADMIN")
    public void ????????????_??????_??????_??????() throws Exception {
        //given
        ArtRequest artRequest = ??????_??????_??????_??????_??????();
        doNothing().when(detailsService).update(2L, artRequest);

        //when
        ResultActions resultActions = mockMvc.perform(
                patch("/api/admin/art")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .param("id", "2")
                        .content(objectMapper.writeValueAsString(artRequest))
        );
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("api/admin/art/update",
                                requestHeaders(
                                        headerWithName("Authorization").description("????????? ??????")
                                ),
                                requestParameters(
                                        parameterWithName("id").description("?????? ?????????")
                                ),
                                requestFields(
                                        fieldWithPath("artName").type(JsonFieldType.STRING)
                                                .description("?????????"),
                                        fieldWithPath("imageURL").type(JsonFieldType.STRING)
                                                .description("????????? ??????"),
                                        fieldWithPath("latitude").type(JsonFieldType.NUMBER)
                                                .description("??????"),
                                        fieldWithPath("longitude").type(JsonFieldType.NUMBER)
                                                .description("??????"),
                                        fieldWithPath("roadAddress").type(JsonFieldType.STRING)
                                                .description("????????? ??????"),
                                        fieldWithPath("oldAddress").type(JsonFieldType.STRING)
                                                .description("??? ??????"),
                                        fieldWithPath("category").type(JsonFieldType.STRING)
                                                .description("???????????? (??????, ??????, ??????, ??????, ??????, ??????, ?????????, ??????)"),
                                        fieldWithPath("authorName").type(JsonFieldType.STRING)
                                                .description("?????? ??????"),
                                        fieldWithPath("agency").type(JsonFieldType.STRING)
                                                .description("????????????"),
                                        fieldWithPath("info").type(JsonFieldType.STRING)
                                                .description("?????? ?????? (255??? ??????)"),
                                        fieldWithPath("startDate").type(JsonFieldType.STRING)
                                                .description("????????? (yyyy-MM-dd)"),
                                        fieldWithPath("endDate").type(JsonFieldType.STRING)
                                                .description("????????? (???????????? ???????????? ????????????.) (yyyy-MM-dd)")
                                )
                        ));
    }

    @Test
    @DisplayName("????????? ?????? ????????? ??????????????? ??? ??? 403 ????????? ????????????.")
    @WithMockUser(roles = "USER")
    public void ?????????_??????_??????_??????_?????????_??????_??????() throws Exception {
        //given
        ArtRequest artRequest = ??????_??????_??????_??????_??????();
        //when
        ResultActions resultActions = mockMvc.perform(
                patch("/api/admin/art")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .param("id", "2")
                        .content(objectMapper.writeValueAsString(artRequest))
        );
        //then
        resultActions.andExpect(status().isForbidden())
                .andDo(print())
                .andDo(
                        document("api/admin/art/update/forbidden")
                );
    }

    @Test
    @DisplayName("???????????? ?????? ????????? ????????????.")
    @WithMockUser(roles = "ADMIN")
    public void ????????????_??????_??????_??????() throws Exception {
        //given
        //when
        ResultActions resultActions = mockMvc.perform(
                delete("/api/admin/art")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .param("id", "2")
        );
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("api/admin/art/delete",
                                requestHeaders(
                                        headerWithName("Authorization").description("????????? ??????")
                                ),
                                requestParameters(
                                        parameterWithName("id").description("?????? ?????????")
                                )
                        )
                );
    }
    
    @Test
    @DisplayName("???????????? ???????????? ?????? ?????? ?????? ?????? ????????? 404 ????????? ????????????.")
    @WithMockUser(roles = "ADMIN")
    public void ????????????_??????_??????_??????_??????_?????????_??????_??????() throws Exception {
        //given
        doThrow(new DetailsNotFoundException()).when(detailsService).delete(2L);
        //when
        ResultActions resultActions = mockMvc.perform(
                delete("/api/admin/art")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .param("id", "2")
        );
        //then
        resultActions.andExpect(status().isNotFound())
                .andDo(print())
                .andDo(
                        document("api/admin/art/delete/notFound")
                );
    }
}