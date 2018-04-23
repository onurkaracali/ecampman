package com.trendyol.ecampman.campaign.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "campaign")
public class CampaignApiProperties {

    private int maxDiscountValueForRateCampaign;
}
