## Meals and Allergies

#### IDE

The project was designed in NetBeans and run/debugged from there. The folder struture allows you to run the project in the same way if you use NetBeans.

#### Build the project

In Netbeans load and (clean) build these ones with `Shift+F11`, in order:

1. common ( library)
2. server (app, with library *common.jar* attached in build.xml)
3. client (app, as above)
4. client_gui (javafx, needs *client.jar*)

#### Build the data

Flat files (newlines) are platform dependent. To avoid this problem data are automatically generated on your side with the  **common.io.DataGenerator** class.  In the main PI1 directory create a folder called **data**, and execute this

	mkdir data
	java -jar common/dist/common.jar data

#### Run the server

Arguments are *path/to/data* and *port-number*.

	java -jar server/dist/server.jar data 8080

#### Run the client

This just a test the client can communicate with server. We use the gui to work. Client is eventually a library used by the client gui below.

	java -jar client/dist/client.jar localhost 8080

#### Run the GUI 

	java -jar gui/dist/gui.jar


