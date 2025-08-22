package com.example.DrumkitTagger.repository;

import com.example.DrumkitTagger.entity.Buyer;
import com.example.DrumkitTagger.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface BuyerRepository extends JpaRepository<Buyer, Long>, JpaSpecificationExecutor<Buyer> {
    List<Buyer> findByUser(User user);
    Optional<Buyer> findByNickname(String nickname);
    Optional<Buyer> findByTag(String tag);
    List<Buyer> findByUserId(Long userId);
    boolean existsByNickname(String nickname);
    boolean existsByTag(String tag);
}