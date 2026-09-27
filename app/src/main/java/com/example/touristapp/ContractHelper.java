package com.example.touristapp;

import android.util.Log;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.math.BigDecimal;

public class ContractHelper {

    private static final String TAG = "ContractHelper";

    public static void sendDummyTransaction() {

        new Thread(() -> {
            try {
                Web3j web3j = BlockchainHelper.getWeb3j();
                Credentials credentials = BlockchainHelper.getCredentials();

                // 🔥 Dummy transaction (just to test connection)
                Transfer.sendFunds(
                        web3j,
                        credentials,
                        "0x0000000000000000000000000000000000000000",
                        BigDecimal.valueOf(0),
                        Convert.Unit.ETHER
                ).send();

                Log.d(TAG, "Transaction successful 🚀");

            } catch (Exception e) {
                Log.e(TAG, "Error sending dummy transaction", e);
            }
        }).start();
    }
}
