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
package com.dermitza.simpleWalletTracker.runners.impl;

import com.dermitza.simpleWalletTracker.wallet.Wallet;
import com.dermitza.simpleWalletTracker.wallet.Wallets;
import com.dermitza.simpleWalletTracker.event.LWEventHandler;
import com.dermitza.simpleWalletTracker.runners.AbstractRunner;
import io.reactivex.disposables.Disposable;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import com.dermitza.simpleWalletTracker.event.LWEventTypes;
import com.dermitza.simpleWalletTracker.logging.LoggerHandler;
import java.util.logging.Level;

/**
 *
 * @author K. Dermitzakis <dermitza at gmail.com>
 */
public class BlockRunner extends AbstractRunner {

    private static final java.util.logging.Logger LOGGER = 
            LoggerHandler.getLogger(AbstractRunner.class.getName());

    private boolean connected = false;

    private final Object lock = new Object();

    private CookieManager cookieManager;
    private JavaNetCookieJar cookieJar;
    private OkHttpClient.Builder builder;
    private OkHttpClient client;
    private Web3j web3j;
    private Disposable blockSub;

    private final String apiURL;

    private final Wallets walletsToTrack;
    
    private StringBuilder sb;

    private boolean showFailed = false;

    public BlockRunner(Wallets walletsToTrack, String apiURL) {
        this.walletsToTrack = walletsToTrack;
        this.apiURL = apiURL;
        sb = new StringBuilder();
    }

    public boolean isConnected() {
        synchronized (lock) {
            return this.connected;
        }
    }

    public void showFailed(boolean showFailed) {
        this.showFailed = showFailed;
    }

    private void processBlock(EthBlock blockToProcess) {
        //LOGGER.log(Level.CONFIG,blockToProcess.getError().getMessage());
        EthBlock.Block block = blockToProcess.getBlock();
        if (block != null) {
            LocalDateTime timestamp = Instant.ofEpochSecond(
                    block.getTimestamp()
                            .longValueExact()).atZone(ZoneId.of("UTC")).toLocalDateTime();

            int transactionCount = block.getTransactions().size();
            
            boolean failed = true;
            boolean haveReceipt = false;

            LOGGER.log(Level.CONFIG, "Block {0} transactions {1}", new Object[]{
                block.getNumberRaw(), transactionCount});
            for (EthBlock.TransactionResult<Transaction> t : block.getTransactions()) {
                try {
                    // Get the TX receipt first
                    Optional<TransactionReceipt> txReceipt
                            = web3j.ethGetTransactionReceipt(t.get().getHash()).send().getTransactionReceipt();
                    haveReceipt = txReceipt.isPresent();
                    if(haveReceipt){
                        // STATUSOK DOES NOT WORK, also returns ok on failed TXes
                        // failed = txReceipt.get().isStatusOK();
                        // IF IT HAS LOGS HOWEVER, IT DOES
                        failed = txReceipt.get().getLogs().isEmpty();
                    }else{
                        failed = true;
                    }
                    
                    //if (txReceipt.isPresent()) {
                        // check the receipt exists, otherwise the transaction is not final!
                        // Check if the transaction has failed or not
                       // boolean failed = txReceipt.get().getLogs().isEmpty();
                        if (!showFailed && failed) {
                            // Dont process, however its a failed TX, so maybe keep a log?
                            continue;
                        }

                        String hash = t.get().getHash();
                        String from = t.get().getFrom();
                        String to = t.get().getTo();
                        if (to != null) {
                            boolean isFrom = true;
                            // Try to get a matching wallet from tx
                            Wallet w = walletsToTrack.getWallet(from);
                            if (w == null) { // is this needed? TODO
                                isFrom = false;
                                // Try to get a matching wallet to tx
                                w = walletsToTrack.getWallet(to);
                            }
                            if (w != null) {
                                // We have a match
                                Toolkit.getDefaultToolkit().beep();
                                sb.append(timestamp);
                                sb.append(" | ");
                                sb.append((haveReceipt)?"PROCESSED":"PENDING");
                                sb.append(" | ");
                                sb.append((failed)?"FAIL":"SUCCESS");
                                sb.append(" | ");
                                sb.append((isFrom)?"FROM":"TO");
                                sb.append(" | ");
                                sb.append(w.getName());
                                sb.append(" | TX ");
                                sb.append(hash);
                                sb.append(" | https://snowtrace.io/tx/");
                                sb.append(hash);
                                LOGGER.info(sb.toString());
                                LWEventHandler.fireEvent(LWEventTypes.WALL_EVT, 5, sb.toString());
                                sb.setLength(0);
                                // TODO LOG TXHISTORY IN THE WALLET (?) MAYBE
                            }
                        } else {
                            // Contract creation
                            //System.out.println(timestamp + " | CONTRACT CREATION, HASH: " + t.get().getHash() + " https://snowtrace.io/tx/" + hash);
                        }
                } catch (IOException ioe) {
                    // TODO MORE ERROR HANDLING HERE
                    // WE CAN ASSUME WE ARE DEAD HERE
                    LOGGER.log(Level.SEVERE, "IO Exception", ioe);
                }
            }
        } else {
            LOGGER.log(Level.WARNING, "Received NULL block");
        }
    }

