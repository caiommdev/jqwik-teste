package org.example;

import net.jqwik.api.*;
import net.jqwik.api.constraints.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;

public class MathFunctionsPropertyTest {

    @Property
    @Report(Reporting.GENERATED)
    void multiplyByTwoAlwaysReturnsEvenNumber(@ForAll int number) {
        int result = MathFunctions.multiplyByTwo(number);
        assertTrue(result % 2 == 0,
            "O resultado de multiplicar " + number + " por 2 deve ser par, mas foi " + result);
    }

    @Property
    @Report(Reporting.GENERATED)
    void multiplyByTwoIsDoubleTheInput(@ForAll int number) {
        int result = MathFunctions.multiplyByTwo(number);
        assertEquals(number * 2, result);
    }

    @Property
    @Report(Reporting.GENERATED)
    void multiplyByTwoHandlesOverflow(@ForAll @IntRange(min = Integer.MAX_VALUE / 2, max = Integer.MAX_VALUE) int number) {
        int result = MathFunctions.multiplyByTwo(number);
        assertTrue(result % 2 == 0);
    }

    @Property
    @Report(Reporting.GENERATED)
    void multiplyByTwoHandlesNegativeOverflow(@ForAll @IntRange(min = Integer.MIN_VALUE, max = Integer.MIN_VALUE / 2) int number) {
        int result = MathFunctions.multiplyByTwo(number);
        assertTrue(result % 2 == 0);
    }

    @Property
    @Report(Reporting.GENERATED)
    void generateMultiplicationTableAllElementsAreMultiples(
            @ForAll("diverseNumbers") int number,
            @ForAll @IntRange(min = 1, max = 100) int limit) {

        int[] table = MathFunctions.generateMultiplicationTable(number, limit);

        for (int i = 0; i < table.length; i++) {
            int expected = number * (i + 1);
            assertEquals(expected, table[i],
                "Elemento na posição " + i + " deveria ser " + expected + " mas foi " + table[i]);

            if (number != 0) {
                long expectedLong = (long) number * (i + 1);
                if (expectedLong == (int) expectedLong) {
                    assertEquals(0, table[i] % number,
                        "Elemento " + table[i] + " não é múltiplo de " + number);
                }
            }
        }

        assertEquals(limit, table.length);
    }

    @Property
    @Report(Reporting.GENERATED)
    void generateMultiplicationTableSequentialProperty(
            @ForAll("diverseNumbers") int number,
            @ForAll @IntRange(min = 2, max = 50) int limit) {

        int[] table = MathFunctions.generateMultiplicationTable(number, limit);

        for (int i = 1; i < table.length; i++) {
            assertEquals(table[i-1] + number, table[i],
                "Diferença entre elementos consecutivos deve ser " + number);
        }
    }

    @Property
    @Report(Reporting.GENERATED)
    void generateMultiplicationTableWithZero(@ForAll @IntRange(min = 1, max = 20) int limit) {
        int[] table = MathFunctions.generateMultiplicationTable(0, limit);

        for (int value : table) {
            assertEquals(0, value);
        }
    }

    @Property
    @Report(Reporting.GENERATED)
    void generateMultiplicationTableWithNegativeNumbers(
            @ForAll @IntRange(min = -1000, max = -1) int number,
            @ForAll @IntRange(min = 1, max = 30) int limit) {

        int[] table = MathFunctions.generateMultiplicationTable(number, limit);

        for (int i = 0; i < table.length; i++) {
            assertTrue(table[i] <= 0, "Multiplicação de número negativo deve resultar em valor não positivo");
            assertEquals(number * (i + 1), table[i]);
        }
    }

    @Property
    @Report(Reporting.GENERATED)
    void isPrimeHasNoDivisorsExceptOneAndItself(@ForAll("diversePrimeNumbers") int prime) {
        assertTrue(MathFunctions.isPrime(prime), "Número gerado deve ser primo");

        for (int i = 2; i < prime; i++) {
            assertFalse(prime % i == 0,
                "Número primo " + prime + " não deveria ser divisível por " + i);
        }
    }

    @Property
    @Report(Reporting.GENERATED)
    void isPrimeReturnsFalseForCompositeNumbers(@ForAll("diverseCompositeNumbers") int composite) {
        assertFalse(MathFunctions.isPrime(composite),
            "Número composto " + composite + " não deveria ser considerado primo");
    }

