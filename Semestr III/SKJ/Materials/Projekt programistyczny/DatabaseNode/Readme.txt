Topic: Distributed Database
Author: Jakub Szarpak

Description:

This project is a distributed database. To create a database you need to connect few DatabaseNode using "-connect <address>:<port>" operation.

To generate database node you need to run "src/DatabaseNode/Main/DatabaseNode.java" with "-tcpport <port number> -record <key>:<value>" arguments. The "-connect <address>:<port>" operation is additional command which is used to connect this node to another.
This distributed database has ring topology, which means, no mater how you connect the nodes, they will always create a ring "Image_15.jpg".
Database with first node looks like "Image_1.jpg". Node points to next node "Image_2.jpg" and previous node "Image_3.jpg". At first, next node and previous node are the same node (node points at itself "Image_1.jpg"). Better look to what are next node and previous node is in "Image_16.jpg".
Adding second node changes database to look like "Image_4.jpg". Now node's next node is "new node" and node's previous node is also "new node". New node's next node is "node" and new node's previous node is also "node".
Adding third node to first node looks like "Image_5.jpg".
Adding third node to second node looks like "Image_6.jpg".
You can add as many nodes as you like and structure will always be the same "Image_15.jpg".

Client can affect the database. Exemplary database client "src/DatabaseNode/DatabaseClient/DatabaseClient.java".
Client should run with arguments "-gateway <address>:<port number> -operation <operation with parameters>".
Client have few allowed operations:
    -set-value <key>:<value> - set value on key <key> to <value>. Returns communicate "OK" if key was found and value is set or "ERROR" if key was not found and value is not set.
    -get-value <key> - returns communique "<key>:<value>" if key was found or "ERROR" if kay was not found.
    -find-key <key> - returns "<address>:<value>" if key was found or "ERROR" if key was not found.
    -get-max - returns communique "<key>:<value>" of the biggest value in database.
    -get-min - returns communique "<key>:<value>" of the smallest value in database.
    -new-record <key>:<value> - sets new key and new value on node to which the request was sent.
    -terminate - shutdown the node to which the request was sent.
Client can use one operation at the time.
Few clients can connect to database at the same time.

The trip of request is shown in images from "Image_7.jpg" to "Image_14.jpg".
Client connects to node. If this node have information, then it returns result of an operation. If this node does not have information, then request is forwarded to its next node "Image_8.jpg".
It goes till information is found or node's next node it the starting one.
In first case, if some node has required information, then result of an operation start its way back "Image_11.jpg". It finally goes to the client "Image_14.jpg".
In second case, "ERROR" message goes back to client.
Operations like "get-max" or "get-min" every time go to every node of the web.
Operation "terminate" goes only to one node of the web, but this node sends notifications to his next node and previous node to inform them about shutdown. It uses "newPrevious <address>:<port>" operation for that.


Description of the individual elements of the DistributedDatabase:

- DatabaseNode:
    DatabaseNode class is the Main class. It recognizes information given in arguments of project to start the server. It uses "recognizeCommand()" method to assign the fields.
    After recognizing and assigning the fields, "main()" method can set up the server.

    DatabaseNode can be created with or without stored value.
    If server need to be hooked to the web, then "connect()" method need to be called with destination address and destination port values.
    Connecting server to the node replaces node's next node with server, and node's next node's previous node is set to this server. So this server is added between node and node's next node "Image_5.jpg", "Image_6.jpg".
    Now server is set up.

    * recognizeCommand(String[] commands):x
        This method recognizes and divides commands from "commands" array.
        At first, fields are set to error values like "" or -1.
        Then three patterns are created:
            "portPattern" for recognizing the area with port.
            "connectionPattern" for recognizing the area with connection data.
            "recordPattern" for recognizing the area with value.

        Next in for loop every pattern gets recognized and the fields are assigned with correct values.


