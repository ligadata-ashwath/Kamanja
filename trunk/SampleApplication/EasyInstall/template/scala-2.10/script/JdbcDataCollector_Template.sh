#!/usr/bin/env bash

KAMANJA_HOME={InstallDirectory}

java -Dlog4j.configurationFile=file:$KAMANJA_HOME/config/log4j2.xml -cp $KAMANJA_HOME/lib/system/ExtDependencyLibs2_2.10-1.4.1.jar:$KAMANJA_HOME/lib/system/ExtDependencyLibs_2.10-1.4.1.jar:$KAMANJA_HOME/lib/system/KamanjaInternalDeps_2.10-1.4.1.jar:$KAMANJA_HOME/lib/system/jdbcdatacollector_2.10-1.4.1.jar com.ligadata.JdbcDataCollector.RunJdbcCollector "$@"

