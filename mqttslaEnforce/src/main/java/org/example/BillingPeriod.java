/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.example;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import org.json.JSONObject;

@DataType()
public class BillingPeriod {

    @Property()
    private String ComplianceStatus;
    @Property()
    private String ErrorRate;

    public BillingPeriod() {
    }

    

    public String toJSONString() {
        return new JSONObject(this).toString();
    }

    public static BillingPeriod fromJSONString(String json) {
        String ErrorRate = new JSONObject(json).getString("ErrorRate");
        String ComplianceStatus = new JSONObject(json).getString("ComplianceStatus");
        BillingPeriod billingreport = new BillingPeriod();
        billingreport.setComplianceStatus(ComplianceStatus);
        billingreport.setErrorRate(ErrorRate);
        return billingreport;
    }

    public String getComplianceStatus() {
        return ComplianceStatus;
    }

    public void setComplianceStatus(String complianceStatus) {
        ComplianceStatus = complianceStatus;
    }

    public String getErrorRate() {
        return ErrorRate;
    }

    public void setErrorRate(String errorRate) {
        ErrorRate = errorRate;
    }
}
