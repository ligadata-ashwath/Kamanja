/*
 * Copyright 2015 ligaDATA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ligadata.kamanja.financial;

import com.ligadata.KamanjaBase.*;
import com.ligadata.kamanja.metadata.ModelDef;

import java.io.*;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.DateTime;

import java.util.Locale;

public class SubscriberUsageAlert extends ModelInstance {
    public SubscriberUsageAlert(ModelInstanceFactory factory) {
        super(factory);
    }

    public ContainerOrConcept[] execute(TransactionContext txnCtxt, ContainerOrConcept[] execMsgsSet, int matchedInputSetIndex, boolean outputDefault) {
        if (execMsgsSet.length == 0) {
            ContainerInterface[] returnArr = new ContainerInterface[0];
            return returnArr;
        }
        //Get the current transaction data
        SubscriberUsage rcntTxn = (SubscriberUsage) execMsgsSet[0];

        // Get the current subscriber, account info and global preferences
        SubscriberGlobalPreferences gPref = (SubscriberGlobalPreferences) SubscriberGlobalPreferences.getRecentOrNew(new String[]{"Type 1"});
        System.out.println("msisdn:" + rcntTxn.msisdn());
        SubscriberInfo subInfo = (SubscriberInfo) SubscriberInfo.getRecentOrNew(new String[]{String.valueOf(rcntTxn.msisdn())});
        System.out.println("subinfo:" + subInfo.actno());
        AccountInfo actInfo = (AccountInfo) AccountInfoFactory.rddObject.getRecentOrNew(new String[]{subInfo.actno()});
        System.out.println("Account info:" + actInfo.actno());
        SubscriberPlans planInfo = (SubscriberPlans) SubscriberPlansFactory.rddObject.getRecentOrNew(new String[]{subInfo.planname()});
        System.out.println("planInfo.planlimit:" + planInfo.planlimit());
        String logTag = "SubscriberUsageAlertApp(" + subInfo.msisdn() + "," + actInfo.actno() + "): ";

        // Get current values of aggregatedUsage
        SubscriberAggregatedUsage subAggrUsage = (SubscriberAggregatedUsage) SubscriberAggregatedUsage.getRecentOrNew(new String[]{String.valueOf(subInfo.msisdn())});
        AccountAggregatedUsage actAggrUsage = (AccountAggregatedUsage) AccountAggregatedUsage.getRecentOrNew(new String[]{actInfo.actno()});


        // Get current month
        RddDate curDtTmInMs = RddDate.currentGmtDateTime();
        int txnMonth = getMonth(String.valueOf(rcntTxn.date()));
        int currentMonth = getCurrentMonth();

        // planLimit values are supplied as GB. But SubscriberUsage record contains the usage as MB
        // So convert planLimit to MB
        long planLimit = planInfo.planlimit() * 1024;
        long indLimit = planInfo.individuallimit() * 1024;
        System.out.println("plan limit:" + planLimit);

        // we are supposed to check whether the usage belongs to current month
        // if the usage doesn't belong to this month, we are supposed to ignore it
        // Here we let all the data pass through just to generate sample alerts no matter
        // what the actual usage data is
        // if( txnMonth != currentMonth )
        //   return null;

        //aggregate account uasage
        long actMonthlyUsage = actAggrUsage.thismonthusage() + rcntTxn.usage();
        actAggrUsage.set("thismonthusage", actMonthlyUsage);
        actAggrUsage.save();

        // aggregate the usage
        // aggregate individual subscriber usage
        long subMonthlyUsage = subAggrUsage.thismonthusage() + rcntTxn.usage();
        subAggrUsage.set("thismonthusage", subMonthlyUsage);
        subAggrUsage.save();

        long curTmInMs = curDtTmInMs.getDateTimeInMs();

        // generate alerts if plan limits are exceeded based on planType
        System.out.println("plantype:" + planInfo.plantype());
        System.out.println("plan limit:" + planLimit);
        System.out.println("act monthly usage:" + actMonthlyUsage);
        System.out.println("--------------------------------------");
        switch (planInfo.plantype()) {
            case 1:
                if (actMonthlyUsage > planLimit) {
                    if (actInfo.thresholdalertoptout() == false) {
                        AccountUsageAlertMessage msg = (AccountUsageAlertMessage) AccountUsageAlertMessage.createInstance();
                        msg.set(0, actInfo.actno());
                        msg.set(1, actMonthlyUsage);
                        msg.set(2, "Past Threshold Alert");
                        msg.set(3, curTmInMs);
                        ContainerInterface[] returnArr = new ContainerInterface[1];
                        returnArr[0] = msg;
                        return returnArr;
                    }
                }
                break;
            case 2:
                if (subMonthlyUsage > indLimit) {
                    if (subInfo.thresholdalertoptout() == false) {
                        SubscriberUsageAlertMessage msg = (SubscriberUsageAlertMessage) SubscriberUsageAlertMessage.createInstance();
                        msg.set(0, rcntTxn.msisdn());
                        msg.set(1, subMonthlyUsage);
                        msg.set(2, "Past Threshold Alert");
                        msg.set(3, curTmInMs);
                        ContainerInterface[] returnArr = new ContainerInterface[1];
                        returnArr[0] = msg;
                        return returnArr;
                    }
                }
                break;

            default: {
                break;
            }
        }

        ContainerInterface[] returnArr = new ContainerInterface[0];
        return returnArr;
    }

    private int getMonth(String dt) {
        DateTime jdt = DateTime.parse(dt, DateTimeFormat.forPattern("yyyyMMdd").withLocale(Locale.US));
        return jdt.monthOfYear().get();
    }

    private int getCurrentMonth() {
        DateTime jdt = new DateTime();
        return jdt.monthOfYear().get();
    }

    private void dumpAppLog(String logStr) throws IOException {
        FileWriter fw = null;
        try {
            fw = new FileWriter("SubscriberUsageAlertAppLog.txt", true);

            fw.write(logStr + "\n");
        } finally {
            fw.close();
        }
    }

    public static class SubscriberUsageAlertFactory extends ModelInstanceFactory {
        public SubscriberUsageAlertFactory(ModelDef modelDef, NodeContext nodeContext) {
            super(modelDef, nodeContext);
        }

        public ModelInstance createModelInstance() {
            return new SubscriberUsageAlert(this);
        }

        public String getModelName() {
            return "com.ligadata.kamanja.financial.SubscriberUsageAlert";
        }

        public String getVersion() {
            return "0.0.1";
        }
    }
}