- DatabaseNodeCenter:
    DatabaseNodeCenter class is the heart of the server.
    It has thirteen fields:
        "PORT" - public field with local port number.
        "ADDRESS" - public field with local ip address text.
        "nextNodePort" - private field with port number of next connected node.
        "nextNodeAddress" - private field with InetAddress of next connected node.
        "previousNodePort" - private field with port number of previous connected node.
        "previousNodeAddress" - private field with InetAddress of previous connected node.
        "nodeCommunication" - private field which is a reference to NodeCommunication class which is used to communicate between nodes.
        "socketListener" - private field which is a reference to SocketListener class which is used to listen for new nodes or new clients.
        "key" - private field with key number of stored value.
        "value" - private field with stored value number.
        "wait" - public filed which tells if node communication should wait.
        "running" - public filed which tells if server is running.
        "sendNext" - private field which tells if request can be sent to next node.

    There are two constructors whose only deference is that one accepts key and value to be stored and second does not. Their task is to assign fields.
    There are getters and setters for "nextNodePort" and "nextNodeAddress".

    * connect(String destinationAddress, int destinationPort):
        This method is used to connect to the web. Its parameters are String "destinationAddress" and int "destinationPort", whose are ip address and port of the node that it tries to connect.
        It creates TCP connection with node that it tries to connect (That node will be called "previous node").
        Then it sends "Node" message to let previous node know that it is a correct node, neither client nor inappropriate server.
        Next it sends its PORT and reads line of text. It checks if previous node allows to continue connection or if connection is already broke.
        If response is "ERROR", then it means that connection can not be executed.
        The previous node responses are printed and "connect" method ends.
        Printed information should look like this:
            "ERROR"
            "PORT ALREADY CONNECTED"

        If response is not "ERROR", then connection can be executed and "nextNodePort" field is declared.
        Next, by reading next line "nextNodeAddress" field is declared.
        These new "nextNodePort" and "nextNodeAddress" are now pointing to first node of the web. It provides ring topology (look img1.img).
        Next it assigns previous node port and address to "previousNodeAddress" and "previousNodePort" fields.
        At the end, TCP connection can be closed by calling "Socket.close()"" method.
        Information "Connected to the node" is printed.

    * prepareCommunication(int timeout):
        This method is used to do operations that helps with reliability of the communication between nodes. Its parameter is int "timeout", which is a timeOut of UDP connection between this server and nodes.
        it sleeps current thread for 500 milliseconds to let and node communication re-roll. Then it sets nodeCommunication timeout to given "timeout" in the parameter using "setTimeOut()" method.

    * operate(String task):
        This method coordinates every request. If either node or client sends any request, then this method is called to divide tasks. Its parameter is "String task", which is a line of request that was given to this server.
        Thanks to project structure this is one of two synchronized parts of whole project.

        At first, it prepares by creating variables "String answer" and makes it an empty text, "boolean addYourPort", which tells if node should add its port to the request that is forwarded, then setting "wait" field to true, and finally by calling "prepareCommunication" method with timeout 0, which means, there will be no timeout.
        Then it creates ten patterns. Any but the first and last two are for every possible client operation. Last two are only for nodes.
            "checkPortPattern" - for checking if first four characters of request are a digits.
                                    If yes, then it means this is a request from node which port are that for digits.
                                    If no, then it means it is a request from a client.
            "setValuePattern" - for recognizing "set-value" operation.
            "getValuePattern" - for recognizing "get-value" operation.
            "findKeyPattern" - for recognizing "find-key" operation.
            "getMaxPattern" - for recognizing "get-max" operation.
            "getMinPattern" - for recognizing "get-min" operation.
            "newRecordPattern" - for recognizing "new-record" operation.
            "terminatePattern" - for recognizing "terminate" operation.
            "newNextPattern" - for recognizing "newNext" operation which is used to sets "nextNodePort" and "nextNodeAddress" to new one.
            "newPreviousPattern"- for recognizing "newPrevious" operation which is used to sets "previousNodePort" and "previousNodeAddress" to new one.
            "find-port" - for recognizing "find-port" operation which is used to search if some node in the web already has that given port. This operation is used only in SocketListener if some node wants to connect to the web.

        Firstly, it checks if forwarding the request is possible, by matching first pattern.
        If pattern is found, then it checks if "nextNodePort" not equals founded port. Result is assigned to "sendNext" field.
        If pattern is not found, then it checks if "nextNodePort" not equals "PORT". Result is assigned to "sendNext" field.

        It checks every next pattern till some operation is done, because it allows only one operation at the time.
        If some pattern is found, then it means that this is the operation that needs to be done.
        Proper method is called with proper parameters. That method result is the answer to the request.
        Operation "terminate" is different. It calls "terminate" method, makes "answer" variable to "OK" and starts new thread which changes "running" variable to false and after 5000 milliseconds shuts down whole server. Finally, it returns response from "endOperate()" method the same way as previous.

        At the end of every match, "endOperate()" method is called to handle everything that needs to be done before the end of "operate()" method. The replay of "endOperate()" method is returned.
        In case no pattern matches, same thing happens at the end of "operate()" method.

    * endOperate(String task, String answer):
        This method does everything that need to be done before "operate()" method could end.
        Its parameters are String "task" and String "answer", whose are task and answer from operate.

        Firstly, it calls "prepareCommunication()" method with 500 milliseconds timeout to reset nodeCommunication to initial setting.
        Next "wait" field is changed to false.
        Finally, if returns given "task" if given "answer" is empty or given "answer" if it is not empty.

    * forwardRequest(String msg, String failure):
        This method checks if request can be forwarded by checking "sendNext" field.
        If yes, then it forwards given "msg", by calling "sendMsg()" method on nodeCommunication, to next node. It returns result of "receiveMsg()" method on nodeCommunication.
        If node, then it returns "failure", which is a text message of failing the task.


        In next part parameters of methods will be similar to each other.
        The similarities are:
            String "prefix" - this is a port number in text format.
                            If request comes from client, then prefix would be empty. It tells the node, that it comes from a client, and this node will be called "first node".
                            If request comes from another node, then it has a prefix. This prefix is port number of "first node". That means, the request can be forwarded till "nextNodePort" equals this prefix.
                            All of that logic is done in "operate()" method, and it gives the answer as "addYourPort".

            boolean "addYourPort" - this is a bool parameter which tells if this node is "first node" of this request.
                            If yes, then if request is forwarded, "PORT" of this node need to be added as a "prefix" to this request.
                            If no, then it if request is forwarded, "prefix" need to be added to this request.

    * setValue(int key, int value, String prefix, boolean addYourPort):
        This method sets new, given "value" for given "key".
        If this node's key equals given "key", then "Ok" is returned.
        Else this method returns result of calling "forwardRequest()" method with forwarded request and "ERROR" as failure message.

        If "addYourPort" is true, then forwarded request looks like "<PORT>set-value <key>:<values>".
        if "addYourPort" is false, then forwarded request looks like "<prefix>set-values <key>:<values>".

    * getValue(int key, String prefix, boolean addYourPort):
        This method returns <key>:<value> answer if "key" field equals given "key".
        If they are not equal, then method returns result of calling "forwardRequest()" method with forwarded request and "ERROR" as failure message.

        If "addYourPort" is true, then forwarded request looks like "<PORT>get-value <key>".
        if "addYourPort" is false, then forwarded request looks like "<prefix>get-values <key>".

    * findKey(int key, String prefix, boolean addYourPort):
        this method returns "<ADDRESS>:<PORT>" answer if "key" field equals given "key".
        If they are not equal, then method returns result of calling "forwardRequest()" method with forwarded request and "ERROR" as failure message.

        If "addYourPort" is true, then forwarded request looks like "<PORT>find-key <key>".
        if "addYourPort" is false, then forwarded request looks like "<prefix>find-key <key>".

    * getMax(int max, String prefix, boolean addYourPort):
        This method checks if "value" field is bigger than given "max" number. Bigger one is assigned to "newMax" variable.
        It returns result of calling "forwardRequest()" method with forwarded request and "newMax" variable as failure message.

        If "addYourPort" is true, then forwarded request looks like "<PORT>get-max <newMax>".
        if "addYourPort" is false, then forwarded request looks like "<prefix>get-max <newMax>".

    * getMin(int min, String prefix, boolean addYourPort):
        This method checks if "value" field is smaller than given "min" number. Smaller one is assigned to "newMin" variable.
        It returns result of calling "forwardRequest()" method with forwarded request and "newMin" variable as failure message.

        If "addYourPort" is true, then forwarded request looks like "<PORT>get-min <newMin>".
        if "addYourPort" is false, then forwarded request looks like "<prefix>get-min <newMin>".

    * newRecord(int key, int value):
        This method assigns "key" and "value" fields with new, given numbers

    * terminate():
        This method sends to next node "newPrevious" operation message with "previousNodeAddress" and "previousNodePort"
        and sends to previous node "newNext" operation message with "nextNodeAddress" and "nextNodePort".

        sent messages should look like:
            "newPrevious <previousNodeAddress>:<previousNodePort>"
            "newNext <nextNodeAddress>:<nextNodePort>"

    * findPort(int port, String prefix, boolean addYourPort):
        This checks if given "port" is already connected to the web.
        It returns "ERROR" if "PORT" field equals given "port".
        Else it returns result of calling "forwardRequest()" method with forwarded request and "OK" as failure message.

        If "addYourPort" is true, then forwarded request looks like "<PORT>find-port <port>".
        if "addYourPort" is false, then forwarded request looks like "<prefix>find-port <port>".

