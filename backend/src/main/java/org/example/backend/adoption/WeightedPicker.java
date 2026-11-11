package org.example.backend.adoption;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Random;

@Component
public class WeightedPicker {

    private final Random random;
    public WeightedPicker(Random random) { this.random = random; }

    public <T> T pick(Map<T, Integer> weights) {
        int roll = random.nextInt(totalOf(weights));
        return findByRoll(weights, roll);
    }

    private static <T> int totalOf(Map<T, Integer> weights) {
        return weights.values().stream().mapToInt(Integer::intValue).sum();
    }

    private static <T> T findByRoll(Map<T, Integer> weights, int roll) {
        int cumulative = 0;
        for (Map.Entry<T, Integer> entry : weights.entrySet()) {
            cumulative += entry.getValue();
            if (roll < cumulative) return entry.getKey();
        }
        throw new IllegalStateException("Roll outside total weight");
    }
}