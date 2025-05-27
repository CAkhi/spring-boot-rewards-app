package com.retailer.rewardsapp.repo;

import com.retailer.rewardsapp.model.CustomerTransactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepo extends JpaRepository<CustomerTransactions, Integer> {

}
