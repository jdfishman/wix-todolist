import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.json.JSONObject;

public class ToDoServlet extends HttpServlet
{
	ArrayList<Item> todoList;
	ArrayList<Item> history;
	
	// an Item encapsulates a date/time and
	// a description (for todoList, an todo item, for history, an action)
	public class Item {
		Date date;
		String description;
		
		public Item(Date date, String description) {
			this.date = date;
			this.description = description;
		}
		
		public String toString() {
			return date + ": " + description;
		}
	}
	
	public ToDoServlet() {
		super();
		todoList = new ArrayList<Item>();
		history = new ArrayList<Item>();
	}
	
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
    {
    	try {
        	if (request.getRequestURI().equals("/getAllItems")) {
        		//create a JSON with a list taken from todoList
        		response.setContentType("application/json");
        		JSONObject obj = new JSONObject();
        		for (int i=0; i < todoList.size(); i++) {
        			obj.put(Integer.toString(i), todoList.get(i));
        		}
        		response.getWriter().print(obj);
        	} 
        	else if (request.getRequestURI().equals("/showHistory")) {
        		//create a JSON with a list taken from history
        		response.setContentType("application/json");
        		JSONObject obj = new JSONObject();
        		for (int i=0; i < history.size(); i++) {
        			obj.put(Integer.toString(i), history.get(i));
        		}
        		response.getWriter().print(obj);
        	} 
        	else if (request.getRequestURI().equals("/")) {
        		//serve simple test HTML file
	            response.setContentType("text/html");
	            OutputStream outStream = response.getOutputStream();
	            Path path = new File("todo.html").toPath();
	            Files.copy(path, outStream);
	            outStream.flush();
        	} 
        	else if (request.getRequestURI().equals("/todo.js")) {
        		//serve simple test js file
	            response.setContentType("text/javascript");
	            OutputStream outStream = response.getOutputStream();
	            Path path = new File("todo.js").toPath();
	            Files.copy(path, outStream);
	            outStream.flush();
        	}
    	} catch (Exception e) {
    		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    	}
    }
    
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
    {
    	try {
			if (request.getRequestURI().equals("/addItem")) {
				//add Item to list if it's nonempty
				//return an ID corresponding to unique time stamp
				String description = request.getParameter("description");
				if (description != null) {
					Date date = new Date();
					todoList.add(new Item(date, description));
					history.add(new Item(date, "Added " + description));
					long id = date.getTime();
			        response.getWriter().print(Long.toString(id));
				}
			} 
			else if (request.getRequestURI().equals("/deleteItem")) {
				//delete Item from list if the given ID corresponds to a date in an Item
				//return an ID corresponding to unique time stamp
				String id_string = request.getParameter("id");
				if (id_string != null) {
					long id = Long.parseLong(id_string);
					for (int i=0; i < todoList.size(); i++) {
						Item item = todoList.get(i);
						if (item.date.getTime() == id) {
							history.add(new Item(new Date(), "Deleted " + item.description));
							response.getWriter().print(item.description);
							todoList.remove(i);
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
    }
    
    // Test Client - at http://localhost:8080/
    public static void main(String[] args) throws Exception
    {
        Server server = new Server(8080);
        ServletHandler handler = new ServletHandler();
        handler.addServletWithMapping(ToDoServlet.class, "/*");
        server.setHandler(handler);

        server.start();
        server.join();
    }
}