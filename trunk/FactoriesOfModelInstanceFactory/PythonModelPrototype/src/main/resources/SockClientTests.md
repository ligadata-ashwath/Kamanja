rich@pepper:~/github1/dev/r1.5.0/kamanja/trunk/FactoriesOfModelInstanceFactory/PythonModelPrototype/src/main/scala$  ./SockClient.scala

  One of the following must be supplied. Currently only one command at a time is permitted.

  SocketClient.scala <named args...>
  where <named args> are:

    --cmd startServer   [--host <hostname or ip> ... default = localhost]
                        [--port <user port no.> ... default=9999]
                        [--user <userId.> ... default="kamanja"]
                        --pyPath <location of Kamanja python installation>
    --cmd stopServer    [--host <hostname or ip> ... default = localhost]
                        [--port <user port no.> ... default=9999]
                        [--user <userId.> ... default="kamanja"]
    --cmd addModel      [--host <hostname or ip> ... default = localhost]
                        [--port <user port no.> ... default=9999]
                        [--user <userId.> ... default="kamanja"]
                        --filePath <filePath>
                        --modelOptions <JSON string (map) defining with input and output messages.  Keys are InputMsgs
                        and OutputMsgs respectively. InputMsgs and OutputMsgs values are arrays (there can be multiple
                        messages ingested by a model or produced by one).  Other values can be presented in the map
                        as necessary to be utilized by the model instance that will run on the server.
                        --pyPath <location of Kamanja python installation>
    --cmd removeModel   [--host <hostname or ip> ... default = localhost]
                        [--port <user port no.> ... default=9999]
                        [--user <userId.> ... default="kamanja"]
                        --modelName <modelName>
    --cmd serverStatus  [--host <hostname or ip> ... default = localhost]
                        [--port <user port no.> ... default=9999]
                        [--user <userId.> ... default="kamanja"]
    --cmd executeModel  [--host <hostname or ip> ... default = localhost]
                        [--port <user port no.> ... default=9999]
                        [--user <userId.> ... default="kamanja"]
                        --modelName '<modelName>'
                        --msg '<msg data expressed as json map string>'
    --cmd executeModel  [--host <hostname or ip> ... default = localhost]
                        [--port <user port no.> ... default=9999]
                        [--user <userId.> ... default="kamanja"]
                        --modelName '<modelName>'
                        --filePath '<msg file path>'


  It is possible to run multiple servers on same host simply by varying 
  the port number.


**Script Setup**
export ipport=8998
export CLASSPATH=/tmp/drdigital/Kamanja-1.4.1_2.11/lib/system/ExtDependencyLibs_2.11-1.4.1.jar:/tmp/drdigital/Kamanja-1.4.1_2.11/lib/system/KamanjaInternalDeps_2.11-1.4.1.jar:/tmp/drdigital/Kamanja-1.4.1_2.11/lib/system/ExtDependencyLibs2_2.11-1.4.1.jar
export METADATA=~/github1/dev/r1.5.0/kamanja/trunk/FactoriesOfModelInstanceFactory/PythonModelPrototype/src/main/resources/metadata
export KAMANJAPYPATH=~/github1/dev/r1.5.0/kamanja/trunk/FactoriesOfModelInstanceFactory/PythonModelPrototype/src/main/python
export DATA=~/github1/dev/r1.5.0/kamanja/trunk/FactoriesOfModelInstanceFactory/PythonModelPrototype/src/main/resources/data

**Start Server Commands**
SockClient.scala --cmd startServer --user kamanaja --host localhost --port 9999 --pyPath $KAMANJAPYPATH

./pythonserver.py --user kamanaja --host localhost --port 9999 --pyPath $KAMANJAPYPATH

SockClient.scala --cmd startServer --user kamanaja --host localhost --port 9998 --pyPath $KAMANJAPYPATH

**Stop Server Commands**
SockClient.scala --cmd stopServer --user kamanaja --host localhost --port 9999

SockClient.scala --cmd stopServer --user kamanaja --host localhost --port 9998

