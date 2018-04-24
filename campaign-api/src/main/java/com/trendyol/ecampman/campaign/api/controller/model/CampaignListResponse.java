package com.trendyol.ecampman.campaign.api.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class CampaignListResponse {
    private List<CampaignView> campaigns;
}
