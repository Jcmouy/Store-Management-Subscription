package com.coffee.shop.dto.response;

import lombok.Data;

@Data
public class ModifySubscriptionDto {

    private String username;
    private String modifiedPlanName;
}