**Add Model Commands**
_port 9999_
SockClient.scala --cmd addModel --filePath $METADATA/model/add.py --modelName AddTuple --user kamanaja --modelOptions '{"InputMsgs": [{"name": "org.kamanja.arithmetic.arithmeticMsg", "Fields": {"a": "Int", "b": "Int"} } ], "OutputMsgs": [{"name": "org.kamanja.arithmetic.arithmeticOutMsg", "Fields": {"a": "Int", "b": "Int", "result": "Int"} } ] }'  --host localhost --port 9999 --pyPath $KAMANJAPYPATH

SockClient.scala --cmd addModel --filePath $METADATA/model/subtract.py --modelName SubtractTuple --user kamanaja --modelOptions '{"InputMsgs": [{"name": "org.kamanja.arithmetic.arithmeticMsg", "Fields": {"a": "Int", "b": "Int"} } ], "OutputMsgs": [{"name": "org.kamanja.arithmetic.arithmeticOutMsg", "Fields": {"a": "Int", "b": "Int", "result": "Int"} } ] }'  --host localhost --port 9999 --pyPath $KAMANJAPYPATH

SockClient.scala --cmd addModel --filePath $METADATA/model/multiply.py --modelName MultiplyTuple --user kamanaja --modelOptions '{"InputMsgs": [{"name": "org.kamanja.arithmetic.arithmeticMsg", "Fields": {"a": "Int", "b": "Int"} } ], "OutputMsgs": [{"name": "org.kamanja.arithmetic.arithmeticOutMsg", "Fields": {"a": "Int", "b": "Int", "result": "Int"} } ] }'  --host localhost --port 9999 --pyPath $KAMANJAPYPATH

SockClient.scala --cmd addModel --filePath $METADATA/model/divide.py --modelName DivideTuple --user kamanaja --modelOptions '{"InputMsgs": [{"name": "org.kamanja.arithmetic.arithmeticMsg", "Fields": {"a": "Int", "b": "Int"} } ], "OutputMsgs": [{"name": "org.kamanja.arithmetic.arithmeticOutMsg", "Fields": {"a": "Int", "b": "Int", "result": "Int"} } ] }'  --host localhost --port 9999 --pyPath $KAMANJAPYPATH

_port 9998_
SockClient.scala --cmd addModel --filePath $METADATA/model/add.py --modelName AddTuple --user kamanaja --modelOptions '{"InputMsgs": [{"name": "org.kamanja.arithmetic.arithmeticMsg", "Fields": {"a": "Int", "b": "Int"} } ], "OutputMsgs": [{"name": "org.kamanja.arithmetic.arithmeticOutMsg", "Fields": {"a": "Int", "b": "Int", "result": "Int"} } ] }'  --host localhost --port 9998 --pyPath $KAMANJAPYPATH

SockClient.scala --cmd addModel --filePath $METADATA/model/subtract.py --modelName SubtractTuple --user kamanaja --modelOptions '{"InputMsgs": [{"name": "org.kamanja.arithmetic.arithmeticMsg", "Fields": {"a": "Int", "b": "Int"} } ], "OutputMsgs": [{"name": "org.kamanja.arithmetic.arithmeticOutMsg", "Fields": {"a": "Int", "b": "Int", "result": "Int"} } ] }'  --host localhost --port 9998 --pyPath $KAMANJAPYPATH

SockClient.scala --cmd addModel --filePath $METADATA/model/multiply.py --modelName MultiplyTuple --user kamanaja --modelOptions '{"InputMsgs": [{"name": "org.kamanja.arithmetic.arithmeticMsg", "Fields": {"a": "Int", "b": "Int"} } ], "OutputMsgs": [{"name": "org.kamanja.arithmetic.arithmeticOutMsg", "Fields": {"a": "Int", "b": "Int", "result": "Int"} } ] }'  --host localhost --port 9998 --pyPath $KAMANJAPYPATH

