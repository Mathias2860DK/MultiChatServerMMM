## Chat Server

How does the program work? 

Commands from clients to the server:

CONNECT#<user>  --> Connects the user to the server. The server then validates the user and sends ONLINE command to user.
If <user> is not found --> server will close connection and throw CLOSE#2 //not sure it 'throws'

SEND#<user>#text.. --> who do you want to send the message to? What will you send?
SEND#<user1>,<user2>#text.. --> For multiply users use "," to indicate multiple users
SEND#*#text.. --> Use "*" to send text to all users

CLOSE# --> discard all messages received. Server throws CLOSE#0 (for a normal close) and closes the clients connection.


Commands from Server to client(s):

Whenever a client connect/disconnect a list of all members online will be sent to
all clients currently online --> ONLINE#<User1>,<User2>,<User3>

Everytime a client sends a message the server adds MESSAGE --> MESSAGE#<User>#text...

0 for a normal close --> CLOSE#0
1 illegal input was received --> CLOSE#1
2 User not found --> CLOSE#2
When a close command is sent the server should close the connection and release all resources attached to that client.

This assignment was written by Mathias2860DK, TheAgns and MustafaTomak.
All the code is written together via Zoom. A few tasks were made individually. Check our kanban board and see who is assigned to the tasks for more information.

Response from group xxx : 

