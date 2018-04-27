## Meals and Allergies

#### IDE

The project was designed in NetBeans and run/debugged from there. The folder struture allows you to run the project in the same way if you use NetBeans.

#### Build the project

In Netbeans load and build these ones, in order:

1. common ( library)
2. server (app, with library *jar-file* attached in build.xml)
3. client (app, as above)

#### Build the data ###

Flat files (newlines) are platform dependent. To avoid this problem data are automatically generated on your side with the  **common.io.DataGenerator** class.  In the main PI1 directory create a folder called **data**, and execute this

	java -jar common/dist/common.jar data

#### Run the server

	java -jar server/dist/server.jar 8080

#### Run the client

	java -jar client/dist/client.jar "localhost" 8080

