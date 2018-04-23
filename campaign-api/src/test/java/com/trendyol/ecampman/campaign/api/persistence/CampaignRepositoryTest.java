package com.trendyol.ecampman.campaign.api.persistence;

import com.trendyol.ecampman.campaign.api.persistence.entity.Campaign;
import com.trendyol.ecampman.campaign.api.persistence.entity.CampaignType;
import com.trendyol.ecampman.campaign.api.persistence.repository.CampaignRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
@TestPropertySource(locations = {"classpath:application-test.yml"})
public class CampaignRepositoryTest {

    @Autowired
    private CampaignRepository campaignRepository;

    @Test
    public void testSaveNewCampaign() {
        // Given
        Campaign newCampaign = Campaign.builder()
                .campaignType(CampaignType.AMOUNT)
                .targetId(1L)
                .discount(10L)
                .build();

        // When
        Campaign save = campaignRepository.save(newCampaign);
        List<Campaign> allCampaigns = campaignRepository.findAll();

        // Then
        assertNotNull(save);
        assertTrue(allCampaigns.size() > 0);
    }

    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            statements = {
                    "INSERT INTO CAMPAIGNS(campaign_id, campaign_type, discount, max_discount_amount, target_id) " +
                            "VALUES(1, 'AMOUNT', 10, 0, 1);"
            })
    @Test
    public void testUpdateCampaign() {
        // Given
        Optional<Campaign> targetCampaign = campaignRepository.findById(1L);
        Campaign campaign = targetCampaign.get();

        // When
        campaign.setDiscount(100L);
        Campaign savedEntity = campaignRepository.save(campaign);

        // Then
        assertNotNull(savedEntity);
    }

    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            statements = {
                    "INSERT INTO CAMPAIGNS(campaign_id, campaign_type, discount, max_discount_amount, target_id) " +
                            "VALUES(1, 'AMOUNT', 10, 0, 1);"
            })
    @Test
    public void testDeleteCampaign() {
        // Given
        Optional<Campaign> targetCampaign = campaignRepository.findById(1L);
        Campaign campaign = targetCampaign.get();

        // When
        campaignRepository.delete(campaign);

        // Then
        Optional<Campaign> deletedEntity = campaignRepository.findById(1L);
        if(deletedEntity.isPresent()) {
            fail("Deletion failed");
        }
    }
}
