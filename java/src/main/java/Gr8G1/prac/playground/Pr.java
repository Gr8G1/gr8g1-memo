package Gr8G1.prac.playground;

import java.util.*;
import java.util.stream.*;

public class Pr {
    public static void main(String[] args) {
        Integer groupByString = groupByString(new String[] {"a", "bc", "d", "efg", "hi"});
        System.out.println("group = " + groupByString);

        String qrCode = qrCode(3, 1, "qjnwezgrpirldywt");
        System.out.println("qrCode = " + qrCode);
    }

    public static Integer groupByString(String[] strAtt) {
        HashMap<String, Integer> group = new HashMap<>();

        String[] strArr = {"a","bc","d","efg","hi"};

        for (String s : strArr) {
            group.merge("" + s.length(), 1, Integer::sum);
        }

        Set<Map.Entry<String, Integer>> entries = group.entrySet();

        for (Map.Entry<String, Integer> entry : entries) {
            System.out.println("entry = " + entry.getKey() + ": " + entry.getValue());
        }

        Integer max = group.values().stream().max(Integer::compare).orElse(-1);

        return max;
    }

    public static String qrCode(int q, int r, String code) {
        String collect = IntStream.range(0, code.length())
                             .filter(operand -> operand % q == r)
                             .mapToObj(i -> String.valueOf(code.charAt(i)))
                             .collect(Collectors.joining());

        System.out.println("collect = " + collect);

        String[] split = code.split("(?<=\\G.{" + q + "})");

        return Arrays.stream(split)
                            .filter(s -> s.length() > r)
                            .map(s -> String.valueOf(s.charAt(r)))
                            .reduce("", (s1, s2) -> s1 + s2);


    }
}
