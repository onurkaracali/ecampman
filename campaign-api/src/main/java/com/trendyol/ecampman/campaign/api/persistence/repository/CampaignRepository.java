package com.trendyol.ecampman.campaign.api.persistence.repository;

import com.trendyol.ecampman.campaign.api.persistence.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {
}
