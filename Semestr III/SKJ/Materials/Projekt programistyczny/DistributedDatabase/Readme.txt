- Main:
    Main class recognizes information to start node. It uses method "recognizeCommand" to assign the fields.

    * recognizeCommand:
        At first, fields are set to error values like "" or -1.
        Then three patterns are created:
            "portPattern" for recognizing the area with port.
            "connectionPattern" for recognizing the area with connection data.
            "recordPattern" for recognizing the area with value.

        Next in for loop every pattern gets recognized and the fields are assigned with correct values.

    After recognizing and assigning the fields, Main can set up the server.
    DatabaseNode can be created with or without stored value.
    If server need to be hooked to the web, then "connect" method need to be called with destination address and destination port.
    Connecting server to another server have to be done very precisely, because if node that you try to connect already have connected node, then you shouldn't try to connect with it. It will break the connection and replace it with yours.
    Now server is set up.

- DatabaseNode:
    DatabaseNode class is the center of the server. It has thirteen fields:
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

    * connect:
        This method is used to connect to the web. Its parameters are String "destinationAddress" and int "destinationPort", whose are ip address and port of the node that it tries to connect.
        It creates TCP connection with node that it tries to connect (That node will be called "previous node").
        Then it sends "Node" message to let previous node know that it is a correct node, neither client nor inappropriate server.
        Next it reads two lines of text. The first line is the port and the second line is the ip address of the first node of the web. It assigns them to "nextNodePort" field and "nextNodeAddress" field.
        At the end, it assigns previous node port and address to "previousNodeAddress" and "previousNodePort" fields and closes the TCP connection using Socket.close() method.

    * prepareCommunication:
        This method is used to do operations that helps with reliability of the communication between nodes. Its parameter is int "timeout", which is a timeOut of UDP connection between this server and nodes.
        It sets nodeCommunication timeout to given in the parameter using "setTimeOut" method. Then it sleeps current thread for 500 milliseconds to let and node communication re-roll.

    * operate:
        This method coordinates every request. If either node or client sends any request, then this method is called to divide tasks. Its parameter is "String task", which is a line of request that was given to this server.
        At first, it prepares by creating variables "String answer" and makes it an empty text, "boolean addYourPort", which tells if node should add its port to the request that is forwarded, then setting "wait" field to true, and finally by calling "prepareCommunication" method with timeout 0, which means, there will be no timeout.
        Then it creates ten patterns. Any but the first and last two are for every possible client operation. Last two are only for nodes.
            "checkPortPattern" - for checking if first four characters of request are a digits. If yes, then it means this is a request from node which port is that for digits. If no, then it means it is a request from a client.
            "setValuePattern" - for recognizing "set-value" operation.
            "getValuePattern" - for recognizing "get-value" operation.
            "findKeyPattern" - for recognizing "find-key" operation.
            "getMaxPattern" - for recognizing "get-max" operation.
            "getMinPattern" - for recognizing "get-min" operation.
            "newRecordPattern" - for recognizing "new-record" operation.
            "terminatePattern" - for recognizing "terminate" operation.
            "newNextPattern" - for recognizing "newNext" operation which is used to sets "nextNodePort" and "nextNodeAddress" to new one.
            "newPreviousPattern"- for recognizing "newPrevious" operation which is used to sets "previousNodePort" and "previousNodeAddress" to new one.

        If matcher matches "checkPortPattern" in first four characters of given "task", then "addYourPort" variable is set to false and "sendNext" field is set to false. It also checks if "nextNodePort" equals found four digits. "sendNext" variable is set to the result of this comparison.
        If matcher does not match "checkPortPattern" in first four characters of given "task", then "addYourPort" variable is set to true. It also checks if "nextNodePort" equals local "PORT". "sendNext" variable is set to the result of this comparison.
        Next all client's operation patterns are matched with given "task" in matcher. If matcher finds patter, then it calls a method dedicated to operation. Result of methods is added to "answer" variable.
        Operation "terminate" is different. It calls "terminate" method, adds "OK" to "answer" variable and starts new thread which changes "running" variable to false and after 5000 milliseconds shuts down whole server.

        At the end, it ends by calling "prepareCommunication" method with timeout 500 milliseconds and sets "wait" variable to false, which means, that NodeCommunication is reset.
        Next it checks if answer is empty. If yes, then it means, that none operation had been found and method returns given "task". If not, method returns "answer" variable.

    *




