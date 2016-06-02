#!/usr/bin/python

# server.py 
import socket                                         
import time
import argparse
import sys
import subprocess
import imp

 
sys.path.insert(0, "/home/rich/github1/dev/r1.5.0/kamanja/trunk/FactoriesOfModelInstanceFactory/PythonModelPrototype/src/main/python") 
#print "sys.modules = " + str(sys.modules)

parser = argparse.ArgumentParser(description='Kamanja PyServer')
parser.add_argument('--port', help='port binding (default=9999)', default="9999")
parser.add_argument('--host', help='host name (default=localhost) ', default="localhost")
args = vars(parser.parse_args())
#print args

# create a socket object
serversocket = socket.socket(
	        socket.AF_INET, socket.SOCK_STREAM) 

# get local machine name
host = socket.gethostname()     
port = int(args['port'])

# bind to the port
print('Connection parms: ' + host + ", " + args['port'])
serversocket.bind((host, port))    

def importPackageByName(moduleName, name):
	"""
	Import the class 'name' found in package 'moduleName'.  The moduleName
	may be a sub-package (e.g., the module is found in the 'commands' 
	sub-package in the use below).
	"""
	try:
		print "load moduleName = " + moduleName 
		module = __import__(moduleName, globals(), locals(), [name])
	except ImportError:
		return None
	return getattr(module, name)


# command dictionary used to manage server loaded below
cmdDict = {
    #"addModel": addModel,  .
    #"stopModel": stopModel,
    }

# model instance dictionary.
modelDict = {
    }

# Modify command dict with the python server commands supporte.
# These are loaded on the server from the 'commands' sub directory specified 
# of a directory on the PYTHONPATH...e.g.,
#
#	export PYTHONPATH=$HOME/python/pyserver
#
# Using the PYTHONPATH makes sense at least for testing.  It might be fruitful
# for production as well... 
#
# A similar approach is used for the model dispatch dictionary.  It is dynamically
# created from content, however, that has been sent to the server on an open
# connection to the server.

# Add the system level command to the dispatcher dict
for extname in 'addModel', 'removeModel', 'serverStatus', 'executeModel', 'stopServer':
	HandlerClass = importPackageByName("commands." + extname, "Handler")
	handler = HandlerClass()
	cmdDict[extname] = handler

# The dispatcher invokes the proper command based upon the first line of the received
# data.  It dispatches the appropriate handler for it, giving the command access to the
# model dictionary, host, port, and remaining args from 
# supplying the remaining arguments/lines from the command as its arg.
def dispatcher(cmdkey, args):
	print "processing cmd = " + cmdkey
	cmd = cmdDict[cmdKey]
	results = cmd.handler(modelDict, host, port, args)
	return results

# queue up to 5 requests... could be another value... as written 
# the conn are serialized... we mitigate by having a pythonserver for each
# partition the engine chooses to distribute the input with.
preserveNewLines=True
serversocket.listen(5)
result = ''
while True:                                         
	conn, addr = serversocket.accept()
	print('Connected by', addr)
	while True:
	    # establish a connection
	    data = conn.recv(1024)
	    if not data: break # no more data... 
	    # split the data on the newline, preserving the new lines, cleaning up those
	    # first elements with strip function.  The first line is the server command. 
	    # The rest are processed by the server command that is dispatched.
	    # 
	    # Adding magic demarcation, xid... those should all be doable.
	    dataList = data.splitlines(preserveNewLines)
	    cmdRaw = dataList.pop(0) # obtain the command from first arg
	    # ... both cmd and dataList changed by pop
	    cmd = cmdRaw.strip()
	    result = dispatcher(cmd,dataList)
	    if result != 'kill-9': # stop command will return 'kill-9' as value
	    	conn.sendall(result) 
	    conn.close()
    	break
	
	if result == 'kill-9': # stop command stops listener tears down server ...
		break

print 'server is exiting due to stop command'
