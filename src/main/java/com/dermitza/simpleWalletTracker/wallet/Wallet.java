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

import java.util.Objects;

/**
 *
 * @author K. Dermitzakis <dermitza at gmail.com>
 */
public class Wallet {
    
    private final String walletHash;
    private final String walletName;
    
    // All hashes should be lowercase as this is what the block explorer returns
    public Wallet(String walletHash, String walletName){
        this.walletHash = walletHash.toLowerCase();
        this.walletName = walletName;
    }
    
    public String getWallet(){
        return this.walletHash;
    }
    
    public String getName(){
        return this.walletName;
    }
    
    @Override
    public String toString(){
        return this.walletHash;
    }
    
    @Override
    public boolean equals(Object o){
        if(o instanceof Wallet){
            return walletHash.equals(o);
        }
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.walletHash);
        return hash;
    }
    
    
}
