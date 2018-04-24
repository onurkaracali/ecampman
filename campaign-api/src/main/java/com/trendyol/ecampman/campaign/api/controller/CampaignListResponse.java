package com.trendyol.ecampman.campaign.api.controller;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CampaignListResponse {
    private List<CampaignView> campaigns;
}
