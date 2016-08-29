#!/bin/bash

# StopKamanjaCluster.sh
#
#	NOTE: This script must currently be run from a trunk directory that contains the build installed on the cluster to be run.

Usage()
{
    echo 
    echo "Usage:"
    echo "      StopKamanjaCluster.sh --ClusterId <cluster name identifer> "
    echo "                           --MetadataAPIConfig  <metadataAPICfgPath>  "
    echo "                           [--NodeIds  <nodeIds>] "
    echo 
    echo "  NOTES: Stop the cluster specified by the cluster identifier parameter.  Use the metadata api configuration to locate"
    echo "         the appropriate metadata store.  "
    echo 
}


scalaVersion="2.11"
name1=$1
currentKamanjaVersion=1.5.3


if [[ "$#" -eq 4 || "$#" -eq 6 ]]; then
    echo
else 
    echo "Problem: Incorrect number of arguments"
    echo 
    Usage
    exit 1
fi

if [[ "$name1" != "--MetadataAPIConfig" && "$name1" != "--ClusterId" && "$name1" != "--NodeIds" ]]; then
    echo "Problem: Bad arguments"
    echo 
    Usage
    exit 1
fi

# Collect the named parameters 
metadataAPIConfig=""
clusterId=""
nodeIds=""
valid_nodeIds=();

while [ "$1" != "" ]; do
    echo "parameter is $1"
    case $1 in
        --ClusterId )           shift
                                clusterId=$1
                                ;;
        --MetadataAPIConfig )   shift
                                metadataAPIConfig=$1
                                ;;
        --NodeIds )             shift
                                nodeIds=$1
                                ;;
        * )                     echo "Problem: Argument $1 is invalid named parameter."
                                Usage
                                exit 1
                                ;;
    esac
    shift
done

# 1) Collect the relevant node information for this cluster.
workDir="/tmp" 
ipFile="ip.txt"
ipPathPairFile="ipPath.txt"
ipIdCfgTargPathQuartetFileName="ipIdCfgTarg.txt"
installDir=`cat $metadataAPIConfig | grep '[Rr][Oo][Oo][Tt]_[Dd][Ii][Rr]' | sed 's/.*=\(.*\)$/\1/g'`

echo "...extract node information for the cluster to be started from the Metadata configuration information supplied"

# info is assumed to be present in the supplied metadata store... see trunk/utils/NodeInfoExtract for details 
echo "...Command = java -cp $installDir/lib/system/ExtDependencyLibs2_${scalaVersion}-${currentKamanjaVersion}.jar:$installDir/lib/system/ExtDependencyLibs_${scalaVersion}-${currentKamanjaVersion}.jar:$installDir/lib/system/KamanjaInternalDeps_${scalaVersion}-${currentKamanjaVersion}.jar:$installDir/lib/system/nodeinfoextract_${scalaVersion}-${currentKamanjaVersion}.jar com.ligadata.installer.NodeInfoExtract --MetadataAPIConfig \"$metadataAPIConfig\" --workDir \"$workDir\" --ipFileName \"$ipFile\" --ipPathPairFileName \"$ipPathPairFile\" --ipIdCfgTargPathQuartetFileName \"$ipIdCfgTargPathQuartetFileName\" --installDir \"$installDir\" --clusterId \"$clusterId\""
java -cp $installDir/lib/system/ExtDependencyLibs2_${scalaVersion}-${currentKamanjaVersion}.jar:$installDir/lib/system/ExtDependencyLibs_${scalaVersion}-${currentKamanjaVersion}.jar:$installDir/lib/system/KamanjaInternalDeps_${scalaVersion}-${currentKamanjaVersion}.jar:$installDir/lib/system/nodeinfoextract_${scalaVersion}-${currentKamanjaVersion}.jar com.ligadata.installer.NodeInfoExtract --MetadataAPIConfig $metadataAPIConfig --workDir "$workDir" --ipFileName "$ipFile" --ipPathPairFileName "$ipPathPairFile" --ipIdCfgTargPathQuartetFileName "$ipIdCfgTargPathQuartetFileName" --installDir "$installDir" --clusterId "$clusterId"
if [ "$?" -ne 0 ]; then
    echo
    echo "Problem: Invalid arguments supplied to the NodeInfoExtract-1.0 application... unable to obtain node configuration... exiting."
    Usage
    exit 1
fi

if [[ $nodeIds != "" ]]; then
    OIFS=$IFS;
    IFS=",";
    nodeIdsArray=($nodeIds);
    IFS=$OIFS;

    for ((i=0; i<${#nodeIdsArray[@]}; ++i)); do 
        tmp_str="$(echo -e "${nodeIdsArray[$i]}" | xargs)"
        if [[ "$tmp_str" != "" ]]; then
            valid_nodeIds+=($tmp_str)
        fi
    done
fi

# Start the cluster nodes using the information extracted from the metadata and supplied config.  Remember the jvm's pid in the $installDir/run
# directory setup for that purpose.  The name of the pid file will always be 'node$id.pid'.  The targetPath points to the given cluster's 
# config directory where the Kamanja engine config file is located.
echo "...stopping the Kamanja cluster $clusterName"
exec 12<&0 # save current stdin
exec < "$workDir/$ipIdCfgTargPathQuartetFileName"
while read LINE; do
    machine=$LINE
    read LINE
    id=$LINE
    read LINE
    cfgFile=$LINE
    read LINE
    targetPath=$LINE
    read LINE
    roles=$LINE
    
    if [[ ${#valid_nodeIds[@]} > 0 ]]; then
        currentNodeId=""
        for ((j=0; j<${#valid_nodeIds[@]}; ++j)); do 
            if [[ ${valid_nodeIds[$j]} != $id ]]; then
              continue
            else
                currentNodeId=$id
            fi
        done
        if [[ $currentNodeId != $id ]]; then
          continue
        fi
    fi    

    echo "NodeInfo = $machine, $id, $cfgFile, $targetPath, $roles"
    echo "...On machine $machine, stopping Kamanja node with configuration $cfgFile for nodeId $id to $machine:$targetPath"
    #scp -o StrictHostKeyChecking=no "$cfgFile" "$machine:$targetPath/"
    # 
    # FIXME: something more graceful than killing the jvm is desirable.
    #
    pidfile=node$id.pid
    rm -rf "$workDir/$pidfile"
    scp -o StrictHostKeyChecking=no "$machine:$installDir/run/$pidfile" "$workDir/$pidfile"
    pidvals=`head -1 "$workDir/$pidfile" | sed "s/,/ /g"`

    if [ ! -z "$pidvals" ]; then
       if [ -n "$pidvals" ]; then
            echo "Killing Pid(s):$pidvals on $machine"
           # FIXME: We can check whether we really have pidvals or not and do ssh
           ssh -o StrictHostKeyChecking=no -T $machine  <<-EOF
              kill -15 $pidvals
EOF

       fi
    fi

done
exec 0<&12 12<&-

echo

