start java DatabaseNode -tcpport 4444 -record 1:100
timeout 1 > NUL
java DatabaseClient -gateway localhost:4444 -operation terminate
