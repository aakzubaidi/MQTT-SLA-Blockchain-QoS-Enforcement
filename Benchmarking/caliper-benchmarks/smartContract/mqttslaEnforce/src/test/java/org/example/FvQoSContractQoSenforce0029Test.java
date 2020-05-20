/**
 * SPDX-License-Identifier: Apache-2.0
 */

package org.example;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;

import org.hyperledger.fabric.contract.Context;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeoutException;
import static java.nio.charset.StandardCharsets.UTF_8;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractException;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Use this file for functional testing of your smart contract.
 * Fill out the arguments and return values for a function and
 * use the CodeLens links above the transaction blocks to
 * invoke/submit transactions.
 * All transactions defined in your smart contract are used here
 * to generate tests, including those functions that would
 * normally only be used on instantiate and upgrade operations.
 * This basic test file can also be used as the basis for building
 * further functional tests to run as part of a continuous
 * integration pipeline, or for debugging locally deployed smart
 * contracts by invoking/submitting individual transactions.
 *
 * Generating this test file will also modify the build file
 * in the smart contract project directory. This will require
 * the Java classpath/configuration to be synchronized.
 */

public final class FvQoSContractQoSenforce0029Test {

    Wallet fabricWallet;
    Gateway gateway;
    Gateway.Builder builder;
    Network network;
    Contract contract;
    String homedir = System.getProperty("user.home");
    Path walletPath = Paths.get(homedir, ".fabric-vscode", "environments", "1 Org Local Fabric", "wallets", "Org1");
    Path connectionProfilePath = Paths.get(homedir, ".fabric-vscode", "environments", "1 Org Local Fabric", "gateways", "Org1", "Org1.json");
    String identityName = "admin";
    boolean isLocalhostURL = JavaSmartContractUtil.hasLocalhostURLs(connectionProfilePath);

    @BeforeEach
    public void before() {
        assertThatCode(() -> {
            JavaSmartContractUtil.setDiscoverAsLocalHost(isLocalhostURL);
            fabricWallet = Wallet.createFileSystemWallet(walletPath);
            builder = Gateway.createBuilder();
            builder.identity(fabricWallet, identityName).networkConfig(connectionProfilePath).discovery(true);
            gateway = builder.connect();
            network = gateway.getNetwork("mychannel");
            contract = network.getContract("QoSenforce", "QoSContract");
        }).doesNotThrowAnyException();
    }

    @AfterEach
    public void after() {
        gateway.close();
    }

    //create record for monitoring report on the ledger
    public String reportID = "mr15";

    // @Nested
    // class QueryMonitoringReport {
    //     @Test
    //     public void submitQueryMonitoringReportTest() throws ContractException, TimeoutException, InterruptedException {
    //         // TODO: populate transaction parameters
    //         String arg1 = "EXAMPLE";
    //         String[] args = new String[]{ arg1 };

    //         byte[] response = contract.submitTransaction("queryMonitoringReport", args);
    //         // submitTransaction returns buffer of transaction return value
    //         // TODO: Update with return value of transaction
    //         assertThat(true).isEqualTo(true);
    //         // assertThat(new String(response)).isEqualTo("");
    //     }
    // }

    // @Nested
    // class BillingPeriodExists {
    //     @Test
    //     public void submitBillingPeriodExistsTest() throws ContractException, TimeoutException, InterruptedException {
    //         // TODO: populate transaction parameters
    //         String arg1 = "EXAMPLE";
    //         String[] args = new String[]{ arg1 };

    //         byte[] response = contract.submitTransaction("BillingPeriodExists", args);
    //         // submitTransaction returns buffer of transaction return value
    //         // TODO: Update with return value of transaction
    //         assertThat(true).isEqualTo(true);
    //         // assertThat(new String(response)).isEqualTo("");
    //     }
    // }

