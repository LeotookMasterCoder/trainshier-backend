package com.trainshier.dto;

import lombok.Data;

/**
 * @param totalSales total sales
 * @param totalTransactions total transactions
 * @param totalUsers total users
 * @param averageScore average score
 */
@Data
public class DashboardResponseDTO {

    private Double totalSales;

    private Integer totalTransactions;

    private Integer totalUsers;

    private Double averageScore;
}