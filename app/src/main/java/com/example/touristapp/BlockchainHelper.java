package com.example.touristapp;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

public class BlockchainHelper {

    // 🔐 Hardhat account private key (Account #0)
    private static final String PRIVATE_KEY = "0xf39fd6e51aad88f6f4ce6ab8827279cfffb92266"; // 👈 replace कर

    public static Credentials getCredentials() {
        return Credentials.create(PRIVATE_KEY);
    }

    public static Web3j getWeb3j() {
        return Web3Helper.getWeb3j();
    }
}