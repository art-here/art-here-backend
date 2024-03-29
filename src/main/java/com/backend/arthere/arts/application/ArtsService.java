package com.backend.arthere.arts.application;

import com.amazonaws.services.s3.AmazonS3;
import com.backend.arthere.arts.domain.ArtsRepository;
import com.backend.arthere.arts.dto.request.ArtImageByAddressRequest;
import com.backend.arthere.arts.dto.request.ArtImageByArtNameRequest;
import com.backend.arthere.arts.dto.request.ArtImageByLocationRequest;
import com.backend.arthere.arts.dto.request.ArtImageByRevisionDateRequest;
import com.backend.arthere.arts.dto.response.*;
import com.backend.arthere.arts.util.LocationUtils;
import com.backend.arthere.image.util.PresignedURLUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArtsService {

    private final ArtsRepository artsRepository;

    private final PresignedURLUtils presignedURLUtils;

    private final LocationUtils locationUtils;

    private final AmazonS3 adminS3Client;

    private final String adminBucketName;


    public ArtImageByRevisionDateResponse findArtImageByRevisionDate(ArtImageByRevisionDateRequest request) {

        Long id = null;
        LocalDateTime next = null;
        List<ArtImageResponse> artImageResponses = artsRepository.findArtImageByRevisionDate(request);

        Boolean hasNext = hasNext(artImageResponses, request.getLimit() + 1);
        if (hasNext) {
            id = artImageResponses.get(artImageResponses.size() - 1).getId();
            next = artsRepository.findRevisionDateById(id).get(0);
        }

        createImageSharePresignedURLByImageURL(artImageResponses);

        return new ArtImageByRevisionDateResponse(artImageResponses, id, next, hasNext);
    }

    public List<ArtImageByLocationResponse> findArtImageByLocation(ArtImageByLocationRequest request) {

        LocationRangeResponse locationRangeResponse = locationUtils.getLocationRange(request);

        List<ArtImageByLocationResponse> artImageResponses = artsRepository.findArtImageByLocation(locationRangeResponse);
        locationUtils.removeIncorrectLocation(request, artImageResponses);

        createImageBylocationSharePresignedURLByImageURL(artImageResponses);

        return artImageResponses;
    }

    public ArtImageByAddressResponse searchArtImageByAddress(final ArtImageByAddressRequest request) {

        List<ArtImageResponse> artImageResponses = artsRepository.findArtImageByAddress(request);

        Boolean hasNext = hasNext(artImageResponses, request.getLimit() + 1);
        Long nextIdx = null;
        if (hasNext) {
            nextIdx = artImageResponses.get(artImageResponses.size() - 1).getId();
        }
        createImageSharePresignedURLByImageURL(artImageResponses);
        return new ArtImageByAddressResponse(artImageResponses, hasNext, nextIdx);

    }

    public ArtImageByArtNameResponse searchArtImageByArtName(ArtImageByArtNameRequest request) {

        Long nextId = null;
        List<ArtImageResponse> artImageResponses = artsRepository.findArtImageByArtName(request);

        Boolean hasNext = hasNext(artImageResponses, request.getLimit() + 1);
        if (hasNext) {
            nextId = artImageResponses.get(artImageResponses.size() - 1).getId();
        }

        createImageSharePresignedURLByImageURL(artImageResponses);

        return new ArtImageByArtNameResponse(artImageResponses, nextId, hasNext);
    }

    private void createImageSharePresignedURLByImageURL(List<ArtImageResponse> artImageResponses) {

        for (ArtImageResponse artImageRespons : artImageResponses) {

            String presignedURL = presignedURLUtils.createImageShareURL(artImageRespons.getImageURL(),
                    adminS3Client, adminBucketName);
            artImageRespons.setImageURL(presignedURL);
        }
    }

    private void createImageBylocationSharePresignedURLByImageURL(List<ArtImageByLocationResponse> artImageResponses) {

        for (ArtImageByLocationResponse artImageRespons : artImageResponses) {

            String presignedURL = presignedURLUtils.createImageShareURL(artImageRespons.getImageURL(),
                    adminS3Client, adminBucketName);
            artImageRespons.setImageURL(presignedURL);
        }
    }

    private Boolean hasNext(List<ArtImageResponse> artImageResponses, int size) {
        if (artImageResponses.size() == size) {
            artImageResponses.remove(size - 1);
            return true;
        }
        return false;
    }

}