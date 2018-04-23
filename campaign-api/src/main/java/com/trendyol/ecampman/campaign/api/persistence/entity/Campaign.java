package com.trendyol.ecampman.campaign.api.persistence.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CAMPAIGNS")
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CAMPAIGN_ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "CAMPAIGN_TARGET")
    private CampaignTargetType targetType;

    @Column(name = "TARGET_ID", nullable = false)
    private Long targetId;

    @Enumerated(EnumType.STRING)
    @Column(name = "CAMPAIGN_TYPE", nullable = false)
    private CampaignType campaignType;

    @Column(name = "DISCOUNT", nullable = false)
    private Long discount;

    @Column(name = "MAX_DISCOUNT_AMOUNT")
    private Integer maxDiscountAmount;

    @Column(name = "CREATE_DATE")
    private LocalDateTime createDateTime;

    @Column(name = "UPDATE_DATE")
    private LocalDateTime updateDateTime;
}
