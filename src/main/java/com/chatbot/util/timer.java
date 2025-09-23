package com.chatbot.util;

import java.util.Scanner;

public class timer {
    public void startTimer(String command) {
        Scanner scanner = new Scanner(System.in);

        if (command.toLowerCase().contains("second")) {
            speak.speak("Enter timer duration");
            System.out.print("Enter Timer Duration /seconds: ");

            int duration = Integer.parseInt(scanner.next());
            scanner.nextLine();

            while (duration > 0) {
                try {
                    Thread.sleep(1000);
                    duration--;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Interrupted");
                }
                System.out.println(duration);
            }

            System.out.println("Time's up!");

        }
        else if (command.toLowerCase().contains("minutes")) {
            speak.speak("Enter timer duration");
            System.out.print("Enter Timer Duration /minutes: ");

            int duration = Integer.parseInt(scanner.next());
            scanner.nextLine();

            System.out.println(duration + ":00");
            while (duration > 0) {
                int secondsLeft = 60;
                while (secondsLeft > 0) {
                    try {
                        Thread.sleep(1000);
                        secondsLeft--;

                        System.out.println(duration - 1 + ":" + getFormat(secondsLeft));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.out.println("Interrupted");
                    }
                }
                duration--;
            }
            System.out.println("Time's up!");

        }

        else if (command.toLowerCase().contains("hour")) {
            speak.speak("Enter timer duration");
            System.out.print("Enter Timer Duration /hours: ");

            int duration = Integer.parseInt(scanner.next());
            scanner.nextLine();

            System.out.println(duration + ":00");
            while (duration > 0) {
                int minutesLeft = 60;

                while (minutesLeft > 0) {
                    int secondsLeft = 60;

                    while (secondsLeft > 0) {
                        try {
                            Thread.sleep(1000);
                            secondsLeft--;


                            System.out.println(getFormat(duration - 1) + ":" + getFormat(minutesLeft - 1) + ":" + getFormat(secondsLeft));

                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            System.out.println("Interrupted");
                        }
                    }

                    minutesLeft--;


                    System.out.println(getFormat(duration - 1) + ":" + getFormat(minutesLeft) + ":" + getFormat(secondsLeft));
                }
                duration--;

            }
            System.out.println("Time's up!");
        }

        else {
            speak.speak("Enter timer duration");
            System.out.print("Enter Timer Duration /minutes: ");

            int duration = Integer.parseInt(scanner.next());
            scanner.nextLine();

            System.out.println(duration + ":00");
            while (duration > 0) {
                int secondsLeft = 60;

                while (secondsLeft > 0) {
                    try {
                        Thread.sleep(1000);
                        secondsLeft--;

                        if (secondsLeft < 10) {
                            System.out.println(duration - 1 + ":" + "0" + secondsLeft);
                        } else {
                            System.out.println(duration - 1 + ":" + secondsLeft);
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.out.println("Interrupted");
                    }
                }
                duration--;
            }
            System.out.println("Time's up!");
        }
    }

    public String getFormat(int quantity) {
        if(quantity<10) {
            return "0" + quantity;
        }
        else {
            return Integer.toString(quantity);
        }

    }

}
