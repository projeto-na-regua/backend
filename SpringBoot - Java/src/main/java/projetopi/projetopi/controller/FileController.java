package projetopi.projetopi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import projetopi.projetopi.service.ImageService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/files")
public class FileController {
    @Autowired
    private ImageService imageService;

    @PostMapping
    public String upload(@RequestBody MultipartFile file) {
        return imageService.upload(file, "usuario");
    }

    @GetMapping
    public String getImg(@RequestParam String filename) {
        return imageService.getImgURL(filename, "usuario");
    }
}

