package com.trendyol.ecampman.campaign.api.controller;

import com.trendyol.ecampman.campaign.api.model.CampaignListResponse;
import com.trendyol.ecampman.campaign.api.model.CampaignView;
import com.trendyol.ecampman.campaign.api.exception.CampaignNotFoundException;
import com.trendyol.ecampman.campaign.api.persistence.entity.Campaign;
import com.trendyol.ecampman.campaign.api.persistence.entity.CampaignTargetType;
import com.trendyol.ecampman.campaign.api.persistence.entity.CampaignType;
import com.trendyol.ecampman.campaign.api.service.CampaignService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/campaigns")
@Log
public class CampaignController {

    private final CampaignService campaignService;

    @Autowired
    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public CampaignListResponse getAllCampaigns() {
        List<Campaign> allCampaigns = campaignService.getAllCampaigns();

        if (CollectionUtils.isEmpty(allCampaigns)) {
            return new CampaignListResponse(Collections.emptyList());
        }

        List<CampaignView> collect = allCampaigns.stream()
                .map(campaign -> mapCampaignView(campaign))
                .collect(Collectors.toList());
        return new CampaignListResponse(collect);
    }

    @RequestMapping(value = "/{campaignId}", method = RequestMethod.GET, produces = "application/json")
    public CampaignView getCampaignById(@PathVariable Long campaignId) {
        Optional<Campaign> campaignFromDB = campaignService.getCampaignById(campaignId);
        if (!campaignFromDB.isPresent()) {
            throw new CampaignNotFoundException();
        }
        return mapCampaignView(campaignFromDB.get());
    }

    @RequestMapping(
            value = "/{campaignId}",
            method = RequestMethod.PUT,
            produces = "application/json",
            consumes = "application/json")
    public CampaignView updateCampaign(@PathVariable Long campaignId,
                                       @RequestBody CampaignView campaignView) {

        Campaign campaignObj = mapCampaign(campaignView);
        return mapCampaignView(campaignService.updateCampaign(campaignObj));
    }

    @RequestMapping(method = RequestMethod.POST,
            produces = "application/json", consumes = "application/json")
    public void saveNewCampaign(
            @RequestBody CampaignView campaignView) {

        Campaign campaignObj = mapCampaign(campaignView);
        campaignService.saveNewCampaign(campaignObj);
    }

    @RequestMapping(value = "/{campaignId}", method = RequestMethod.DELETE)
    public void deleteCampaign(@PathVariable Long campaignId) {
        log.info("Campaign {} is being deleted..");
        campaignService.deleteCampaign(campaignId);
        log.info("Campaign {} deleted..");
    }

    private CampaignView mapCampaignView(Campaign campaign) {
        return CampaignView.builder()
                .id(campaign.getId())
                .targetType(campaign.getTargetType().name())
                .targetId(campaign.getTargetId())
                .discount(campaign.getDiscount())
                .campaignType(campaign.getCampaignType().name())
                .maxDiscountAmount(campaign.getMaxDiscountAmount())
                .build();
    }

    private Campaign mapCampaign(CampaignView campaign) {
        return Campaign.builder()
                .id(campaign.getId())
                .targetType(CampaignTargetType.valueOf(campaign.getTargetType()))
                .targetId(campaign.getTargetId())
                .discount(campaign.getDiscount())
                .campaignType(CampaignType.valueOf(campaign.getCampaignType()))
                .maxDiscountAmount(campaign.getMaxDiscountAmount())
                .build();
    }
}