    private void subToBlock() {
        this.blockSub = web3j.blockFlowable(true)
                .subscribe(ethBlock -> {
                    try {
                        processBlock(ethBlock);
                    } catch (Exception e) {
                        LOGGER.log(Level.SEVERE, "Exception while processing block", e);
                    }
                }, throwable -> {
                    // TODO OTHER ERROR HANDLING HERE OR FAIL AND RESTART RUNNER
                    LOGGER.log(Level.SEVERE, "Exception while subscribing to block", throwable);
                });
    }

    // CookieSetup is required as AVAX RPC API is served by a node pool, and as
    // such, we need to indicate our connection's persistence to a node inside
    // the pool. See below
    // https://docs.avax.network/build/tools/public-api/#sticky-sessions
    // https://stackoverflow.com/questions/35743291/add-cookie-to-client-request-okhttp/35744388#35744388
    //
    // At the same time, it looks like trying to subscrive to events on 
    // the websocket api is not available in the mainnet, so we are forced to
    // use the RPC API instead.
    private void initCookieSetup() {
        // init cookie manager
        this.cookieManager = new CookieManager();
        this.cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        this.cookieJar = new JavaNetCookieJar(cookieManager);

        this.builder = new OkHttpClient.Builder();
        this.builder.cookieJar(cookieJar);
    }

    protected void connect() {
        initCookieSetup();
        this.client = builder.build();
        this.web3j = Web3j.build(new HttpService(apiURL, client));
        // DONT NEED A PRIVATE KEY FOR FREE TXES
        //Credentials c = org.web3j.crypto.Credentials.create(privateKey);
        this.connected = true;
        LWEventHandler.fireEvent(LWEventTypes.NET_STA_EVT, BlockRunnerIF.STATUS_CONN, "Connected to " + apiURL);
    }

    protected void disconnect() {
            // dispose of the subscription
            this.blockSub.dispose();
            this.blockSub = null;
            this.web3j.shutdown();
            this.web3j = null;
            this.client = null;
            this.builder = null;
            this.cookieJar = null;
            this.cookieManager = null;
            this.connected = false;
            LWEventHandler.fireEvent(LWEventTypes.NET_STA_EVT, BlockRunnerIF.STATUS_DISC, "Disconnected from " + apiURL);
    }

    @Override
    protected void init() {
        synchronized (lock) {
            connect();
            if (connected) {
                subToBlock();
            } else {
                setRunning(false);
            }
        }
    }

    @Override
    protected void process() {
    }

    @Override
    protected void shutdown() {
        synchronized (lock) {
            disconnect();
        }
    }

}
