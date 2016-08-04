#!/usr/bin/env bash
KAMANJA_HOME={InstallDirectory}
if [ "$#" -eq 1 ]; then
INPUTFILE=$@
else
count=0
FILEDIR=$KAMANJA_HOME/input/SampleApplications/data
for entry in "$FILEDIR"/*
do
count=$((count+1))
  echo "$count: $entry"
  LISTOFFILES[count-1]=$entry
done
read -p "Please select from the above options: " useroption
OPTION=useroption-1
INPUTFILE=${LISTOFFILES[OPTION]}
fi

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
  KERBEROS_CONFIG_OPT="-Djava.security.krb5.conf="$KAMANJA_KERBEROS_CONFIG
  echo "Using java.security.krb5.conf="$KERBEROS_CONFIG_OPT
fi

if [ "$KAMANJA_SECURITY_CLIENT" ]; then
  SECURITY_PROP_OPT="--secprops "$KAMANJA_SECURITY_CLIENT
  echo "Using security client = "$SECURITY_PROP_OPT
fi

echo "User selected: $INPUTFILE"
echo "Running kafka client version $kafkaversion"

currentKamanjaVersion=1.5.2

if [ "$kafkaversion" = "0.8" ]; then
  java  $JAAS_CLIENT_OPT $KERBEROS_CONFIG_OPT $KEYSTORE_CONFIG_OPT $KEYSTORE_PASS_CONFIG_OPT $TRUSTSTORE_CONFIG_OPT $TRUSTSTORE_PASS_CONFIG_OPT -cp $KAMANJA_HOME/lib/system/ExtDependencyLibs2_2.11-$currentKamanjaVersion.jar:$KAMANJA_HOME/lib/system/ExtDependencyLibs_2.11-$currentKamanjaVersion.jar:$KAMANJA_HOME/lib/system/KamanjaInternalDeps_2.11-$currentKamanjaVersion.jar:$KAMANJA_HOME/lib/system/kafka-clients-0.8.2.2.jar:$KAMANJA_HOME/lib/system/simplekafkaproducer_v8_2.11-$currentKamanjaVersion.jar com.ligadata.tools.kafkaProducer_v8.SimpleKafkaProducer --gz true --topics "testin_1" --threads 1 --topicpartitions 8 --brokerlist "$kafkahostname" --files $INPUTFILE  --partitionkeyidxs "1" --format CSV $SECURITY_PROP_OPT
elif [ "$kafkaversion" = "0.9" ]; then
  java  $JAAS_CLIENT_OPT $KERBEROS_CONFIG_OPT $KEYSTORE_CONFIG_OPT $KEYSTORE_PASS_CONFIG_OPT $TRUSTSTORE_CONFIG_OPT $TRUSTSTORE_PASS_CONFIG_OPT -cp $KAMANJA_HOME/lib/system/ExtDependencyLibs2_2.11-$currentKamanjaVersion.jar:$KAMANJA_HOME/lib/system/ExtDependencyLibs_2.11-$currentKamanjaVersion.jar:$KAMANJA_HOME/lib/system/KamanjaInternalDeps_2.11-$currentKamanjaVersion.jar:$KAMANJA_HOME/lib/system/kafka-clients-0.9.0.1.jar:$KAMANJA_HOME/lib/system/simplekafkaproducer_v9_2.11-$currentKamanjaVersion.jar com.ligadata.tools.kafkaProducer_v9.SimpleKafkaProducer --gz true --topics "testin_1" --threads 1 --topicpartitions 8 --brokerlist "$kafkahostname" --files $INPUTFILE  --partitionkeyidxs "1" --format CSV $SECURITY_PROP_OPT
else
  java  $JAAS_CLIENT_OPT $KERBEROS_CONFIG_OPT $KEYSTORE_CONFIG_OPT $KEYSTORE_PASS_CONFIG_OPT $TRUSTSTORE_CONFIG_OPT $TRUSTSTORE_PASS_CONFIG_OPT -cp $KAMANJA_HOME/lib/system/ExtDependencyLibs2_2.11-$currentKamanjaVersion.jar:$KAMANJA_HOME/lib/system/ExtDependencyLibs_2.11-$currentKamanjaVersion.jar:$KAMANJA_HOME/lib/system/KamanjaInternalDeps_2.11-$currentKamanjaVersion.jar:$KAMANJA_HOME/lib/system/kafka-clients-0.10.0.0.jar:$KAMANJA_HOME/lib/system/simplekafkaproducer_v10_2.11-$currentKamanjaVersion.jar com.ligadata.tools.kafkaProducer_v10.SimpleKafkaProducer --gz true --topics "testin_1" --threads 1 --topicpartitions 8 --brokerlist "$kafkahostname" --files $INPUTFILE  --partitionkeyidxs "1" --format CSV $SECURITY_PROP_OPT
fi

