package com.example.tripogranizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trip {

    public String id;
    public String name;
    public String description;
    public List<String> emails = new ArrayList<String>();
    public List<Cost> costs = new ArrayList<Cost>();
    public List<Shopping> items = new ArrayList<Shopping>();

    public Trip(){}

    public Trip(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float GetTotalCost() {
        float cost=0;
        for(Cost item : costs) {
            cost += item.price;
        }
        return cost;
    }
    public float GetCostPerUser() {
        float totalCost = GetTotalCost();
        return totalCost / emails.size();
    }
    public float GetMoneySpentByUser(String userEmail) {
        float money = 0;
        for(Cost item : costs) {
            if(item.userEmail.equals(userEmail)) {
                money += item.price;
            }
        }
        return money;
    }
    public float GetUserBalance(String userEmail) {
        float costPerUser = GetCostPerUser();
        float userSpendMoney = GetMoneySpentByUser(userEmail);
        return costPerUser-userSpendMoney;

    }
    public List<String> GetEmailsWithPositiveBalance() {
        List<String> out= new ArrayList<>();
        for(String email:emails) {
            if(GetUserBalance(email)>0) {
                out.add(email);
            }
        }
        return out;
    }
    public List<String> GetEmailsWithNegativeBalance() {
        List<String> out= new ArrayList<>();
        for(String email:emails) {
            if(GetUserBalance(email)<0) {
                out.add(email);
            }
        }
        return out;
    }
    public List<Refund> TakeMoney(Map<String,Float> balances,float moneyToTake, String destinationEmail) {
        List<Refund> refunds = new ArrayList<>();

        float takenMoney = 0;
        for(String email : balances.keySet()) {
            float needToTakeMoney = moneyToTake-takenMoney;
            float userBalance = balances.get(email);
            if(userBalance==0) {
                continue;
            }
            if(takenMoney==moneyToTake) {
                break;
            }

            // jesli user ma wiecej hajsu
            if(userBalance> needToTakeMoney ) {
                takenMoney += needToTakeMoney;
                float newUserBalance = userBalance-needToTakeMoney;
                balances.put(email,newUserBalance);

                Refund refund = new Refund();
                refund.FromUserEmail = email;
                refund.ToUserEmail = destinationEmail;
                refund.Value = needToTakeMoney;
                refunds.add(refund);
                break;
            }
            if(userBalance<=needToTakeMoney) {
                Refund refund = new Refund();
                refund.FromUserEmail = email;
                refund.ToUserEmail = destinationEmail;
                refund.Value = userBalance;

                takenMoney += userBalance;
                float newUserBalance = 0;
                balances.put(email,newUserBalance);
                refunds.add(refund);
            }
        }
        return refunds;
    }
    public List<Refund> RefundList() {
        List<Refund> list = new ArrayList<>();
        Map<String,Float> positiveBalances = new HashMap<>();
        Map<String,Float> negativeBalances = new HashMap<>();

        for(String email:GetEmailsWithPositiveBalance()) {
            positiveBalances.put(email,GetUserBalance(email));
        }

        for(String email:GetEmailsWithNegativeBalance()) {
            negativeBalances.put(email,GetUserBalance(email));
        }

        // pozytywny oddaje negatywnemu
        for(String email:negativeBalances.keySet()) {
            float balance = negativeBalances.get(email);
            list.addAll(TakeMoney(positiveBalances,balance*(-1),email));
        }

        return list;
    }

}
