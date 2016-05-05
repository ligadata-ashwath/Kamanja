#!/usr/bin/env bash
KAMANJA_HOME={InstallDirectory}
java -Dlog4j.configurationFile=file:$KAMANJA_HOME/config/log4j2.xml -cp $KAMANJA_HOME/lib/system/ExtDependencyLibs2_2.11-1.4.0.jar:$KAMANJA_HOME/lib/system/ExtDependencyLibs_2.11-1.4.0.jar:$KAMANJA_HOME/lib/system/KamanjaInternalDeps_2.11-1.4.0.jar:$KAMANJA_HOME/lib/system/kvinit_2.11-1.4.0.jar com.ligadata.tools.kvinit.KVInit --typename com.ligadata.kamanja.samples.containers.GlobalPreferences  --config {InstallDirectory}/config/Engine1Config.properties --datafiles {InstallDirectory}/input/SampleApplications/data/GlobalPreferences_Finance.dat --ignorerecords 1 --deserializer "com.ligadata.kamanja.serializer.csvserdeser" --optionsjson '{"alwaysQuoteFields":false,"fieldDelimiter":",","valDelimiter":"~"}'
java -Dlog4j.configurationFile=file:$KAMANJA_HOME/config/log4j2.xml -cp $KAMANJA_HOME/lib/system/ExtDependencyLibs2_2.11-1.4.0.jar:$KAMANJA_HOME/lib/system/ExtDependencyLibs_2.11-1.4.0.jar:$KAMANJA_HOME/lib/system/KamanjaInternalDeps_2.11-1.4.0.jar:$KAMANJA_HOME/lib/system/kvinit_2.11-1.4.0.jar com.ligadata.tools.kvinit.KVInit --typename com.ligadata.kamanja.samples.containers.CustPreferences    --config {InstallDirectory}/config/Engine1Config.properties --datafiles {InstallDirectory}/input/SampleApplications/data/CustPreferences_Finance.dat --ignorerecords 1 --deserializer "com.ligadata.kamanja.serializer.csvserdeser" --optionsjson '{"alwaysQuoteFields":false,"fieldDelimiter":",","valDelimiter":"~"}'
java -Dlog4j.configurationFile=file:$KAMANJA_HOME/config/log4j2.xml -cp $KAMANJA_HOME/lib/system/ExtDependencyLibs2_2.11-1.4.0.jar:$KAMANJA_HOME/lib/system/ExtDependencyLibs_2.11-1.4.0.jar:$KAMANJA_HOME/lib/system/KamanjaInternalDeps_2.11-1.4.0.jar:$KAMANJA_HOME/lib/system/kvinit_2.11-1.4.0.jar com.ligadata.tools.kvinit.KVInit --typename com.ligadata.kamanja.samples.containers.CustomerInfo       --config {InstallDirectory}/config/Engine1Config.properties --datafiles {InstallDirectory}/input/SampleApplications/data/CustomerInfo_Finance.dat --ignorerecords 1 --deserializer "com.ligadata.kamanja.serializer.csvserdeser" --optionsjson '{"alwaysQuoteFields":false,"fieldDelimiter":",","valDelimiter":"~"}'
