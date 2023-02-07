package com.backend.arthere.image.presentation;

import com.backend.arthere.image.application.ImageService;
import com.backend.arthere.image.dto.ImageResponse;
import com.backend.arthere.image.dto.ImageUploadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class ImageController {

    final private ImageService imageService;

    @GetMapping("/image/share")
    public ResponseEntity<?> createAdminUserImageSharePresignedURL(@RequestParam("image") String imageURL) {

        ImageResponse imageResponse = imageService.createAdminUserImageSharePresignedURL(imageURL);
        return ResponseEntity.ok(imageResponse);
    }

    @GetMapping("/admin/image/upload")
    public ResponseEntity<?> createAdminImageUploadPresignedURL() {

        ImageUploadResponse uploadResponse = imageService.createAdminImageUploadPresignedURL();
        return ResponseEntity.ok(uploadResponse);
    }

    @GetMapping("/admin/image/delete")
    public ResponseEntity<?> createAdminDeletePresignedURL(@RequestParam("image") String imageURL) {

        ImageResponse imageResponse = imageService.createAdminDeletePresignedURL(imageURL);
        return ResponseEntity.ok(imageResponse);
    }
}
