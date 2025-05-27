package com.retailer.rewardsapp.service;

import com.retailer.rewardsapp.model.CustomerRewardsInfo;
import com.retailer.rewardsapp.model.CustomerTransactions;
import com.retailer.rewardsapp.repo.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RewardsService {

    @Autowired
    private TransactionRepo repo;


    public List<CustomerRewardsInfo> getRewards() {

        List<CustomerTransactions> transactionsList = repo.findAll();

        Map<String, Map<Month, List<CustomerTransactions>>> transactionsPerCustomerPerMonth = transactionsList.stream()
                .collect(Collectors.groupingBy(CustomerTransactions::getCustomerId,
                                Collectors.groupingBy(transaction -> transaction.getTransactionDate().getMonth())
                        )
                );


        Map<String, Map<Month, Long>> rewardsMap = calculateRewards(transactionsPerCustomerPerMonth);

        List<CustomerRewardsInfo> customerRewardsInfoList = rewardsMap.entrySet().stream()
                .flatMap(customer -> {
                    String id = customer.getKey();
                    Map<Month, Long> monthlyRewardsInfo = customer.getValue();
                    long totalRewards = monthlyRewardsInfo.values().stream().mapToLong(Long::longValue).sum();
                    return monthlyRewardsInfo.entrySet().stream()
                            .map(entry -> new CustomerRewardsInfo(id, entry.getValue(), entry.getKey(), totalRewards));
                }).collect(Collectors.toList());
        return customerRewardsInfoList;
    }

    public Map<String, Map<Month, Long>> calculateRewards(Map<String, Map<Month, List<CustomerTransactions>>> transactionsPerCustomerPerMonth) {
        Map<String, Map<Month, Long>> rewards = new HashMap<>();
        BigDecimal reward100 = new BigDecimal("100");
        BigDecimal reward50 = new BigDecimal("50");
        for (Map.Entry<String, Map<Month, List<CustomerTransactions>>> entry : transactionsPerCustomerPerMonth.entrySet()) {
            String customerId = entry.getKey();
            Map<Month, Long> monthAndRewards = new HashMap<>();
            Map<Month, List<CustomerTransactions>> transactionsPerMonth = entry.getValue();

            for (Map.Entry<Month, List<CustomerTransactions>> monthlyEntries : transactionsPerMonth.entrySet()) {
                Month month = monthlyEntries.getKey();

                long totalRewardPointsPerMonth = 0;
                long rewardEligiblePoints = 0;
                List<CustomerTransactions> purchases = monthlyEntries.getValue();
                for (CustomerTransactions t : purchases) {
                    if (t.getTransactionAmount().compareTo(reward100) > 0) {
                        rewardEligiblePoints = t.getTransactionAmount().subtract(reward100).setScale(0, RoundingMode.HALF_UP).intValue();
                        totalRewardPointsPerMonth += rewardEligiblePoints * 2 + 50;
                    }
                    if (t.getTransactionAmount().compareTo(reward100) < 0 && t.getTransactionAmount().compareTo(reward50) > 0) {
                        rewardEligiblePoints = t.getTransactionAmount().subtract(reward50).setScale(0, RoundingMode.HALF_UP).intValue();
                        totalRewardPointsPerMonth += rewardEligiblePoints;
                    }
                }
                monthAndRewards.put(month, totalRewardPointsPerMonth);

            }
            rewards.put(customerId, monthAndRewards);
        }
        return rewards;
    }
}
