To run test suite, execute bash script "test".
To run a simple client with the server, execute the bash script "simpleClient", and open http://localhost:8080/ on a web browser.

My todo list is an ArrayList of Items. An Item is an object that encapsulates a Date, representing the time that the todo was added, and a String, representing the description of the todo.
Similarly, the history is also stored as an ArrayList of Items.
I used Jetty and Java's HttpServlet to handle requests. I run tests with JUnit.
I also created a simple client to test on, using jQuery. 
