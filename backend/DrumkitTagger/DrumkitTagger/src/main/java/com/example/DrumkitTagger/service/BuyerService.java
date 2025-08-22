package com.example.DrumkitTagger.service;

import com.example.DrumkitTagger.entity.Buyer;
import com.example.DrumkitTagger.entity.User;
import com.example.DrumkitTagger.repository.BuyerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuyerService {

    private final BuyerRepository buyerRepository;

    public BuyerService(BuyerRepository buyerRepository) {
        this.buyerRepository = buyerRepository;
    }

    /** Tworzy nowego Buyera powiązanego z Userem */
    public Buyer createBuyer(User user, Buyer buyer) {
        buyer.setUser(user);
        return buyerRepository.save(buyer);
    }

    /** Pobiera wszystkich Buyerów danego Usera */
    public List<Buyer> getBuyersByUser(User user) {
        return buyerRepository.findByUser(user);
    }

    /** Pobiera Buyera po ID */
    public Optional<Buyer> getBuyerById(Long id) {
        return buyerRepository.findById(id);
    }

    /** Pobiera Buyera po nickname */
    public Optional<Buyer> getBuyerByNickname(String nickname) {
        return buyerRepository.findByNickname(nickname);
    }

    /** Aktualizuje dane Buyera */
    public Buyer updateBuyer(Buyer buyer) {
        return buyerRepository.save(buyer);
    }

    /** Soft delete Buyera */
    public void deleteBuyer(Buyer buyer) {
        // jeśli soft-delete jest potrzebny, np. ustawiamy nickname/tag na DELETED
        buyer.setNickname("DELETED-BUYER-" + buyer.getId());
        buyer.setTag("00000");
        buyerRepository.save(buyer);
    }
}
