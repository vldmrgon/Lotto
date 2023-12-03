package com.lotto.luck;

import com.lotto.luck.model.Client;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class RunLotto {

    private static int NUMBER_PARTICIPANTS = 2_000_000;
    private static int NUMBER_LOTTERIES = 1;

    public static void main(String[] args) {
        Lotto lotto = new Lotto();

        long startTime = System.currentTimeMillis();
        List<Integer> lottery = null;

        for (int i = 0; i < NUMBER_LOTTERIES; i++) {
            List<Client> clients = lotto.clientListGen(NUMBER_PARTICIPANTS);
            lottery = lotto.lotteryLottoGen();
            Map<String, List<Client>> stringListMap = lotto.calculateStatistics(lottery, clients);
            System.out.println("Number Lotto: " + (1 + i));
        }

        long endTime = System.currentTimeMillis();
        long executionTimeMillis = endTime - startTime;
        long minutes = (executionTimeMillis / 1000) / 60;
        long seconds = (executionTimeMillis / 1000) % 60;
        System.out.println("The method executed " + minutes + " min " + seconds + " sec.");
        System.out.println("* --------------------------------------------- *");

        StringJoiner joiner = new StringJoiner(", ");
        for (Integer number : lottery) {
            joiner.add(String.valueOf(number));
        }
        String result = joiner.toString();
        System.out.println(result);

        lotto.printLotteryStatistic();
        System.out.println("* --------------------------------------------- *");

        lotto.printStatisticsLottoNumbers();
        System.out.println("* --------------------------------------------- *");
    }
}
