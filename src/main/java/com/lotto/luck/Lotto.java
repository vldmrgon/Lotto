package com.lotto.luck;

import com.lotto.luck.model.Client;
import java.util.*;

public class Lotto {

    private final Map<String, Integer> statisticStrongNumbers = new HashMap<>();
    private final Map<String, Integer> statisticSixNumbers = new HashMap<>();
    private final Map<String, List<Client>> map = new LinkedHashMap<>();

    private final Random random = new Random();

    private Integer countGenerations = 0;
    private int countClient = 1;

    public Lotto() {
        map.put("6+Strong", new ArrayList<>());
        map.put("6", new ArrayList<>());
        map.put("5+Strong", new ArrayList<>());
        map.put("5", new ArrayList<>());
        map.put("4+Strong", new ArrayList<>());
        map.put("4", new ArrayList<>());
        map.put("3+Strong", new ArrayList<>());
        map.put("3", new ArrayList<>());
    }

    public List<Client> clientListGen(int numberClient) {
        List<Client> clients = new ArrayList<>();
        for (int i = 0; i < numberClient; i++) {
            Client client = new Client();
            client.setNumber(countClient);
            List<Integer> generatedNumbers = lotteryLottoGen();
            client.setList(generatedNumbers);
            clients.add(client);
            countClient++;
        }
        return clients;
    }

    public List<Integer> lotteryLottoGen() {
        TreeSet<Integer> lotteryNumbers = new TreeSet<>((a, b) -> Integer.compare(b, a));

        while (lotteryNumbers.size() < 6) {
            int randomNumber = getRandomNumberInRange(1, 37);
            lotteryNumbers.add(randomNumber);
            addStatisticSixNumbers(randomNumber);
        }

        List<Integer> fullNumberList = new ArrayList<>(lotteryNumbers);

        int strongNumber = getRandomNumberInRange(1, 7);
        fullNumberList.add(strongNumber);
        addStatisticStrongNumber(strongNumber);
        return fullNumberList;
    }

    private int getRandomNumberInRange(int from, int to) {
        countGenerations++;
        return random.nextInt(to) + from;
    }

    private void addStatisticSixNumbers(Integer generatedNumber) {
        String key = generatedNumber.toString();
        if (statisticSixNumbers.containsKey(key)) {
            Integer integer = statisticSixNumbers.get(key);
            integer++;
            statisticSixNumbers.put(key, integer);
        } else {
            statisticSixNumbers.put(key, 1);
        }
    }

    private void addStatisticStrongNumber(Integer generatedNumber) {
        String key = generatedNumber.toString();
        if (statisticStrongNumbers.containsKey(key)) {
            Integer integer = statisticStrongNumbers.get(key);
            integer++;
            statisticStrongNumbers.put(key, integer);
        } else {
            statisticStrongNumbers.put(key, 1);
        }
    }

    public Map<String, List<Client>> calculateStatistics(List<Integer> lottery, List<Client> clients) {

        clients.forEach(
                client -> {
                    boolean strongMatches = false;

                    List<Integer> list = client.getList();

                    if (lottery.get(6).equals(list.get(6))) {
                        strongMatches = true;
                    }

                    int totalMatches = countMatches(lottery.subList(0, 6), list.subList(0, 6));

                    if (totalMatches >= 3) {
                        String groupKey = totalMatches + (strongMatches ? "+Strong" : "");
                        List<Client> matchingClients = map.get(groupKey);
                        matchingClients.add(client);
                    }
                }
        );
        return map;
    }

    public void printLotteryStatistic() {
        for (String key : map.keySet()) {
            List<Client> clients = map.get(key);
            System.out.printf("%-8s: %d%n", key, clients.size());
        }

    }

    public void printStatisticsLottoNumbers() {
        System.out.println("Count all generation: " + countGenerations);
        System.out.println();

        System.out.println("Statistic for six numbers:");
        printStatistics(statisticSixNumbers, 30);
        System.out.println();

        System.out.println("Statistic for strong numbers:");
        printStatistics(statisticStrongNumbers, 7);
    }

    private void printStatistics(Map<String, Integer> statistics, int limit) {
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(statistics.entrySet());
        entries.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        int count = 0;
        for (Map.Entry<String, Integer> entry : entries) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
            count++;
            if (count >= limit) {
                break;
            }
        }
    }

    private int countMatches(List<Integer> lottery, List<Integer> clientNumbers) {
        int matchCount = 0;

        for (Integer number : lottery) {
            if (clientNumbers.contains(number)) {
                matchCount++;
            }
        }
        return matchCount;
    }
}
