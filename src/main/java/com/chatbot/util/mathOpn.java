package com.chatbot.util;


import com.chatbot.audioServices.numConverter;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class mathOpn {
    private static final numConverter CONVERTER = new numConverter();

    public void computeMath(String command) {

        command = command.replaceAll("by", "");
        command = command.replaceAll("of", "");
        command = command.replaceAll("to", "");
        command = command.replaceAll("from", "");
        command = command.replaceAll("calculate", "");


        String[] numberOps = {"divide", "over",
                "add", "plus", "sum",
                "multiply", "times", "multiplied",
                "minus", "subtract",
                "percent", "by", "power"};

        ArrayList<String> operation = new ArrayList<>();

        for (int count = 0; count < numberOps.length; count++) {
            if (command.contains(numberOps[count])) {
                operation.add(numberOps[count]);
            }
        }

        String[] numbers = command.split(" ");
        Double[] operands = {0.0, 0.0};

        if (operation.size() == 1) {
            int count = 0;
            for (String item : numbers) {
                item = item.toLowerCase();

                if (!item.equals("zero")) {
                    if (CONVERTER.getNumbers(item) > 0.0 && count < 2) {
                        operands[count] = CONVERTER.getNumbers(item);
                        count++;
                    }
                }
            }
            String result = "";

            DecimalFormat df = new DecimalFormat("#.##");

            switch (operation.get(0)) {
                case "multiply", "times", "multiplied" -> result = (operands[0] + " * " +
                        operands[1] + " = " + df.format(operands[0] * operands[1]));

                case "divide", "by", "divided", "over" -> result = (operands[0] + " / " +
                        operands[1] + " = " + df.format(operands[0] / operands[1]));

                case "add", "sum", "plus" -> result = (operands[0] + " + " +
                        operands[1] + " = " + df.format(operands[0] + operands[1]));

                case "minus", "subtract" -> result = (operands[0] + " - " +
                        operands[1] + " = " + df.format(operands[0] - operands[1]));

                case "power" -> result = (operands[0] + "^" +
                        operands[1] + " = " + df.format(Math.pow(operands[0], operands[1])));

                case "percent", "percentage" -> result = (operands[0] + "% of " +
                        operands[1] + " is " + df.format((operands[0] / 100) * operands[1]));

                default -> System.out.println("Sorry - I am unable to process this operation");
            }

            System.out.println(result);

            try {
                double formatResult = Double.parseDouble(df.format(Double.parseDouble(result.split(" ")[result.split(" ").length - 1])));

                speak.speak(operands[0] + " " + operation.get(0) + " " + operands[1] +
                        " is " + formatResult);

            } catch (Exception e) {
                result = result.split(" ")[result.split(" ").length - 1];

                if (result.equals("0")) {
                    speak.speak(operands[0] + " " + operation.get(0) + " " + operands[1] +
                            " is 0");
                } else if (result.equals("∞")) {
                    speak.speak(operands[0] + " " + operation.get(0) + " " + operands[1] +
                            " is infinity!");
                }
            }

        } else {
            System.out.println("Sorry - I can't process this.");

        }
    }

}
