#!/bin/python

import sys
# import socket since we are doing a node to node connection
import socket
from datetime import datetime as dt

# script will take in two arguments
# define target
if len(sys.argv) == 2:
	# similar to $1 in bash, translate hostname to IPv4
	target = socket.gethostbyname(sys.argv[1]) 
else:
	print("Invalid amount of arguments.")
	print("SYNTAX: python3 scanner.py <ip>")

# add a pretty banner
print("-" * 50)
print("Scanning target " + target)
print("Time started: {}".format(dt.now()))
print("-" * 50)

# try statements
try:
	# in real portscanner, range will be 1 to 65535
	for port in range(1,65535):
		#AF_INET = IPv4, SOCK_STREAM = PORT
		print("Scanning port {}".format(port))
		s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
		# if port is not connectable, it will wait 1 second then move on
		socket.setdefaulttimeout(1)
		#returns an error indicator, if port is open then result will be 0, if not ERROR will be returned		
		result = s.connect_ex((target,port))
		if result == 0:
			print("Port {} is open".format(port))
		s.close()
		
except KeyboardInterrupt:
	#if we want to interrup scan we need to identify it
	print("\n Exiting program :)")
	sys.exit()
except socket.gaierror:
	print("Hostname could not be resolved Q.Q")
	sys.exit()
except socket.error:
	print("Couldn't connect to server >:(")
	sys.exit()
