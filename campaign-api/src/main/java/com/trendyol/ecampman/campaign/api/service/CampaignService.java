package com.trendyol.ecampman.campaign.api.service;

import com.trendyol.ecampman.campaign.api.config.CampaignApiProperties;
import com.trendyol.ecampman.campaign.api.exception.CampaignNotFoundException;
import com.trendyol.ecampman.campaign.api.exception.ValidationException;
import com.trendyol.ecampman.campaign.api.persistence.entity.Campaign;
import com.trendyol.ecampman.campaign.api.persistence.entity.CampaignType;
import com.trendyol.ecampman.campaign.api.persistence.repository.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final CampaignApiProperties campaignApiProperties;

    @Autowired
    public CampaignService(CampaignRepository campaignRepository,
                           CampaignApiProperties campaignApiProperties) {
        this.campaignRepository = campaignRepository;
        this.campaignApiProperties = campaignApiProperties;
    }

    public Campaign saveNewCampaign(Campaign campaign) {
        campaign.setCreateDateTime(LocalDateTime.now());
        return saveCampaign(campaign);
    }

    public Campaign updateCampaign(Campaign campaign) {
        Optional<Campaign> campaignEntityOpt = campaignRepository.findById(campaign.getId());
        if (!campaignEntityOpt.isPresent()) {
            throw new ValidationException("Campaign could not be found with given id!");
        }

        Campaign originalCampaign = campaignEntityOpt.get();
        originalCampaign.setCampaignType(campaign.getCampaignType());
        originalCampaign.setTargetType(campaign.getTargetType());
        originalCampaign.setTargetId(campaign.getTargetId());
        originalCampaign.setDiscount(campaign.getDiscount());
        originalCampaign.setMaxDiscountAmount(campaign.getMaxDiscountAmount());
        return saveCampaign(originalCampaign);
    }

    private Campaign saveCampaign(Campaign campaign) {
        if (!isCampaignValid(campaign)) {
            throw new ValidationException("Campaign is not valid!");
        }

        campaign.setUpdateDateTime(LocalDateTime.now());
        return campaignRepository.save(campaign);
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

    public Campaign getCampaignById(long campaignId) {
        Optional<Campaign> byId = campaignRepository.findById(campaignId);
        if (!byId.isPresent()) {
            throw new CampaignNotFoundException();
        }
        return byId.get();
    }

    public List<Campaign> getAllCampaigns() {
        return campaignRepository.findAll();
    }
}