- NodeCommunication:
    NodeCommunication class is a UDP server, which task is to communicate with other node's UDP servers.
    It has four fields:
        "nodeSocket" - private field with reference to DatagramSocket.
        "node" - private final field with reference to DatagramNode which "this" node.
        "sourcePort" - private field with port of the UDP server from whom a message was received.
        "sourceAddress" - private field with address of the UDP server from whom a message was received.

    There is a constructor which job is to assign "node" field and start "RequestListener" thread.

    * createSocket(int localPort):
        This method tries to create UDP server on given "localPort" with time of 500 milliseconds.

    * receiveMsg():
        This method received message from other UDP servers and assigns "sourcePort" and "sourceAddress" fields with sourced UDP server information.
        It also prints "Message received from <sourcePort> <the message>".
        It returns the message in String format
        If there was no message received, then returns "null" message.

    * sendMsg(String msg, InetAddress destinationAddress, int destinationPort):
        This method sends given "msg" to UDP server on "given destinationAddress" and "destinationPort"
        It also prints "Message send from <this UDP server PORT> to <destinationPort> <msg>"
        If there is a problem with sending the message, then it prints "Message send ERROR".

    * setTimeOut(int timeout):
        This method sets "nodeSocket" timeout by calling "setSoTimeout()" method.

    *- RequestListener
        This inner class is a thread, which job is to receive message over and over again.
        If "node.wait" field is true, then is does not call "receiveMsg()" method.
        Else if nodeSocket exists it receives messages and checks if this message not equals "null".
        If no, then it means there was no message, just some message receiving problem.
        If yes, then it means there was actual message received. Then it remembers "sourceAddress" and "sourcePort", calls "operate()" method with that received message on "node".
        Then it sends the result of "operate()" method back to UDP server on "sourceAddress" and "sourcePort".

