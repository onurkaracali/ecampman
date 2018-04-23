package com.trendyol.ecampman.campaign.api.service;

import com.trendyol.ecampman.campaign.api.config.CampaignApiProperties;
import com.trendyol.ecampman.campaign.api.persistence.entity.Campaign;
import com.trendyol.ecampman.campaign.api.persistence.entity.CampaignType;
import com.trendyol.ecampman.campaign.api.persistence.repository.CampaignRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final CampaignApiProperties campaignApiProperties;

    public void saveNewCampaign(Campaign campaign) {
        campaign.setCreateDateTime(LocalDateTime.now());
        saveCampaign(campaign);
    }

    public void updateCampaign(Campaign campaign) {
        Optional<Campaign> campaignEntityOpt = campaignRepository.findById(campaign.getId());
        if (!campaignEntityOpt.isPresent()) {
            throw new RuntimeException("Campaign could not be found with given id!");
        }

        Campaign originalCampaign = campaignEntityOpt.get();
        originalCampaign.setCampaignType(campaign.getCampaignType());
        originalCampaign.setTargetType(campaign.getTargetType());
        originalCampaign.setTargetId(campaign.getTargetId());
        originalCampaign.setDiscount(campaign.getDiscount());
        originalCampaign.setMaxDiscountAmount(campaign.getMaxDiscountAmount());
        saveCampaign(originalCampaign);
    }

    private void saveCampaign(Campaign campaign) {
        if (!isCampaignValid(campaign)) {
            throw new RuntimeException("Campaign is not valid!");
        }

        campaign.setUpdateDateTime(LocalDateTime.now());
        campaignRepository.save(campaign);
    }

    public boolean isCampaignValid(Campaign campaign) {
        if (CampaignType.RATE.equals(campaign.getCampaignType())) {
            boolean maxDiscAmtNotEmpty = campaign.getMaxDiscountAmount() != null
                    && campaign.getMaxDiscountAmount() > 0;

            boolean discountInRange = campaign.getDiscount() < campaignApiProperties.getMaxDiscountValueForRateCampaign();
            return maxDiscAmtNotEmpty && discountInRange;
        }
        return true;
    }

    public void deleteCampaign(Campaign campaignToDelete) {
        campaignRepository.delete(campaignToDelete);
    }
}
