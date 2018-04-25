package com.trendyol.ecampman.campaign.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private Long productId;
    private Long categoryId;
    private Long price;
    private Long discountedPrice;
}
