package com.trendyol.ecampman.campaign.api.model;

import com.trendyol.ecampman.campaign.api.model.CampaignView;
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