    @Property
    @Report(Reporting.GENERATED)
    void isPrimeEdgeCases(@ForAll @IntRange(min = Integer.MIN_VALUE, max = 1) int number) {
        assertFalse(MathFunctions.isPrime(number),
            "Números <= 1 não deveriam ser considerados primos");
    }

    @Property
    @Report(Reporting.GENERATED)
    void isPrimeLargeNumbers(@ForAll @IntRange(min = 1000, max = 10000) int number) {
        boolean result = MathFunctions.isPrime(number);

        if (result) {
            for (int i = 2; i <= Math.sqrt(number); i++) {
                assertFalse(number % i == 0);
            }
        }
    }

    @Property
    @Report(Reporting.GENERATED)
    void isPrimeSquareNumbers(@ForAll @IntRange(min = 2, max = 100) int base) {
        int square = base * base;
        assertFalse(MathFunctions.isPrime(square),
            "Números quadrados não deveriam ser primos: " + square);
    }

    @Property
    @Report(Reporting.GENERATED)
    void calculateAverageIsBetweenMinAndMax(@ForAll("diverseIntArrays") int[] numbers) {
        double average = MathFunctions.calculateAverage(numbers);

        int min = Arrays.stream(numbers).min().orElseThrow();
        int max = Arrays.stream(numbers).max().orElseThrow();

        assertTrue(average >= min && average <= max,
            "Média " + average + " deveria estar entre " + min + " e " + max);
    }

    @Property
    @Report(Reporting.GENERATED)
    void calculateAverageThrowsExceptionForNullOrEmpty(@ForAll("nullOrEmptyArrays") int[] numbers) {
        assertThrows(IllegalArgumentException.class,
            () -> MathFunctions.calculateAverage(numbers),
            "Deveria lançar IllegalArgumentException para array nulo ou vazio");
    }

    @Property
    @Report(Reporting.GENERATED)
    void calculateAverageForSingleElement(@ForAll("extremeValues") int number) {
        int[] singleElement = {number};
        double average = MathFunctions.calculateAverage(singleElement);

        assertEquals(number, average, 0.0001,
            "Média de um único elemento deveria ser o próprio elemento");
    }

    @Property
    @Report(Reporting.GENERATED)
    void calculateAverageForIdenticalElements(
            @ForAll("diverseValues") int value,
            @ForAll @IntRange(min = 1, max = 50) int size) {

        int[] identicalElements = new int[size];
        Arrays.fill(identicalElements, value);

        double average = MathFunctions.calculateAverage(identicalElements);

        assertEquals(value, average, 0.0001,
            "Média de elementos idênticos deveria ser o próprio valor");
    }

    @Property
    @Report(Reporting.GENERATED)
    void calculateAverageSymmetryProperty(@ForAll("symmetricArrays") int[] numbers) {
        double average = MathFunctions.calculateAverage(numbers);

        int[] reversed = new int[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            reversed[i] = numbers[numbers.length - 1 - i];
        }

        double reversedAverage = MathFunctions.calculateAverage(reversed);
        assertEquals(average, reversedAverage, 0.0001,
            "Média deve ser a mesma independente da ordem dos elementos");
    }

    @Property
    @Report(Reporting.GENERATED)
    void calculateAverageWithExtremeValues(@ForAll("extremeValueArrays") int[] numbers) {
        double average = MathFunctions.calculateAverage(numbers);

        int min = Arrays.stream(numbers).min().orElseThrow();
        int max = Arrays.stream(numbers).max().orElseThrow();

        assertTrue(average >= min && average <= max);
        assertFalse(Double.isNaN(average));
        assertFalse(Double.isInfinite(average));
    }

    @Provide
    Arbitrary<Integer> diverseNumbers() {
        return Arbitraries.oneOf(
            Arbitraries.integers().between(-10000, 10000),
            Arbitraries.of(0, 1, -1),
            Arbitraries.of(Integer.MAX_VALUE, Integer.MIN_VALUE),
            Arbitraries.integers().between(-100, 100),
            Arbitraries.of(42, -42, 100, -100, 1000, -1000)
        );
    }