SockClient.scala --cmd addModel --filePath $METADATA/model/divide.py --modelName DivideTuple --user kamanaja --modelOptions '{"InputMsgs": [{"name": "org.kamanja.arithmetic.arithmeticMsg", "Fields": {"a": "Int", "b": "Int"} } ], "OutputMsgs": [{"name": "org.kamanja.arithmetic.arithmeticOutMsg", "Fields": {"a": "Int", "b": "Int", "result": "Int"} } ] }'  --host localhost --port 9998 --pyPath $KAMANJAPYPATH

**Remove Model Commands**
SockClient.scala --cmd removeModel --modelName AddTuple --user kamanaja --host localhost --port 9999
SockClient.scala --cmd removeModel --modelName SubtractTuple --user kamanaja --host localhost --port 9999
SockClient.scala --cmd removeModel --modelName MultiplyTuple --user kamanaja --host localhost --port 9999
SockClient.scala --cmd removeModel --modelName DivideTuple --user kamanaja --host localhost --port 9999

SockClient.scala --cmd removeModel --modelName AddTuple --user kamanaja --host localhost --port 9998
SockClient.scala --cmd removeModel --modelName SubtractTuple --user kamanaja --host localhost --port 9998
SockClient.scala --cmd removeModel --modelName MultiplyTuple --user kamanaja --host localhost --port 9998
SockClient.scala --cmd removeModel --modelName DivideTuple --user kamanaja --host localhost --port 9998

**Server Status Commands**
SockClient.scala --cmd serverStatus --user kamanaja --host localhost --port 9999

SockClient.scala --cmd serverStatus --user kamanaja --host localhost --port 9998

**Execute Model Commands (single message)**
SockClient.scala --cmd executeModel --modelName AddTuple --msg '[{"name": "org.kamanja.arithmetic.arithmeticMsg", "fields": {"a": 1, "b": 2 } } ]'  --user kamanaja --host localhost --port 9999

SockClient.scala --cmd executeModel --modelName AddTuple --msg '[{"name": "org.kamanja.arithmetic.arithmeticMsg", "fields": {"a": 1, "b": 2 } } ]'  --user kamanaja --host localhost --port 9998

**Execute Model Commands (data from csv file with header record)**
SockClient.scala --cmd executeModel --modelName AddTuple --filePath $DATA/arithmeticData.txt --user kamanaja --host localhost --port 9999

SockClient.scala --cmd executeModel --modelName AddTuple --filePath $DATA/arithmeticData.txt --user kamanaja --host localhost --port 9998

**------------------------------------------------------------**
**Retrieving pid from server on port 9998 from 'ps aux' output**
**------------------------------------------------------------**

ps aux | grep python | grep '\-\-port 9998' | sed 's/^[A-Za-z0-9][A-Za-z0-9]*[\t ][\t ]*\([0-9][0-9]*\).*$/\1/g'

**-----------------------------------**
**INFORMATION ABOUT MESSAGES AND DATA**
**-----------------------------------**

**The Arithmetic message metadata supplied to the AddModel commands**
{"InputMsgs": [{"name": "org.kamanja.arithmetic.arithmeticMsg", "Fields": {"a": "Int", "b": "Int"} } ], "OutputMsgs": [{"name": "org.kamanja.arithmetic.arithmeticOutMsg", "Fields": {"a": "Int", "b": "Int", "result": "Int"} } ] }

