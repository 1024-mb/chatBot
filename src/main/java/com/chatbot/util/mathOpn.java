package com.chatbot.util;


import com.chatbot.audioServices.numConverter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class mathOpn {
    private static final numConverter CONVERTER = new numConverter();

    public void computeMath(String command) {
        command = command.replaceAll("by", "");
        command = command.replaceAll("of", "");
        command = command.replaceAll("to", "");
        command = command.replaceAll("from", "");
        command = command.replaceAll("calculate", "");
        command = command.replaceAll("what", "");
        command = command.replaceAll("is", "");
        command = command.replaceAll("the", "");


        String[] numberOps = {"divide", "over",
                "add", "plus", "sum",
                "multiply", "times", "multiplied",
                "minus", "subtract",
                "percent", "by", "power"};

        ArrayList<String> operation = new ArrayList<>();

        for (String numberOp : numberOps) {
            if (command.contains(numberOp)) {
                operation.add(numberOp);
            }
        }

        String[] numbers = command.split(" ");
        Double[] operands = {0.0, 0.0};

        if (operation.size() == 1) {
            String performed = operation.get(0);

            String firstNumber = command.substring(0, command.indexOf(performed)).trim();
            String secondNumber = command.substring(command.indexOf(performed) + performed.length()+1).trim();

            try {
                operands[0] = CONVERTER.getNumbers(firstNumber);
                operands[1] = CONVERTER.getNumbers(secondNumber);
            }
            catch(Exception e) {
                System.out.println("Sorry - I'm having trouble processing this");
                speak.speak("Sorry - I'm having trouble processing this");
                return;
            }

            String result = "";

            DecimalFormat df = new DecimalFormat("#.##");

            switch (performed) {
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

            }
            catch (Exception e) {
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