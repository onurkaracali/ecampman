package com.trendyol.ecampman.campaign.api.service;

import com.google.common.collect.Lists;
import com.trendyol.ecampman.campaign.api.model.Cart;
import com.trendyol.ecampman.campaign.api.model.CartItem;
import com.trendyol.ecampman.campaign.api.persistence.entity.Campaign;
import com.trendyol.ecampman.campaign.api.persistence.entity.CampaignTargetType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Comparator.*;

@Service
@RequiredArgsConstructor
public class CartDiscountManager {

    private final CampaignService campaignService;

    public Cart processCart(Cart cart) {
        Cart calculatedCart = new Cart(Lists.newArrayList());
        List<CartItem> itemsToProcess = cart.getItems();

        itemsToProcess.forEach((CartItem currentItem) -> {
            Long categoryId = currentItem.getCategoryId();
            long discountedPrice = currentItem.getPrice();

            Optional<Campaign> categoryCampaign = findCampaignByCategoryId(categoryId);

            if (categoryCampaign.isPresent()) {
                CartItem maxPriceItem = findMaxPriceItemInCart(itemsToProcess, categoryId);
                if (maxPriceItem.equals(currentItem)) {
                     discountedPrice = DiscountCalculator.setup(currentItem, categoryCampaign.get())
                            .calculateDiscountedPrice();
                }
            }
            else {
                Optional<Campaign> productCampaign = findCampaignByProductId(currentItem);
                if (productCampaign.isPresent()) {
                    discountedPrice = DiscountCalculator.setup(currentItem, productCampaign.get())
                            .calculateDiscountedPrice();
                }
            }

            currentItem.setDiscountedPrice(discountedPrice);
            calculatedCart.getItems().add(currentItem);
        });

        return calculatedCart;
    }

    private CartItem findMaxPriceItemInCart(List<CartItem> itemsToProcess, long categoryId) {
        return itemsToProcess.stream()
                .filter(ci -> ci.getCategoryId().equals(categoryId))
                .max(comparing(CartItem::getPrice))
                .get();
    }

    private Optional<Campaign> findCampaignByProductId(CartItem cartItem) {
        return campaignService
                .getCampaignForTarget(CampaignTargetType.PRODUCT, cartItem.getProductId());
    }

    private Optional<Campaign> findCampaignByCategoryId(long categoryId) {
        return campaignService.getCampaignForTarget(CampaignTargetType.CATEGORY, categoryId);
    }
}
