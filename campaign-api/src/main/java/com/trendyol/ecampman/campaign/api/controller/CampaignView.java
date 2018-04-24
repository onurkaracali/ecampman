package com.trendyol.ecampman.campaign.api.controller;

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
    private String campaignType;
    private Long discount;
    private Integer maxDiscountAmount;
    private LocalDateTime createDateTime;
    private LocalDateTime updateDateTime;
}
