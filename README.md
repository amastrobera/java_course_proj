## Meals and Allergies

The aim of this excercise is to make a software to manage a univerity canteen. The user must be able to set a menu (first, second, dessert or fruit courses) and save it. He/she should be notified of the number of cateen users that are allergic to some of the ingredients of a course, and which course it they are allergic to (first, second or third). There are a client and a server, and a *rough* gui. 

#### IDE

The project was designed in NetBeans and run/debugged from there. The folder struture allows you to run the project in the same way if you use NetBeans.

#### Build the project

In Netbeans load and (clean) build these ones with `Shift+F11`, in order:

1. common ( library)
2. server (app, with library *common.jar* attached in build.xml)
3. client (app, as above)

#### Build the data

Flat files (newlines) are platform dependent. To avoid this problem data are automatically generated on your side with the  **common.io.DataGenerator** class.  In the main PI1 directory create a folder called **data**, and execute this

	mkdir data
	java -jar common/dist/common.jar data

#### Run the server

Arguments are *path/to/data* and *port-number*.

	java -jar server/dist/server.jar data 8080

#### Run the client

This **just a tests** to see whether the client can communicate with server properly. We use the gui to work. Client is eventually a library used by a gui or other program.

	java -jar client/dist/client.jar 


