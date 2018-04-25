package com.trendyol.ecampman.campaign.api.persistence;

import com.trendyol.ecampman.campaign.api.persistence.entity.Campaign;
import com.trendyol.ecampman.campaign.api.persistence.entity.CampaignTargetType;
import com.trendyol.ecampman.campaign.api.persistence.entity.CampaignType;
import com.trendyol.ecampman.campaign.api.persistence.repository.CampaignRepository;
import org.hamcrest.Matchers;
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

    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            statements = {
                    "INSERT INTO CAMPAIGNS(campaign_id, campaign_type, DISCOUNT, max_discount_amount, target_id) " +
                            "VALUES(1, 'AMOUNT', 10, 0, 1);"
            })
    @Test
    public void testFindById() {
        Campaign expected = Campaign.builder()
                .id(1L)
                .campaignType(CampaignType.AMOUNT)
                .targetId(1L)
                .discount(10L)
                .maxDiscountAmount(0L)
                .build();

        Optional<Campaign> returned = campaignRepository.findById(1L);

        assertTrue(returned.isPresent());
        assertThat(returned.get(), Matchers.equalTo(expected));
    }

    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            statements = {
                    "INSERT INTO CAMPAIGNS(campaign_id, campaign_target, campaign_type, DISCOUNT, max_discount_amount, target_id) " +
                            "VALUES(1, 'PRODUCT', 'AMOUNT', 10, 0, 1);"
            })
    @Test
    public void testFindByTargetTypeAndTargetId() {
        Campaign expected = Campaign.builder()
                .id(1L)
                .targetType(CampaignTargetType.PRODUCT)
                .campaignType(CampaignType.AMOUNT)
                .targetId(1L)
                .discount(10L)
                .maxDiscountAmount(0L)
                .build();

        Optional<Campaign> returned = campaignRepository.findByTargetTypeAndTargetId(CampaignTargetType.PRODUCT, 1L);

        assertTrue(returned.isPresent());
        assertThat(returned.get(), Matchers.equalTo(expected));
    }

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
                    "INSERT INTO CAMPAIGNS(campaign_id, campaign_type, DISCOUNT, max_discount_amount, target_id) " +
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
                    "INSERT INTO CAMPAIGNS(campaign_id, campaign_type, DISCOUNT, max_discount_amount, target_id) " +
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
