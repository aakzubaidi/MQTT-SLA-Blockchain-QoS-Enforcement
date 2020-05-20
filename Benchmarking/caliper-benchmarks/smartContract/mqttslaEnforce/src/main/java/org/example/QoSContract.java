/*
 * SPDX-License-Identifier: Apache-2.0
 */
package org.example;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import static java.nio.charset.StandardCharsets.UTF_8;
import com.owlike.genson.Genson;

@Contract(name = "QoSContract",
    info = @Info(title = "QoS contract",
                description = "SLA enforcement",
                version = "0.0.1",
                license =
                        @License(name = "Apache-2.0",
                                url = ""),
                                contact =  @Contact(email = "aakzubaidi@gmail.com",
                                                name = "mqttslaEnforce",
                                                url = "http://mqttslaEnforce.me")))
@Default
public class QoSContract implements ContractInterface {

    private final Genson genson = new Genson();

    private enum MonitoringReportError {
        MonitoringReport_NOT_FOUND,
        MonitoringReport_ALREADY_EXISTS
    }

        /**
     * Placeholder for init function
     *
     * @param ctx the transaction context
     */
    @Transaction()
    public void init(final Context ctx) {
        createMonitoringReport(ctx,"mr01","0","0");
    }
    
    
    public  QoSContract() {

    }

    @Transaction()
    public boolean BillingPeriodExists(Context ctx, String BillingPeriodId) {
        byte[] buffer = ctx.getStub().getState(BillingPeriodId);
        return (buffer != null && buffer.length > 0);
    }


        /**
     * Creates a new car on the ledger.
     *
     * @param ctx the transaction context
     * @param key the key for the new monitoring report
     * @param incidentCount The count of recent Request failures
     * @param validRquestCount The count of Valid requsts
     * @return the created Car
     */
    @Transaction()
    public MonitoringReport createMonitoringReport(final Context ctx, final String key, final String incidentCount, final String validRquestCount) {

        String monitoringReportState =  ctx.getStub().getStringState(key);
        if (!monitoringReportState.isEmpty()) {
            String errorMessage = String.format("Monitoring Report %s already exists", key);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, MonitoringReportError.MonitoringReport_ALREADY_EXISTS.toString());
        }
        MonitoringReport monitoringReport = new MonitoringReport(incidentCount, validRquestCount);
        monitoringReportState = genson.serialize(monitoringReport);
        System.out.println(monitoringReportState);
        ctx.getStub().putStringState(key, monitoringReportState);

        return monitoringReport;
    }



     /**
     * Retrieves a car with the specified key from the ledger.
     *
     * @param ctx the transaction context
     * @param key the key
     * @return the Monitoring report found on the ledger if there was one
     */
    @Transaction()
    public MonitoringReport queryMonitoringReport(final Context ctx, final String key) {
        String monitoringReportState = ctx.getStub().getStringState(key);
        if (monitoringReportState.isEmpty()) {
            String errorMessage = String.format("Monitoring Report %s does not exist", key);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, MonitoringReportError.MonitoringReport_NOT_FOUND.toString());
        }

        MonitoringReport monitoringReport = genson.deserialize(monitoringReportState, MonitoringReport.class);

        return monitoringReport;
    }



        /**
     * update the Monitoring Report Record.
     *
     * @param ctx the transaction context
     * @param key the key
     * @param newIncidentCount the new incident Count
     * * @param newValidRequestCount the new Valid Request Count
     * @return the updated Monitoring report
     */
    @Transaction()
    public MonitoringReport reportViolation(final Context ctx, final String key, final String newIncidentCount, String newValidRequestCount) {

        String monitoringReportState = ctx.getStub().getStringState(key);

        if (monitoringReportState.isEmpty()) {
            String errorMessage = String.format("Monitoring Report %s does not exist", key);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, MonitoringReportError.MonitoringReport_NOT_FOUND.toString());
        }

        MonitoringReport monitoringReport = genson.deserialize(monitoringReportState, MonitoringReport.class);
        //update count Incident & Valid requests
        int updatedIncidentCount = Integer.valueOf(monitoringReport.getIncidentCount()) + Integer.valueOf(newIncidentCount);
        int updatedValidRequestCount = Integer.valueOf(monitoringReport.getValidRquestCount()) + Integer.valueOf(newValidRequestCount);
        MonitoringReport newMonitoringReport = 
                            new MonitoringReport(String.valueOf(updatedIncidentCount), String.valueOf(updatedValidRequestCount));
        String newmonitoringReportState = genson.serialize(newMonitoringReport);
        ctx.getStub().putStringState(key, newmonitoringReportState);
        System.out.println(newmonitoringReportState);
        return newMonitoringReport;
    }


    @Transaction()
    public BillingPeriod assessCompliance (Context ctx, final String BillingPeriodId, final String key, final String newIncidentCount, String newValidRequestCount) {
        boolean exists = BillingPeriodExists(ctx,BillingPeriodId);
        if (exists) {
            throw new RuntimeException("The asset "+BillingPeriodId+" Already exist and billig period has been concluded");
        }

        //final update before concluding the billing period
        MonitoringReport monitoringReport = reportViolation(ctx, key, newIncidentCount, newValidRequestCount);
        
        //get the updated values
        int updatedIncidentCount = Integer.valueOf(monitoringReport.getIncidentCount());
        int updatedValidRequestCount = Integer.valueOf(monitoringReport.getValidRquestCount());

        //Calculate the error Rate
        Double errorRate = ((double)updatedIncidentCount/ (double)updatedValidRequestCount) * 100;

        //decide Compliance Status
        String decision;
        if (errorRate >= 10)
        {
            decision = "Violation";
            System.out.println("Violation because the Error Rate is: " + errorRate);
        }
        else
        {
            decision = "Compliant";
            System.out.println("Compliant because the Error Rare is: " + errorRate);
        } 

        BillingPeriod billinPeriodReport = new BillingPeriod();
        billinPeriodReport.setComplianceStatus(decision);
        billinPeriodReport.setErrorRate(String.valueOf(errorRate));

        ctx.getStub().putState(BillingPeriodId, billinPeriodReport.toJSONString().getBytes(UTF_8));

        return billinPeriodReport;
    }


    @Transaction()
    public BillingPeriod readQoS(Context ctx, String BillingPeriodId) {
        boolean exists = BillingPeriodExists(ctx,BillingPeriodId);
        if (!exists) {
            throw new RuntimeException("The asset "+BillingPeriodId+" does not exist");
        }

        BillingPeriod newAsset = BillingPeriod.fromJSONString(new String(ctx.getStub().getState(BillingPeriodId),UTF_8));
        return newAsset;
    }


    @Transaction()
    public void deleteQoS(Context ctx, String BillingPeriodId) {
        boolean exists = BillingPeriodExists(ctx,BillingPeriodId);
        if (!exists) {
            throw new RuntimeException("The asset "+BillingPeriodId+" does not exist");
        }
        ctx.getStub().delState(BillingPeriodId);
    }

}