**Sample Arithmetic message data supplied to the ExecuteModel commands**
[{"name": "org.kamanja.arithmetic.arithmeticMsg", "fields": {"a": 1, "b": 2 } } 

**Sample Arithmetic message data in the $DATA/arithmeticData.txt**
__csv data with header whose values match input msg_
a,b
1,1
2,2
3,3
4,4
5,5
6,6
7,7
8,8
9,9


java -Xdebug -Xrunjdwp:transport=dt_socket,address="$ipport",server=y -Dlog4j.configurationFile=file:/tmp/drdigital/Kamanja-1.4.1_2.11/config/log4j2.xml -cp /tmp/drdigital/Kamanja-1.4.1_2.11/lib/system/ExtDependencyLibs2_2.11-1.4.1.jar:/tmp/drdigital/Kamanja-1.4.1_2.11/lib/system/ExtDependencyLibs_2.11-1.4.1.jar:/tmp/drdigital/Kamanja-1.4.1_2.11/lib/system/KamanjaInternalDeps_2.11-1.4.1.jar:/tmp/drdigital/Kamanja-1.4.1_2.11/lib/system/kamanjamanager_2.11-1.4.1.jar:/home/rich/github1/dev/r1.5.0/kamanja/trunk/FactoriesOfModelInstanceFactory/PythonModelPrototype/target/scala-2.11/pythonmodelprototype_2.11-1.0.jar SockClient --cmd startServer --user kamanaja --host localhost --port 9999 --pyPath $KAMANJAPYPATH

java -Xdebug  -cp /tmp/drdigital/Kamanja-1.4.1_2.11/lib/system/ExtDependencyLibs2_2.11-1.4.1.jar:/tmp/drdigital/Kamanja-1.4.1_2.11/lib/system/ExtDependencyLibs_2.11-1.4.1.jar:/tmp/drdigital/Kamanja-1.4.1_2.11/lib/system/KamanjaInternalDeps_2.11-1.4.1.jar:/tmp/drdigital/Kamanja-1.4.1_2.11/lib/system/kamanjamanager_2.11-1.4.1.jar:/home/rich/github1/dev/r1.5.0/kamanja/trunk/FactoriesOfModelInstanceFactory/PythonModelPrototype/target/scala-2.11/pythonmodelprototype_2.11-1.0.jar SockClient --cmd startServer --user kamanaja --host localhost --port 9999 --pyPath $KAMANJAPYPATH


{
  "result": "Server started successfully",
  "pid": 22537
}


java  -cp /tmp/drdigital/Kamanja-1.4.1_2.11/lib/system/ExtDependencyLibs2_2.11-1.4.1.jar:/tmp/drdigital/Kamanja-1.4.1_2.11/lib/system/ExtDependencyLibs_2.11-1.4.1.jar:/tmp/drdigital/Kamanja-1.4.1_2.11/lib/system/KamanjaInternalDeps_2.11-1.4.1.jar:/tmp/drdigital/Kamanja-1.4.1_2.11/lib/system/kamanjamanager_2.11-1.4.1.jar:/home/rich/github1/dev/r1.5.0/kamanja/trunk/FactoriesOfModelInstanceFactory/PythonModelPrototype/target/scala-2.11/pythonmodelprototype_2.11-1.0.jar SockClient --cmd addModel --filePath $METADATA/model/add.py --modelName AddTuple --user kamanaja --modelOptions '{"a": "Int", "b": "Int"}'  --host localhost --port 9999 --pyPath $KAMANJAPYPATH

java -Xdebug -Xrunjdwp:transport=dt_socket,address="$ipport",server=y -Dlog4j.configurationFile=file:/tmp/drdigital/Kamanja-1.4.1_2.11/config/log4j2.xml -cp /tmp/drdigital/Kamanja-1.4.1_2.11/lib/system/ExtDependencyLibs2_2.11-1.4.1.jar:/tmp/drdigital/Kamanja-1.4.1_2.11/lib/system/ExtDependencyLibs_2.11-1.4.1.jar:/tmp/drdigital/Kamanja-1.4.1_2.11/lib/system/KamanjaInternalDeps_2.11-1.4.1.jar:/tmp/drdigital/Kamanja-1.4.1_2.11/lib/system/kamanjamanager_2.11-1.4.1.jar:/home/rich/github1/dev/r1.5.0/kamanja/trunk/FactoriesOfModelInstanceFactory/PythonModelPrototype/target/scala-2.11/pythonmodelprototype_2.11-1.0.jar SockClient --cmd addModel --filePath $METADATA/model/add.py --modelName AddTuple --user kamanaja --modelOptions '{"a": "Int", "b": "Int"}'  --host localhost --port 9999 --pyPath $KAMANJAPYPATH





