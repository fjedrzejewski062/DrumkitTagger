package com.example.DrumkitTagger.controller;

import com.example.DrumkitTagger.entity.DrumKit;
import com.example.DrumkitTagger.service.DrumKitService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/drumkits")
public class DrumKitController {

    private final DrumKitService drumKitService;

    public DrumKitController(DrumKitService drumKitService) {
        this.drumKitService = drumKitService;
    }

    /** Upload + tagging drumkitu */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadDrumKit(@RequestParam("file") MultipartFile file,
                                           @RequestParam("buyerId") Long buyerId,
                                           @RequestParam("userId") Long userId) {
        try {
            DrumKit drumKit = drumKitService.uploadAndTagDrumKit(file, buyerId, userId);
            return ResponseEntity.ok(drumKit);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    java.util.Map.of("error", e.getMessage())
            );
        }
    }

    /** Pobranie wszystkich drumkitów */
    @GetMapping
    public List<DrumKit> getAllDrumKits() {
        return drumKitService.getAllDrumKits();
    }

    /** Pobranie drumkitu po ID */
    @GetMapping("/{id}")
    public ResponseEntity<?> getDrumKitById(@PathVariable Long id) {
        Optional<DrumKit> drumKit = drumKitService.getDrumKitById(id);
        return drumKit
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /** Pobranie otagowanego pliku */
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadTaggedFile(@PathVariable Long id) {
        Optional<DrumKit> drumKitOpt = drumKitService.getDrumKitById(id);
        if (drumKitOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        DrumKit drumKit = drumKitOpt.get();
        File taggedFile = new File(drumKit.getTaggedPath());
        if (!taggedFile.exists()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(taggedFile);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + taggedFile.getName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    /** Usunięcie drumkitu */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDrumKit(@PathVariable Long id) {
        try {
            drumKitService.deleteDrumKit(id);
            return ResponseEntity.ok(java.util.Map.of("message", "DrumKit deleted successfully"));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(java.util.Map.of("error", e.getMessage()));
        }
    }
}
