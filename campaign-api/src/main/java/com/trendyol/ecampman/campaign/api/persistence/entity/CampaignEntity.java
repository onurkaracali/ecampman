package com.trendyol.ecampman.campaign.api.persistence.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CAMPAIGNS")
public class CampaignEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CAMPAIGN_ID")
    private Long id;

    @Column(name = "PRODUCT_ID", nullable = false)
    private Long poductId;

    @Enumerated(EnumType.STRING)
    @Column(name = "CAMPAIGN_TYPE", nullable = false)
    private CampaignType campaignType;

    @Column(name = "DISCOUNT", nullable = false)
    private Long discount;

    @Column(name = "MAX_DISCOUNT_RATE")
    private Integer maxDiscountRate;
}
