package com.example.touristapp;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

public class Web3Helper {

    private static Web3j web3j;

    // 🔗 Connect to Hardhat Blockchain
    public static Web3j getWeb3j() {

        if (web3j == null) {

            // 👉 Emulator साठी
            String url = "http://10.0.2.2:8545";

            // 👉 Real phone वापरत असशील तर:
            // String url = "http://192.168.X.X:8545";

            web3j = Web3j.build(new HttpService(url));
        }

        return web3j;
    }
}