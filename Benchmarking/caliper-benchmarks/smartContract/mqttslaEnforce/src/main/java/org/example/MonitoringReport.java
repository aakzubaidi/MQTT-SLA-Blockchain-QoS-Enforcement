/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.example;

import java.util.Objects;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import com.owlike.genson.annotation.JsonProperty;

@DataType()
public final class MonitoringReport {

    @Property()
    private final String incidentCount;

    @Property()
    private final String validRquestCount;


    public String getIncidentCount() {
        return incidentCount;
    }

    public String getValidRquestCount() {
        return validRquestCount;
    }


   public MonitoringReport(
                        @JsonProperty("incidentCount") final String incidentCount,
                        @JsonProperty("validRquestCount") final String validRquestCount) 
    {
        this.incidentCount = incidentCount;
        this.validRquestCount = validRquestCount;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        MonitoringReport other = (MonitoringReport) obj;

        return Objects.deepEquals(new String[] {getIncidentCount(), getValidRquestCount()},
                new String[] {other.getIncidentCount(), other.getValidRquestCount()});
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIncidentCount(), getValidRquestCount());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [IncidentsCount=" + incidentCount + ", Valid Rquest Count="
                + validRquestCount +"]";
    }


}
