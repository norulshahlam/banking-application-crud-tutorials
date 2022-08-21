package com.shah.bankingapplicationcrud;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

class Codility {

    /**
     * that, given an array A of N integers, returns the smallest positive integer (greater than 0) that does not occur in A.
     * <p>
     * For example, given A = [1, 3, 6, 4, 1, 2], the function should return 5.
     * <p>
     * Given A = [1, 2, 3], the function should return 4.
     * <p>
     * Given A = [−1, −3], the function should return 1.
     * <p>
     * Write an efficient algorithm for the following assumptions:
     * <p>
     * N is an integer within the range [1..100,000];
     * each element of array A is an integer within the range [−1,000,000..1,000,000].
     * <p>
     * <p>
     * Example test:   [1, 3, 6, 4, 1, 2]
     * Output:
     * max: 6
     * 3
     * result: 5
     * OK
     * <p>
     * Example test:   [1, 2, 3]
     * Output:
     * max: 3
     * result: 4
     * OK
     * <p>
     * Example test:   [-1, -3]
     * Output:
     * max: -1
     * OK
     */

    @Test
    void codilityTest() {
        List<Integer> n = asList(-3, -2, 4, 0, 1, 3);
        Integer possibleInteger = getPossibleInteger(n);
        System.out.println(possibleInteger);
    }

    public Integer getPossibleInteger(List<Integer> A) {
        Integer result = 0;

        Integer[] sorted = A.stream()
                .peek(System.out::println)
                .sorted()
                .distinct()
                .collect(Collectors.toList())
                .toArray(Integer[]::new);

        int max = sorted[sorted.length - 1];
        System.out.println("max: " + max);
        if (max < 1) {
            result = 1;
            return result;
        }

        for (int i = 0; i < sorted.length; i++) {
            if (sorted[i] + 1 != sorted[i + 1]) {
                System.out.println(i);
                result = sorted[i] + 1;
                break;
            }
            result = max + 1;
        }
        System.out.println("result: " + result);
        return result;
    }

    public Integer getPossibleInteger2(int[] A) {
        List<Integer> list = Arrays.stream(A).boxed().collect(Collectors.toList());

        Integer result = 0;

        Integer[] sorted = list.stream()
                .sorted()
                .peek(System.out::println)
                .distinct()
                .collect(Collectors.toList())
                .toArray(Integer[]::new);

        int max = sorted[sorted.length - 1];
        System.out.println("max: " + max);
        if (max < 1) {
            result = 1;
            return result;
        }

        for (int i = 0; i < sorted.length - 1; i++) {
            if (sorted[i] + 1 != sorted[i + 1]) {
                result = sorted[i] + 1;
                break;
            }
            result = max + 1;
        }
        return result;
    }

    public int solution(int[] A, int S) {

        /**
         * You are given an array A of N integers and an integer S. Your task is to compute how many ways one can choose a contiguous fragment of A that has an arithmetic mean equal to S. The arithmetic mean (average) of a fragment is the sum of the elements of the fragment divided by its length. For example, the arithmetic mean of [1, 4, 4, 5] is 14/4 = 3.5.
         *
         *  Example test:   [[2, 1, 3], 2]
         * OK
         *
         * Example test:   [[0, 4, 3, -1], 2]
         * OK
         *
         * Example test:   [[2, 1, 4], 3]
         * OK
         */

        int n = A.length;
        Map<Integer, Integer> mappedItems = new HashMap<>();
        mappedItems.put(0, 1);
        int currentComputation = 0;
        int response = 0;
        for (int i = 0; i < n; i++) {
            currentComputation += A[i];
            final int key = currentComputation - (S * (i + 1));
            response += mappedItems.getOrDefault(key, 0);
            mappedItems.put(key, mappedItems.getOrDefault(key, 0) + 1);
        }
        return response;
    }
}