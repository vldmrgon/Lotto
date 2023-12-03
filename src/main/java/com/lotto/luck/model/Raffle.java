package com.lotto.luck.model;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Raffle {
    private int number;
    private Date date;
    private int number1;
    private int number2;
    private int number3;
    private int number4;
    private int number5;
    private int number6;
    private int strong;
    private Set<Integer> sixNumbers;

    public Raffle(String[] data, Map<Integer, Integer> statistic) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        sixNumbers = new LinkedHashSet<>(6);

        this.number = Integer.parseInt(data[0]);
        this.date = dateFormat.parse(data[1]);

        this.number1 = Integer.parseInt(data[2]);
        executeStatistic(this.number1, statistic);
        sixNumbers.add(this.number1);

        this.number2 = Integer.parseInt(data[3]);
        executeStatistic(this.number2, statistic);
        sixNumbers.add(this.number2);

        this.number3 = Integer.parseInt(data[4]);
        executeStatistic(this.number3, statistic);
        sixNumbers.add(this.number3);

        this.number4 = Integer.parseInt(data[5]);
        executeStatistic(this.number4, statistic);
        sixNumbers.add(this.number4);

        this.number5 = Integer.parseInt(data[6]);
        executeStatistic(this.number5, statistic);
        sixNumbers.add(this.number5);

        this.number6 = Integer.parseInt(data[7]);
        executeStatistic(this.number6, statistic);
        sixNumbers.add(this.number6);

        this.strong = Integer.parseInt(data[8]);
        executeStatistic(1000 + this.strong, statistic);
    }

    private void executeStatistic(int number, Map<Integer, Integer> statistic) {
        statistic.merge(number, 1, Integer::sum);
    }
}
