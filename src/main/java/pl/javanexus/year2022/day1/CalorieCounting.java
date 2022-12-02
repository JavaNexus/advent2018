package pl.javanexus.year2022.day1;

import com.google.common.base.Strings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CalorieCounting {

    public long getMostCaloriesFromSingleElf(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        long calories = 0, mostCalories = 0;

        String line;
        while ((line = reader.readLine()) != null) {
            if (Strings.isNullOrEmpty(line)) {
                if (calories > mostCalories) {
                    mostCalories = calories;
                }
                calories = 0;
            } else {
                calories += Long.parseLong(line);
            }
        }

        return mostCalories;
    }

    public long getMostCaloriesFromThreeElves(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        long calories = 0, minCalories = 0, maxCalories = 0, totalCalories = 0;

        String line;
        while ((line = reader.readLine()) != null) {
            if (Strings.isNullOrEmpty(line)) {
                if (calories > minCalories) {
                    long totalCaloriesNew = totalCalories - minCalories + calories;
                    minCalories = totalCalories - minCalories - maxCalories;
                    if (calories < minCalories) {
                        minCalories = calories;
                    } else if (calories > maxCalories) {
                        maxCalories = calories;
                    }
                    totalCalories = totalCaloriesNew;
                }
                calories = 0;
            } else {
                calories += Long.parseLong(line);
            }
        }

        if (calories > minCalories) {
            totalCalories = totalCalories - minCalories + calories;
        }

        return totalCalories;
    }
}