- SocketListener:
    This class is a TCP server thread, which task is to handle TCP connections.
    It has one field:
        "node" - private final field with reference to DatagramNode which "this" node.

    There is a constructor which job is to assign "node" field.

    * run():
        This method is run method from Runnable interface. Its listens for incoming TCP connections.
        In constant while loop "socket" variable, which is a Socket, is assigned by calling "accept()" on "server" variable, which is ServerSocket.
        Then this assigned "socket" variable is sent to "recognizeSocket()" method.

    * recognizeSocket(Socket socket):
        This method communicate with given "socket" and handle it properly.
        It communicates by BufferedReader "read" variable and PrintWriter "write" variable.
        Firstly, it reads first message.
            If it is "Node", then it means a node is tying to connect to this node. Then next message is read. This message is this new node's port. Then "operate()" method is called with "find-port <port>" operation.
                If its response is "Ok", it means that there is no node in the web with this port and this new node can be connected.
                    Then "nextNodePort" and "nextNodeAddress" are got from "node" field, and they are sent using "println()" method on "write" variable.
                    Then "nextNodePort" and "nextNodeAddress" from "node" field are set to socket's address and given socket port.
                    Next "Node connected" message is printed.

                If its response is "ERROR", it means there is node with this port in the web and connection can not be done.
                    Messages "ERROR" and "PORT ALREADY CONNECTED" are sent using "println()" method on "write" variable and "Node rejected" is printed.

                After all socket is closed.

            If it is "Client", then it means a client is trying to communicate with this node.
                new TCPClient is stared to handle this client and "Client connected" is printed.

                After all socket is still not closed.

            If it is any other option, then is means the node can not recognize it and sends "Not recognized" message using "println()" method on "write" variable.
                Then "Not recognized socket :: ip = " with address of given "socket" is printed and socket is closed.

- TCPClient
    This class is a thread that handles a TCP client which communicated with this node.
    It has two fields:
        "clientSocket" - private final field with reference to socket that connected to this node.
        "node" - private final field with reference to DatagramNode which "this" node.

    There is a constructor which task is to assign two fields.

    * run():
        This method is run method from Runnable interface. Anything that need to be done with client is handled here.
        It reads message that should be one of the operations. Then it sends this message to "operate()" method on "node" field and resends the answer to connected client.
        After that "clientSocket" is closed.