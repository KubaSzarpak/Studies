Topic: Distributed Database
Author: Jakub Szarpak

Description:





Description of how the individual elements of the code work:

- Code:
    Code class recognizes information given in arguments of project, to start the server. It uses "recognizeCommand()" method to assign the fields.
    After recognizing and assigning the fields, Main can set up the server.

    Code can be created with or without stored value.
    If server need to be hooked to the web, then "connect()" method need to be called with destination address and destination port.
    Connecting server to the node have to be done very precisely, because if node that you try to connect already have connected node, then you should not try to connect with it. It will break the existing connection and replace it with yours.
    Now server is set up.

    * recognizeCommand(String[] commands):
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

    There are two constructors whose only deference is that one accepts value to be stored and second does not. Their task is to assign fields.
    There are getters and setters for "nextNodePort" and "nextNodeAddress".

    * connect(String destinationAddress, int destinationPort):
        This method is used to connect to the web. Its parameters are String "destinationAddress" and int "destinationPort", whose are ip address and port of the node that it tries to connect.
        It creates TCP connection with node that it tries to connect (That node will be called "previous node").
        Then it sends "Node" message to let previous node know that it is a correct node, neither client nor inappropriate server.
        Next it sends its PORT and reads line of text. It checks if previous node allows to continue connection or if connection is already broke.
        If response is "ERROR", then it means that connection can not be executed.
        The previous node responses are printed and "connect" method ends.
        Printed information should look like this:
            "ERROR
            PORT ALREADY CONNECTED"

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
        "node" - private final field with references to its node DatagramNode.
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
    This class is a TCP server