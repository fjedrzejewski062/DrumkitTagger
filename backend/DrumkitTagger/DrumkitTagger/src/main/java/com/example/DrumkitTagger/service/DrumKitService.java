package com.example.DrumkitTagger.service;

import com.example.DrumkitTagger.entity.Buyer;
import com.example.DrumkitTagger.entity.DrumKit;
import com.example.DrumkitTagger.entity.User;
import com.example.DrumkitTagger.repository.BuyerRepository;
import com.example.DrumkitTagger.repository.DrumKitRepository;
import com.example.DrumkitTagger.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class DrumKitService {

    private final DrumKitRepository drumKitRepository;
    private final BuyerRepository buyerRepository;
    private final UserRepository userRepository;

    private final String uploadDir = "uploads/original/";
    private final String taggedDir = "uploads/tagged/";

    public DrumKitService(DrumKitRepository drumKitRepository,
                          BuyerRepository buyerRepository,
                          UserRepository userRepository) {
        this.drumKitRepository = drumKitRepository;
        this.buyerRepository = buyerRepository;
        this.userRepository = userRepository;
    }

    /**
     * Zapisuje oryginalny drumkit, tworzy jego otagowaną wersję i zapisuje w DB
     */
    public DrumKit uploadAndTagDrumKit(MultipartFile file, Long buyerId, Long userId) throws IOException {
        Optional<Buyer> buyerOpt = buyerRepository.findById(buyerId);
        Optional<User> userOpt = userRepository.findById(userId);

        if (buyerOpt.isEmpty() || userOpt.isEmpty()) {
            throw new IllegalArgumentException("Nie znaleziono użytkownika lub kupującego");
        }

        Buyer buyer = buyerOpt.get();
        User user = userOpt.get();

        // zapisz oryginalny plik
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null) throw new IllegalArgumentException("Plik bez nazwy");

        Path originalPath = Paths.get(uploadDir + originalFileName);
        Files.createDirectories(originalPath.getParent());
        file.transferTo(originalPath.toFile());

        // wygeneruj 5-cyfrowy tag
        String tag = String.format("%05d", new Random().nextInt(100000));

        // stwórz nową nazwę pliku otagowanego
        String taggedFileName = addTagToFileName(originalFileName, tag);
        Path taggedPath = Paths.get(taggedDir + taggedFileName);
        Files.createDirectories(taggedPath.getParent());

        // kopiuj plik do folderu tagged
        Files.copy(originalPath, taggedPath);

        // utwórz encję DrumKit
        DrumKit drumKit = new DrumKit();
        drumKit.setOriginalPath(originalPath.toString());
        drumKit.setTaggedPath(taggedPath.toString());
        drumKit.setBuyerTag(buyer.getTag());
        drumKit.setBuyer(buyer);
        drumKit.setUploadedBy(user);

        return drumKitRepository.save(drumKit);
    }

    /**
     * Dodaje tag do nazwy pliku przed rozszerzeniem
     * np. "808.wav" + "12345" -> "808 - 12345.wav"
     */
    private String addTagToFileName(String originalName, String tag) {
        int dotIndex = originalName.lastIndexOf('.');
        if (dotIndex == -1) return originalName + " - " + tag;
        return originalName.substring(0, dotIndex) + " - " + tag + originalName.substring(dotIndex);
    }

    public List<DrumKit> getAllDrumKits() {
        return drumKitRepository.findAll();
    }

    public Optional<DrumKit> getDrumKitById(Long id) {
        return drumKitRepository.findById(id);
    }

    public void deleteDrumKit(Long id) throws IOException {
        Optional<DrumKit> drumKitOpt = drumKitRepository.findById(id);
        if (drumKitOpt.isPresent()) {
            DrumKit drumKit = drumKitOpt.get();
            // usuń pliki z dysku
            Files.deleteIfExists(Path.of(drumKit.getOriginalPath()));
            Files.deleteIfExists(Path.of(drumKit.getTaggedPath()));
            drumKitRepository.delete(drumKit);
        }
    }
}
