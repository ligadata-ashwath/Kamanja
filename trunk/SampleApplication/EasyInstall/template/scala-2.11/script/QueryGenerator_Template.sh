#!/usr/bin/env bash

KAMANJA_HOME={InstallDirectory}

java -Dlog4j.configurationFile=file:$KAMANJA_HOME/config/log4j2.xml -cp $KAMANJA_HOME/lib/system/orientdb-jdbc-2.1.19-all.jar:$KAMANJA_HOME/lib/system/ExtDependencyLibs2_2.11-1.5.0.jar:$KAMANJA_HOME/lib/system/ExtDependencyLibs_2.11-1.5.0.jar:$KAMANJA_HOME/lib/system/KamanjaInternalDeps_2.11-1.5.0.jar:$KAMANJA_HOME/lib/system/querygenerator_2.11-1.5.0.jar com.ligadata.queryutility.QueryGenerator "$@"