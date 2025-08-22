package com.example.DrumkitTagger.repository;

import com.example.DrumkitTagger.entity.DrumKit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface DrumKitRepository extends JpaRepository<DrumKit, Long>, JpaSpecificationExecutor<DrumKit> {
    // Szukanie po ścieżkach
    Optional<DrumKit> findByOriginalPath(String originalPath);
    Optional<DrumKit> findByTaggedPath(String taggedPath);

    // Szukanie po buyerTag (czyli np. nick kupującego/kod)
    List<DrumKit> findByBuyerTag(String buyerTag);

    // Wszystkie drumkity wrzucone przez danego usera
    List<DrumKit> findByUploadedById(Long userId);

    // Wszystkie drumkity przypisane do danego buyera
    List<DrumKit> findByBuyerId(Long buyerId);

    // Walidacja przydatna przy dodawaniu plików
    boolean existsByOriginalPath(String originalPath);
    boolean existsByTaggedPath(String taggedPath);
}