## Meals and Allergies

The aim of this excercise is to make a software to manage a univerity canteen. The user must be able to set a menu (first, second, dessert or fruit courses) and save it. He/she should be notified of the number of cateen users that are allergic to some of the ingredients of a course, and which course it they are allergic to (first, second or third). There are a client and a server, and a *rough* gui. 

#### IDE

The project was designed in NetBeans and run/debugged from there. The folder struture allows you to run the project in the same way if you use NetBeans.

#### Build the project

In Netbeans load and build these ones with `F11`, in this order:

1. common (library)
2. server (app, with library *common.jar* attached in build.xml)
3. client (lib + test-app, as above)
4. gui (javafx, needs *client.jar*)

#### Build the data

Flat files (newlines) are platform dependent. To avoid this problem data are automatically generated on your side with the  **common.io.DataGenerator** class. If there isn't already one in the main directory, create a folder called **data**, and execute this. 

	java -jar common/dist/common.jar /path/to/data

#### Run the server

The server needs to know where the data is stored and what port of the (localhost) to server to, say 8080. 

	java -jar server/dist/server.jar /path/to/data 8080

#### Run the client

This **just a tests** to see whether the client can communicate with server properly. We use the gui to work. Client is eventually a library used by a gui or other program.

	java -jar client/dist/client.jar 

#### Run the GUI 

The GUI has three tabs. A Menu tab, to select four courses and save it. A Meal tab, to view the specifics of each course. A Users tab, to view all users, or allergic users to the selected menu.

	java -jar gui/dist/gui.jar


