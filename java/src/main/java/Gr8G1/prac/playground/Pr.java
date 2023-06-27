package Gr8G1.prac.playground;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.*;

public class Pr {
    public static void main(String[] args) {
        // 문제 풀이

        String input = "2023-06-06 00:00:00";
        String pattern = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}";

        boolean isMatch = input.matches(pattern);
        System.out.println(isMatch); // Output: true

    }
}
