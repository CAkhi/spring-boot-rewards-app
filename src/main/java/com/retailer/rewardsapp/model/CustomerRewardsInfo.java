package com.retailer.rewardsapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Month;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRewardsInfo {
    private String customerId;
    private long rewardsPerMonth;
    private Month month;
    private long totalRewards;
}
