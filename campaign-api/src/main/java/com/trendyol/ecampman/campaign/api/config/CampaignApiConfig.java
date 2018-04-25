package com.trendyol.ecampman.campaign.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        basePackages = {"com.trendyol.ecampman.campaign.api.persistence.repository"})
public class CampaignApiConfig {
}
