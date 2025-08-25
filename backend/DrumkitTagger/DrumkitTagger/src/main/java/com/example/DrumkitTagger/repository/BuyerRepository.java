package com.example.DrumkitTagger.repository;

import com.example.DrumkitTagger.entity.Buyer;
import com.example.DrumkitTagger.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface BuyerRepository extends JpaRepository<Buyer, Long>, JpaSpecificationExecutor<Buyer> {

    // Pobiera wszystkich buyerów utworzonych przez danego usera
    List<Buyer> findByCreatedBy(User user);

    // Znajduje Buyera po nickname
    Optional<Buyer> findByNickname(String nickname);

    // Znajduje Buyera po tagu
    Optional<Buyer> findByTag(String tag);

    // Sprawdza czy istnieje Buyera o danym nickname
    boolean existsByNickname(String nickname);

    // Sprawdza czy istnieje Buyera o danym tagu
    boolean existsByTag(String tag);
}
