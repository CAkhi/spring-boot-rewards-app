package com.retailer.rewardsapp;

import com.retailer.rewardsapp.model.CustomerRewardsInfo;
import com.retailer.rewardsapp.service.RewardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RewardsController {

    @Autowired
    private RewardsService rewardsService;

    @GetMapping("/rewards")
    public ResponseEntity<?> getRewards(){
        List<CustomerRewardsInfo> rewardsInfo = rewardsService.getRewards();
        return new ResponseEntity<>(rewardsInfo, HttpStatus.OK);
    }
}
