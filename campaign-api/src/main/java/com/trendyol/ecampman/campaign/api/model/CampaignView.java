package com.trendyol.ecampman.campaign.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CampaignView {
    private Long id;
    private String targetType;
    private Long targetId;
    private String campaignName;
    private String campaignType;
    private Long discount;
    private Long maxDiscountAmount;
}
