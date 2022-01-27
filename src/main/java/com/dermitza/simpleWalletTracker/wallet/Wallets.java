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
package com.dermitza.simpleWalletTracker.wallet;

import com.dermitza.simpleWalletTracker.util.FileUtils;
import java.util.HashMap;

/**
 *
 * @author K. Dermitzakis <dermitza at gmail.com>
 */
public class Wallets {

    private HashMap<String, Wallet> wallets;

    public Wallets() {
        loadWallets();
        //FileUtils.saveData(wallets, "wallets.wall");
    }

    public boolean contains(String walletHash) {
        // All hashes should be lowercase as 
        //this is what the block explorer returns
        // possibly safe to omit TODO
        return wallets.containsKey(walletHash.toLowerCase());
    }

    
    private void loadWallets() {
        wallets = FileUtils.<HashMap>loadData("wallets.wall");
    }

    // Returns null if wallet doesnt exist
    public Wallet getWallet(String walletHash) {
        // All hashes should be lowercase as 
        //this is what the block explorer returns
        // possibly safe to omit TODO
        return wallets.get(walletHash.toLowerCase());
    }

    public boolean addNewWallet(String walletHash, String walletName) {
        if (wallets.containsKey(walletHash)) {
            // Wallet already exists, handle error TODO
            return false;
        } else {
            wallets.put(walletHash, new Wallet(walletHash, walletName));
            return true;
        }
    }

    public HashMap<String, Wallet> getWallets() {
        return this.wallets;
    }

}
