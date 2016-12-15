#!/usr/bin/env bash

scalaver=2.11
release=1.6.1
topicname="testIn_1"
inputfmt="CSV"
gzcompressed=false

KAMANJA_HOME={InstallDirectory}

kafkahostname="localhost:9092"
kafkaversion="0.10"
while [ $# -gt 0 ]
do
    if [ "$1" == "--kafkahosts" ]; then
    	kafkahostname="$2"
    fi
    if [ "$1" == "--kafkaversion" ]; then
        kafkaversion="$2"
    fi
    shift
done


if [ "$KAMANJA_SEC_CONFIG" ]; then
  JAAS_CLIENT_OPT="-Djava.security.auth.login.config="$KAMANJA_SEC_CONFIG
  echo "Using java.security.auth.login.config="$JAAS_CLIENT_OPT
fi

if [ "$KAMANJA_KERBEROS_CONFIG" ]; then
  KERBEROS_CONFIG_OPT="-Djava.security.krb5.confg="$KAMANJA_KERBEROS_CONFIG
  echo "Using java.security.krb5.conf="$KERBEROS_CONFIG_OPT
fi

if [ "$KAMANJA_SECURITY_CLIENT" ]; then
  SECURITY_PROP_OPT="--secprops "$KAMANJA_SECURITY_CLIENT
  echo "Using security client = "$SECURITY_PROP_OPT
fi

echo "User selected: $INPUTFILE"
echo "Running kafka client version $kafkaversion"

currentKamanjaVersion=1.6.1

KEYSTORE_CONFIG_OPT=""
KEYSTORE_PASS_CONFIG_OPT=""

# -Djavax.net.ssl.keyStore=/tmp/config/cert/kamanja.ks -Djavax.net.ssl.keyStorePassword=password
if [ "$KAMANJA_KEYSTORE" != "" ]; then
	KEYSTORE_CONFIG_OPT="-Djavax.net.ssl.keyStore=$KAMANJA_KEYSTORE"
	KEYSTORE_PASS_CONFIG_OPT="-Djavax.net.ssl.keyStorePassword=$KAMANJA_KEYSTORE_PASS"
fi

TRUSTSTORE_CONFIG_OPT=""
TRUSTSTORE_PASS_CONFIG_OPT=""

# -Djavax.net.ssl.trustStore=/tmp/config/cert/kamanja.ts -Djavax.net.ssl.trustStorePassword=password
if [ "$KAMANJA_TRUSTSTORE" != "" ]; then
	TRUSTSTORE_CONFIG_OPT="-Djavax.net.ssl.trustStore=$KAMANJA_TRUSTSTORE"
	TRUSTSTORE_PASS_CONFIG_OPT="-Djavax.net.ssl.trustStorePassword=$KAMANJA_TRUSTSTORE_PASS"
fi

if [ "$kafkaversion" = "0.8" ]; then
  java  $JAAS_CLIENT_OPT $KERBEROS_CONFIG_OPT $KEYSTORE_CONFIG_OPT $KEYSTORE_PASS_CONFIG_OPT $TRUSTSTORE_CONFIG_OPT $TRUSTSTORE_PASS_CONFIG_OPT -cp $KAMANJA_HOME/lib/system/ExtDependencyLibs2_2.11-$currentKamanjaVersion.jar:$KAMANJA_HOME/lib/system/ExtDependencyLibs_2.11-$currentKamanjaVersion.jar:$KAMANJA_HOME/lib/system/KamanjaInternalDeps_2.11-$currentKamanjaVersion.jar:$KAMANJA_HOME/lib/system/kamanjakafkaadapters_0_8_2.11-$currentKamanjaVersion.jar:$KAMANJA_HOME/lib/system/kafka-clients-0.8.2.2.jar:$KAMANJA_HOME/lib/system/kafka_2.11-0.8.2.2.jar:$KAMANJA_HOME/lib/system/simplekafkaproducer_2.11-$currentKamanjaVersion.jar com.ligadata.tools.SimpleKafkaProducer --gz true --topics "testin_1" --threads 1 --topicpartitions 8 --brokerlist "$kafkahostname" --files "$KAMANJA_HOME/input/SampleApplications/data/pairs.txt.gz" --partitionkeyidxs "0" --kafkaversion 8 --format CSV $SECURITY_PROP_OPT
elif [ "$kafkaversion" = "0.9" ]; then
  java  $JAAS_CLIENT_OPT $KERBEROS_CONFIG_OPT $KEYSTORE_CONFIG_OPT $KEYSTORE_PASS_CONFIG_OPT $TRUSTSTORE_CONFIG_OPT $TRUSTSTORE_PASS_CONFIG_OPT -cp $KAMANJA_HOME/lib/system/ExtDependencyLibs2_2.11-$currentKamanjaVersion.jar:$KAMANJA_HOME/lib/system/ExtDependencyLibs_2.11-$currentKamanjaVersion.jar:$KAMANJA_HOME/lib/system/KamanjaInternalDeps_2.11-$currentKamanjaVersion.jar:$KAMANJA_HOME/lib/system/kamanjakafkaadapters_0_9_2.11-$currentKamanjaVersion.jar:$KAMANJA_HOME/lib/system/kafka-clients-0.9.0.1.jar:$KAMANJA_HOME/lib/system/simplekafkaproducer_2.11-$currentKamanjaVersion.jar com.ligadata.tools.SimpleKafkaProducer --gz true --topics "testin_1" --threads 1 --topicpartitions 8 --brokerlist "$kafkahostname" --files "$KAMANJA_HOME/input/SampleApplications/data/pairs.txt.gz" --partitionkeyidxs "0" --kafkaversion 9 --format CSV $SECURITY_PROP_OPT
else
  java  $JAAS_CLIENT_OPT $KERBEROS_CONFIG_OPT $KEYSTORE_CONFIG_OPT $KEYSTORE_PASS_CONFIG_OPT $TRUSTSTORE_CONFIG_OPT $TRUSTSTORE_PASS_CONFIG_OPT -cp $KAMANJA_HOME/lib/system/ExtDependencyLibs2_2.11-$currentKamanjaVersion.jar:$KAMANJA_HOME/lib/system/ExtDependencyLibs_2.11-$currentKamanjaVersion.jar:$KAMANJA_HOME/lib/system/KamanjaInternalDeps_2.11-$currentKamanjaVersion.jar:$KAMANJA_HOME/lib/system/kamanjakafkaadapters_0_10_2.11-$currentKamanjaVersion.jar:$KAMANJA_HOME/lib/system/kafka-clients-0.10.0.0.jar:$KAMANJA_HOME/lib/system/simplekafkaproducer_2.11-$currentKamanjaVersion.jar com.ligadata.tools.SimpleKafkaProducer --gz true --topics "testin_1" --threads 1 --topicpartitions 8 --brokerlist "$kafkahostname" --files "$KAMANJA_HOME/input/SampleApplications/data/pairs.txt.gz" --partitionkeyidxs "0" --format CSV $SECURITY_PROP_OPT
fi


