package com.trendyol.ecampman.campaign.api.persistence.repository;

import com.trendyol.ecampman.campaign.api.persistence.entity.Campaign;
import com.trendyol.ecampman.campaign.api.persistence.entity.CampaignTargetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {

    Optional<Campaign> findByTargetTypeAndTargetId(CampaignTargetType targetType, long productId);
}
