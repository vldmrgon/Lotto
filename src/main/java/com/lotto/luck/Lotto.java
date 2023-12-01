package com.lotto.luck;

import com.lotto.luck.model.Client;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
        return Stream
                .iterate(countClient, i -> ++i)
                .limit(numberClient)
                .map(i -> {
                            Client client = new Client();
                            client.setNumber(i);
                            client.setList(lotteryLottoGen());
                            return client;
                        }
                ).collect(Collectors.toList());
    }

    public List<Integer> lotteryLottoGen() {
        Set<Integer> lotteryNumbers = Stream
                .generate(() -> getRandomNumberInRange(1, 37))
                .distinct()
                .limit(6)
                .collect(Collectors.toCollection(TreeSet::new));

        int strongNumber = getRandomNumberInRange(1, 7);

        return Stream.concat(lotteryNumbers.stream(), Stream.of(strongNumber))
                .peek(this::addStatisticSixNumbers)
                .peek(this::addStatisticStrongNumber)
                .collect(Collectors.toList());
    }

    private int getRandomNumberInRange(int from, int to) {
        countGenerations++;
        return random.nextInt(to) + from;
    }

    private void addStatisticSixNumbers(Integer generatedNumber) {
        String key = generatedNumber.toString();
        statisticSixNumbers.put(key, statisticSixNumbers.containsKey(key) ? statisticSixNumbers.get(key) + 1 : 1);
    }

    private void addStatisticStrongNumber(Integer generatedNumber) {
        String key = generatedNumber.toString();
        statisticStrongNumbers.put(key, statisticStrongNumbers.containsKey(key) ? statisticStrongNumbers.get(key) + 1 : 1);
    }

    public Map<String, List<Client>> calculateStatistics(List<Integer> lottery, List<Client> clients) {
        clients
                .forEach(client -> {
                            boolean strongMatches = lottery.get(6).equals(client.getList().get(6));
                            int totalMatches = countMatches(lottery.subList(0, 6), client.getList().subList(0, 6));
                            if (totalMatches >= 3) {
                                String groupKey = totalMatches + (strongMatches ? "+Strong" : "");
                                map.computeIfAbsent(groupKey, k -> new ArrayList<>()).add(client);
                            }
                        }
                );
        return map;
    }

    private void printStatistics(Map<String, Integer> statistics, int limit) {
        statistics
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(limit)
                .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
    }

    private int countMatches(List<Integer> lottery, List<Integer> clientNumbers) {
        return (int) lottery.stream().filter(clientNumbers::contains).count();
    }

    public void printLotteryStatistic() {
        map.forEach((key, clients) -> System.out.printf("%-8s: %d%n", key, clients.size()));
    }

    public void printStatisticsLottoNumbers() {
        System.out.println("Count all generation: " + countGenerations);
        System.out.println();

        System.out.println("Statistic for six numbers:");
        printStatistics(statisticSixNumbers, 30);
        System.out.println();

        System.out.println("Statistic for strong numbers:");
        printStatistics(statisticStrongNumbers, 7);
        System.out.println();
    }
}
