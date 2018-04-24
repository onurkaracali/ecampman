package com.trendyol.ecampman.campaign.api.service;

import com.google.common.collect.Lists;
import com.trendyol.ecampman.campaign.api.config.CampaignApiProperties;
import com.trendyol.ecampman.campaign.api.exception.ValidationException;
import com.trendyol.ecampman.campaign.api.persistence.entity.Campaign;
import com.trendyol.ecampman.campaign.api.persistence.entity.CampaignTargetType;
import com.trendyol.ecampman.campaign.api.persistence.entity.CampaignType;
import com.trendyol.ecampman.campaign.api.persistence.repository.CampaignRepository;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CampaignServiceTest {

    @Mock
    private CampaignRepository campaignRepository;

    private CampaignService campaignService;

    @Captor
    private ArgumentCaptor<Campaign> campaignArgumentCaptor;

    @Before
    public void setup() {
        CampaignApiProperties campaignApiProperties = new CampaignApiProperties();
        campaignApiProperties.setMaxDiscountValueForRateCampaign(100);

        campaignService = new CampaignService(campaignRepository, campaignApiProperties);
    }

    @Test
    public void testGetCampaignById_ShouldReturnCampaignInstanceById() {
        Campaign expectedCampaign = Campaign.builder()
                .id(1L)
                .targetType(CampaignTargetType.PRODUCT)
                .campaignType(CampaignType.AMOUNT)
                .targetId(123L)
                .discount(200L)
                .build();

        when(campaignRepository.findById(1L)).thenReturn(Optional.of(expectedCampaign));
        Campaign campaign = campaignService.getCampaignById(1L);

        assertThat(campaign, Matchers.equalTo(expectedCampaign));
    }

    @Test
    public void testGelAllCampaigns_ShouldReturnAllCampaigns() {
        ArrayList<Campaign> expectedCampaigns = Lists.newArrayList(
                Campaign.builder()
                        .id(1L)
                        .targetType(CampaignTargetType.PRODUCT)
                        .campaignType(CampaignType.AMOUNT)
                        .targetId(123L)
                        .discount(200L)
                        .build(),
                Campaign.builder()
                        .id(1L)
                        .targetType(CampaignTargetType.PRODUCT)
                        .campaignType(CampaignType.AMOUNT)
                        .targetId(123L)
                        .discount(200L)
                        .build());

        when(campaignRepository.findAll()).thenReturn(expectedCampaigns);

        List<Campaign> campaigns = campaignService.getAllCampaigns();

        assertThat(expectedCampaigns, equalTo(campaigns));
    }

    @Test
    public void testSave_ShouldSaveCampaignEntityToDB_WithNewCampaign() {
        // Given
        Campaign givenCampaign = Campaign.builder()
                .targetType(CampaignTargetType.PRODUCT)
                .campaignType(CampaignType.AMOUNT)
                .targetId(123L)
                .discount(200L)
                .build();

        // When
        campaignService.saveNewCampaign(givenCampaign);

        // Then
        verify(campaignRepository, times(1)).save(eq(givenCampaign));
    }

    @Test(expected = ValidationException.class)
    public void testSave_ShouldThrowException_WhenRateCampaignAndMaxDiscountAmountIsEmpty() {
        // Given
        Campaign campaignToSave = Campaign.builder()
                .campaignType(CampaignType.RATE)
                .discount(10L)
                .targetId(1L)
                .build();

        // When
        campaignService.saveNewCampaign(campaignToSave);
    }

    @Test(expected = ValidationException.class)
    public void testSave_ShouldThrowException_WhenDiscountIsNotValid() {
        // Given
        Campaign campaignToSave = Campaign.builder()
                .campaignType(CampaignType.RATE)
                .discount(200L)
                .targetId(1L)
                .maxDiscountAmount(2)
                .build();

        // When
        campaignService.saveNewCampaign(campaignToSave);
    }

    @Test
    public void testUpdate_ShouldUpdateCampaignEntity_WithValidCampaign() {
        // Given
        Campaign givenCampaign = Campaign.builder()
                .targetType(CampaignTargetType.PRODUCT)
                .id(1L)
                .targetId(1L)
                .campaignType(CampaignType.RATE)
                .maxDiscountAmount(20)
                .discount(50L)
                .build();

        // This campaign entity will be returned from mock repository
        Campaign originalCampaign = Campaign.builder()
                .targetType(CampaignTargetType.PRODUCT)
                .id(1L)
                .targetId(1L)
                .campaignType(CampaignType.AMOUNT)
                .discount(100L)
                .build();

        // When
        when(campaignRepository.findById(1L)).thenReturn(Optional.of(originalCampaign));

        campaignService.updateCampaign(givenCampaign);

        // Then
        verify(campaignRepository).save(campaignArgumentCaptor.capture());
        Campaign capturedCampaign = campaignArgumentCaptor.getValue();

        assertThat(capturedCampaign.getDiscount(), is(50L));
        assertThat(capturedCampaign.getCampaignType(), is(CampaignType.RATE));
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_ShouldThrowException_WhenCampaignIdNotFound() {
        // Given
        Campaign updatingCampaign = Campaign.builder()
                .id(1L)
                .targetId(1L)
                .campaignType(CampaignType.AMOUNT)
                .discount(50L)
                .build();

        // When
        when(campaignRepository.findById(1L)).thenReturn(Optional.empty());

        // update operation
        campaignService.updateCampaign(updatingCampaign);
    }

    @Test(expected = ValidationException.class)
    public void testUpdate_ShouldThrowException_WhenNewCampaignParamsInvalid() {
        // Given
        Campaign invalidCampaign = Campaign.builder()
                .id(1L)
                .targetId(1L)
                .campaignType(CampaignType.RATE) // Campaign type rate and maxDiscountAmount is empty
                .discount(50L)
                .build();

        Campaign originalEntity = Campaign.builder()
                .id(1L)
                .targetId(1L)
                .discount(50L)
                .build();

        // When
        when(campaignRepository.findById(1L)).thenReturn(Optional.of(originalEntity));

        // update operation
        campaignService.updateCampaign(invalidCampaign);
    }

    @Test
    public void testDelete_ShouldDeleteGivenCampaign() {
        // Given
        Campaign campaignToDelete = Campaign.builder()
                .id(1L)
                .targetId(1L)
                .campaignType(CampaignType.AMOUNT)
                .discount(50L)
                .build();

        // When
        campaignService.deleteCampaign(campaignToDelete);

        verify(campaignRepository).delete(campaignToDelete);
    }
}
