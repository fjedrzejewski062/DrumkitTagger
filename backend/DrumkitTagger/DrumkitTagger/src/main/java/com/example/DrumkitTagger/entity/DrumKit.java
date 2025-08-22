package com.example.DrumkitTagger.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "drumkits")
public class DrumKit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Ścieżka do oryginalnego pliku drumkitu (np. /uploads/original/xyz.zip) */
    @NotNull
    @Size(max = 512)
    private String originalPath;

    /** Ścieżka do otagowanego pliku (np. /uploads/tagged/xyz_12345.zip) */
    @NotNull
    @Size(max = 512)
    private String taggedPath;

    /** Nickname kupującego albo jego kod */
    @NotNull
    @Size(min = 3, max = 50)
    private String buyerTag;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User uploadedBy;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "buyer_id", nullable = false)
    private Buyer buyer;

    /* --- Gettery i settery --- */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    public String getTaggedPath() {
        return taggedPath;
    }

    public void setTaggedPath(String taggedPath) {
        this.taggedPath = taggedPath;
    }

    public String getBuyerTag() {
        return buyerTag;
    }

    public void setBuyerTag(String buyerTag) {
        this.buyerTag = buyerTag;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(User uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }
}
