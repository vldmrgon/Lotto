package com.lotto.luck;

import com.lotto.luck.model.Raffle;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.text.ParseException;
import java.io.IOException;
import java.io.FileReader;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class RunStatistic {

    private static ConcurrentHashMap<Integer, Integer> mapNumbers = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, Integer> mapSchemas = new ConcurrentHashMap<>();

    public static void main(String[] args) throws ParseException {

        String csvFilePath = "src/main/resources/lotto/Lotto.csv";

        List<Raffle> raffles = extractData(csvFilePath);

        Map<Integer, Integer> numbers = mapNumbers
                .entrySet()
                .stream()
                .parallel()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .filter(entry -> entry.getKey() > 0 && entry.getKey() < 38)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        totalValues(numbers);

        Map<Integer, Integer> numbersStrong = mapNumbers
                .entrySet()
                .stream()
                .parallel()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .filter(entry -> entry.getKey() > 1000 && entry.getKey() < 1008)
                .collect(Collectors.toMap(entry -> entry.getKey() - 1000, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        totalValues(numbersStrong);

        for (Raffle raffle : raffles) {
            Set<Integer> sixNumbers = raffle.getSixNumbers();

            int oneToEight = 0;         //  1 - 9
            int tenToNineteen = 0;      // 10 - 19
            int twentyToTwentyNine = 0; // 20 - 29
            int frothyToFrothyFree = 0; // 30 - 37

            for (Integer number : sixNumbers) {
                if (number > 0 && number <= 9) oneToEight++;
                if (number >= 10 && number <= 19) tenToNineteen++;
                if (number >= 20 && number <= 29) twentyToTwentyNine++;
                if (number >= 30 && number <= 37) frothyToFrothyFree++;
            }

            String key = String.format("%d  -  %d  -  %d  -  %d", oneToEight, tenToNineteen, twentyToTwentyNine, frothyToFrothyFree);
            mapSchemas.merge(key, 1, Integer::sum);
        }

        Map<String, Integer> schema = mapSchemas
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(4)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        System.out.println("Number 1-9  10-19 20-29 30-37");
        schema.forEach((key, value) -> {
            System.out.printf("Schema: %s, Count: %d%n", key, value);
        });
    }

    private static void totalValues(Map<Integer, Integer> numbersStrong) {
        int numbersStrongTotalValues = numbersStrong.values().stream().mapToInt(Integer::intValue).sum();

        numbersStrong.forEach((key, value) -> {
            double percentage = (double) value / numbersStrongTotalValues * 100;
            System.out.printf("Number: %d, Count: %d, Percentage: %.2f%%%n", key, value, percentage);
        });

        System.out.println("* --------------------------------------------- *");
    }

    private static List<Raffle> extractData(String csvFilePath) throws ParseException {
        List<Raffle> list = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new FileReader(csvFilePath))) {
            List<String[]> allData = csvReader.readAll();

            for (int i = 1; i < allData.size(); i++) {
                String[] strings = allData.get(i);
                Raffle raffle = new Raffle(strings, mapNumbers);
                list.add(raffle);
            }
        } catch (IOException | CsvException e) {
            throw new RuntimeException("Error scanning");
        }
        return list;
    }
}