    @Provide
    Arbitrary<Integer> diversePrimeNumbers() {
        return Arbitraries.of(
            2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97,
            101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199,
            211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293,
            307, 311, 313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397,
            401, 409, 419, 421, 431, 433, 439, 443, 449, 457, 461, 463, 467, 479, 487, 491, 499,
            503, 509, 521, 523, 541, 547, 557, 563, 569, 571, 577, 587, 593, 599,
            601, 607, 613, 617, 619, 631, 641, 643, 647, 653, 659, 661, 673, 677, 683, 691,
            701, 709, 719, 727, 733, 739, 743, 751, 757, 761, 769, 773, 787, 797,
            809, 811, 821, 823, 827, 829, 839, 853, 857, 859, 863, 877, 881, 883, 887,
            907, 911, 919, 929, 937, 941, 947, 953, 967, 971, 977, 983, 991, 997
        );
    }

    @Provide
    Arbitrary<Integer> diverseCompositeNumbers() {
        return Arbitraries.oneOf(
            Arbitraries.of(4, 6, 8, 9, 10, 12, 14, 15, 16, 18, 20, 21, 22, 24, 25, 26, 27, 28, 30),
            Arbitraries.of(32, 33, 34, 35, 36, 38, 39, 40, 42, 44, 45, 46, 48, 49, 50, 51, 52, 54, 55, 56, 57, 58, 60),
            Arbitraries.of(62, 63, 64, 65, 66, 68, 69, 70, 72, 74, 75, 76, 77, 78, 80, 81, 82, 84, 85, 86, 87, 88, 90),
            Arbitraries.of(91, 92, 93, 94, 95, 96, 98, 99, 100, 102, 104, 105, 106, 108, 110, 111, 112, 114, 115, 116),
            Arbitraries.integers().between(100, 1000).filter(n -> !MathFunctions.isPrime(n)),
            Arbitraries.integers().between(1000, 10000).filter(n -> n % 2 == 0 || n % 3 == 0 || n % 5 == 0)
        );
    }

    @Provide
    Arbitrary<int[]> diverseIntArrays() {
        return Arbitraries.oneOf(
            Arbitraries.integers().between(-1000, 1000).array(int[].class).ofMinSize(1).ofMaxSize(50),
            Arbitraries.integers().between(-100, 100).array(int[].class).ofMinSize(1).ofMaxSize(20),
            Arbitraries.of(Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 1, -1).array(int[].class).ofMinSize(1).ofMaxSize(10),
            Arbitraries.integers().between(-10, 10).array(int[].class).ofMinSize(1).ofMaxSize(100)
        );
    }

    @Provide
    Arbitrary<int[]> nullOrEmptyArrays() {
        return Arbitraries.oneOf(
            Arbitraries.just((int[]) null),
            Arbitraries.just(new int[0])
        );
    }

    @Provide
    Arbitrary<Integer> extremeValues() {
        return Arbitraries.oneOf(
            Arbitraries.of(Integer.MAX_VALUE, Integer.MIN_VALUE, 0, 1, -1),
            Arbitraries.of(32767, -32768, 127, -128),
            Arbitraries.integers().between(-1000000, 1000000)
        );
    }

    @Provide
    Arbitrary<Integer> diverseValues() {
        return Arbitraries.oneOf(
            Arbitraries.integers().between(-100000, 100000),
            Arbitraries.of(0, 1, -1, 42, -42),
            Arbitraries.of(Integer.MAX_VALUE, Integer.MIN_VALUE),
            Arbitraries.integers().between(-10, 10)
        );
    }

    @Provide
    Arbitrary<int[]> symmetricArrays() {
        return Arbitraries.integers().between(-100, 100)
            .array(int[].class)
            .ofMinSize(1)
            .ofMaxSize(30);
    }

    @Provide
    Arbitrary<int[]> extremeValueArrays() {
        return Arbitraries.oneOf(
            Arbitraries.of(Integer.MAX_VALUE, Integer.MIN_VALUE, 0).array(int[].class).ofMinSize(1).ofMaxSize(10),
            Arbitraries.integers().between(Integer.MIN_VALUE / 2, Integer.MAX_VALUE / 2).array(int[].class).ofMinSize(1).ofMaxSize(20),
            Arbitraries.of(1000000, -1000000, 500000, -500000).array(int[].class).ofMinSize(1).ofMaxSize(15)
        );
    }
}
