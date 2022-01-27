/**
 * This file is part of simpleWalletTracker. Copyright (C) 2022 K. Dermitzakis
 *
 * simpleWalletTracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * simpleWalletTracker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.dermitza.simpleWalletTracker;

import com.dermitza.simpleWalletTracker.event.LWEventHandler;
import com.dermitza.simpleWalletTracker.event.LWEventTypes;
import com.dermitza.simpleWalletTracker.example.ui.MainUI;
import com.dermitza.simpleWalletTracker.wallet.Wallets;
import com.dermitza.simpleWalletTracker.runners.impl.BlockRunner;
import com.dermitza.simpleWalletTracker.logging.LoggerHandler;
import com.dermitza.simpleWalletTracker.timer.tasks.ExitTask;
import java.awt.EventQueue;
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author K. Dermitzakis <dermitza at gmail.com>
 */
public class SimpleWalletTrackerMain {

    private static final Logger LOGGER = LoggerHandler.getLogger(SimpleWalletTrackerMain.class.getName());

    private Wallets wallets;
    private BlockRunner blockRunner;

    private Thread blockRunnerThread;

    private static final String C_API_URL = "https://api.avax.network/ext/bc/C/rpc";

    public static final String BEG
            = "If you find this tool useful, a donation, no matter how small,\n"
            + "would help to keep working on and improving tools like this one.\n\n"
            + "Donation account: 0x52804D3cAeC5a1dFB676bB887DDDB0B2685f6998";

    private MainUI ui;

    private void init() {
        // Initialize wallets to track
        wallets = new Wallets();

        LOGGER.log(Level.CONFIG, "Initializing blockRunner");
        blockRunner = new BlockRunner(wallets, C_API_URL);
        blockRunner.showFailed(true);
        blockRunnerThread = new Thread(blockRunner, "blockRunnerThread");
        blockRunnerThread.start();
    }

    public void shutdown() {

        LOGGER.log(Level.CONFIG, "Stopping blockRunner");
        blockRunner.setRunning(false);
        while (blockRunner.isRunning()) {
            //LOGGER.log(Level.FINE, "Waiting...");
            // Wait until the thread dies
        }
        LOGGER.log(Level.CONFIG, "Stopped.");

        Runnable r = new Runnable() {

            @Override
            public void run() {
                LWEventHandler.fireEvent(LWEventTypes.EXIT_EVT);
                //wait.. and exit
                ExitTask t = new ExitTask();
                t.startTask(1000);
            }
        };
        EventQueue.invokeLater(r);

    }

    public void restartBlockRunner() {

        LOGGER.log(Level.CONFIG, "Restarting blockRunner");
        if (blockRunner.isRunning()) {
            blockRunner.setRunning(false);
        }

        while (blockRunner.isRunning()) {
            //Wait
            LOGGER.log(Level.FINE, "Waiting...");
        }
        blockRunnerThread = new Thread(blockRunner, "blockRunnerThread");
        blockRunnerThread.start();
        LOGGER.log(Level.CONFIG, "Started.");
    }

    public void loadUI() {
        Runnable r = new Runnable() {

            @Override
            public void run() {
                ui = new MainUI();
                ui.showFrames();
            }
        };
        EventQueue.invokeLater(r);
    }

    public static void main(String[] args) {
        LoggerHandler.setLevel(Level.ALL);
        com.dermitza.simpleWalletTracker.logging.LoggerHandler.setLevel(Level.WARNING);

        final String splashString = "SimpleWalletTracker_v0.03";
        final String infoString = "Press 'h' for a command list";
        final String commandList = "\r\n\r\n"
                + "Command menu:\r\n"
                + "h   - This menu\r\n"
                + "q   - Shutdown the tracker\r\n"
                + "s   - Show stats (TODO)\r\n"
                + "u   - Show uptime\r\n"
                + "rt  - Restart the pendingTXRunner\r\n"
                + "rb  - Restart the blockRunner\r\n"
                + "rs  - Restart the soundRunner\r\n"
                + "l   - Select logging level \r\n\r\n";

        Scanner keyboard = new Scanner(System.in);
        boolean exit = false;

        Instant start = Instant.now();
        Instant now;
        System.out.println();

        SimpleWalletTrackerMain s = new SimpleWalletTrackerMain();
        s.loadUI();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
        }
        s.init();
        try {
            Thread.sleep(500);
        } catch (InterruptedException ie) {
        }
        System.out.println(splashString);
        System.out.println("\n\n");
        System.out.println(BEG);

        while (!exit) {

            System.out.println(infoString);
            String input = keyboard.nextLine();

            if (input != null) {
                if ("q".equals(input)) { // Q - Quit
                    s.shutdown();
                    // shutdown
                    exit = true;
                } else if ("s".equals(input)) { // S - show stats
                    System.out.println("KEK");
                } else if ("rb".equals(input)) { // RB - restart block runner
                    s.restartBlockRunner();
                } else if ("l".equals(input)) { // Enter logger level menu
                    System.out.println("Set Logger level (1-9) or press any other key to return to main menu");
                    logLoop:
                    while (true) {
                        input = keyboard.nextLine();
                        if (input != null) {
                            switch (input) {
                                case "1":
                                    System.out.println("Setting Logger Level to OFF");
                                    LoggerHandler.setLevel(Level.OFF);
                                    break logLoop;
                                case "2":
                                    System.out.println("Setting Logger Level to SEVERE");
                                    LoggerHandler.setLevel(Level.SEVERE);
                                    break logLoop;
                                case "3":
                                    System.out.println("Setting Logger Level to WARNING");
                                    LoggerHandler.setLevel(Level.WARNING);
                                    break logLoop;
                                case "4":
                                    System.out.println("Setting Logger Level to INFO");
                                    LoggerHandler.setLevel(Level.INFO);
                                    break logLoop;
                                case "5":
                                    System.out.println("Setting Logger Level to CONFIG");
                                    LoggerHandler.setLevel(Level.CONFIG);
                                    break logLoop;
                                case "6":
                                    System.out.println("Setting Logger Level to FINE");
                                    LoggerHandler.setLevel(Level.FINE);
                                    break logLoop;
                                case "7":
                                    System.out.println("Setting Logger Level to FINER");
                                    LoggerHandler.setLevel(Level.FINER);
                                    break logLoop;
                                case "8":
                                    System.out.println("Setting Logger Level to FINEST");
                                    LoggerHandler.setLevel(Level.FINEST);
                                    break logLoop;
                                case "9":
                                    System.out.println("Setting Logger Level to ALL");
                                    LoggerHandler.setLevel(Level.ALL);
                                    break logLoop;
                                default:
                                    break logLoop;
                            }
                        }
                    }
                    System.out.println("Returning to main menu " + infoString);
                } else if ("h".equals(input)) {
                    System.out.println(commandList);
                } else if ("u".equals(input)) {
                    now = Instant.now();
                    System.out.println("Uptime [PTnHnMnS] " + Duration.between(start, now));
                } else {
                    System.out.println("Unknown command - " + infoString);
                }
            }
        }
        keyboard.close();
    }
}