    @Nested
    class ReportViolation {
        @Test
        public void submitReportViolationTest() throws ContractException, TimeoutException, InterruptedException {
            String[] args = new String[]{ reportID, "0", "0"};
            byte[] response = contract.submitTransaction("createMonitoringReport", args);
            System.out.println("Sucessful report recoeded on the ledger" + response);
            //test the functionality of report violation 
            for(int i=1; i<= 4; i++) {
            String[] reportArgs = new String[]{ reportID ,String.valueOf(i), String.valueOf(i*1000) };

            byte[] ReportViolationResponse = contract.submitTransaction("reportViolation", reportArgs);
            // submitTransaction returns buffer of transaction return value

            String[] QueryReportArgs = new String[]{reportID};

            byte[] QueryReportResponse = contract.submitTransaction("queryMonitoringReport", QueryReportArgs);
            assertThat(new String (QueryReportResponse)).isEqualTo(new String (ReportViolationResponse));
            System.out.println(new String (QueryReportResponse) + "--> assert to be equal to: " + new String (ReportViolationResponse));
            // assertThat(new String(response)).isEqualTo("");
            }
        }
    }


    // @Nested
    // class DeleteQoS {
    //     @Test
    //     public void submitDeleteQoSTest() throws ContractException, TimeoutException, InterruptedException {
    //         // TODO: populate transaction parameters
    //         String arg1 = "EXAMPLE";
    //         String[] args = new String[]{ arg1 };

    //         byte[] response = contract.submitTransaction("deleteQoS", args);
    //         // submitTransaction returns buffer of transaction return value
    //         // TODO: Update with return value of transaction
    //         assertThat(true).isEqualTo(true);
    //         // assertThat(new String(response)).isEqualTo("");
    //     }
    // }

    // @Nested
    // class ReadQoS {
    //     @Test
    //     public void submitReadQoSTest() throws ContractException, TimeoutException, InterruptedException {
    //         // TODO: populate transaction parameters
    //         String arg1 = "EXAMPLE";
    //         String[] args = new String[]{ arg1 };

    //         byte[] response = contract.submitTransaction("readQoS", args);
    //         // submitTransaction returns buffer of transaction return value
    //         // TODO: Update with return value of transaction
    //         assertThat(true).isEqualTo(true);
    //         // assertThat(new String(response)).isEqualTo("");
    //     }
    // }

    @Nested
    class AssessCompliance {
        @Test
        public void submitAssessComplianceTest() throws ContractException, TimeoutException, InterruptedException {
            
            //  trial for violation cases
            for (int i = 1; i <= 50; i++) {
                String arg1 = "bpTestV7"+String.valueOf(i);
                String arg2 = "mrTestV7"+String.valueOf(i);
                //random Violation Case Error rate :: More than or equal to 10%
                double randomNum = ThreadLocalRandom.current().nextDouble(10, 100 + 1);
                String arg3 = String.valueOf((int)randomNum);
                String arg4 = String.valueOf(100);
                String[] argsV = new String[]{ arg1, arg2, arg3, arg4 };
                
                //create Monitoring report recoed first
                String[] args = new String[]{ arg2, "0", "0"};
                byte[] response = contract.submitTransaction("createMonitoringReport", args);
                System.out.println("Sucessful report recoeded on the ledger" + new String(response));


               System.out.println("Violation case-- " +" Round " +i + " :-: The random value is: "+ (int)randomNum);
               byte[] responseV = contract.submitTransaction("assessCompliance", argsV);
               System.out.println(" The Assessment result is: " +new String(responseV));
               assertThat(new String (responseV)).containsSubsequence("Violation");
            }

        //  trial for Compliant cases
            for (int i = 1; i <= 50; i++) {
                
                String arg1C = "bpTestC7"+String.valueOf(i);
                System.out.println(arg1C);
                String arg2C = "mrTestC7"+String.valueOf(i);
                System.out.println(arg2C);
                //random Compliant Case Error rate // less than 10%
                double randomNumC = ThreadLocalRandom.current().nextDouble(0, 9 + 1);
                String arg3C = String.valueOf((int)randomNumC);
                String arg4C = String.valueOf(100);
                String[] argsC= new String[]{ arg1C, arg2C, arg3C, arg4C };
                
                //create Monitoring report recoed first
                String[] args = new String[]{ arg2C, "0", "0"};
                byte[] responseMC = contract.submitTransaction("createMonitoringReport", args);
                System.out.println("Sucessful report recoeded on the ledger" + new String(responseMC));


                System.out.println("Compliant case-- " +" Round " +i + " :-: The random value is: "+ (int)randomNumC);
               byte[] responseC = contract.submitTransaction("assessCompliance", argsC);
               System.out.println(" The Assessment result is: " +new String(responseC));
                assertThat(new String (responseC)).containsSubsequence("Compliant");

            }
            
        }
    }
}