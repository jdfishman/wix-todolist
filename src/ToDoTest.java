import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

public class ToDoTest
{
	@Test
    public void addItem() throws UnsupportedEncodingException {
		ToDoServlet servlet = new ToDoServlet();
		
    	MockHttpServletRequest request = new MockHttpServletRequest();
    	request.setRequestURI("/addItem");
    	request.setMethod("POST");
    	request.setParameter("description", "Wash Car");
    	
    	MockHttpServletResponse response = new MockHttpServletResponse();
    	
    	servlet.doPost(request, response);
    	
    	assertEquals(servlet.todoList.size(), 1);
    	assertEquals(servlet.todoList.get(0).description, "Wash Car");
    	assertEquals(servlet.todoList.get(0).date.getTime(), 
    			Long.parseLong(response.getContentAsString()));
    	assertEquals(servlet.history.size(), 1);
    	assertEquals(servlet.history.get(0).description, "Added Wash Car");
    	assertEquals(servlet.history.get(0).date.getTime(), 
    			Long.parseLong(response.getContentAsString()));
    }
	
	@Test
	public void getAllItems() throws UnsupportedEncodingException {
		ToDoServlet servlet = new ToDoServlet();
		
    	MockHttpServletRequest request1 = new MockHttpServletRequest();
    	request1.setRequestURI("/addItem");
    	request1.setMethod("POST");
    	request1.setParameter("description", "Wash Car");
    	
    	MockHttpServletRequest request2 = new MockHttpServletRequest();
    	request2.setRequestURI("/addItem");
    	request2.setMethod("POST");
    	request2.setParameter("description", "Feed Dog");
    	
    	MockHttpServletRequest request3 = new MockHttpServletRequest();
    	request3.setRequestURI("/getAllItems");
    	request3.setMethod("GET");
    	
    	MockHttpServletResponse response1 = new MockHttpServletResponse();
    	MockHttpServletResponse response2 = new MockHttpServletResponse();
    	MockHttpServletResponse response3 = new MockHttpServletResponse();
    	
    	servlet.doPost(request1, response1);
    	servlet.doPost(request2, response2);
    	servlet.doGet(request3, response3);
    	
    	assertEquals(servlet.todoList.size(), 2);
    	assertEquals(servlet.todoList.get(0).description, "Wash Car");
    	assertEquals(servlet.todoList.get(1).description, "Feed Dog");
    	assertEquals(servlet.todoList.get(0).date.getTime(), 
    			Long.parseLong(response1.getContentAsString()));
    	assertEquals(servlet.todoList.get(1).date.getTime(), 
    			Long.parseLong(response2.getContentAsString()));
    	
		String expectedResponse3 = "{\"0\":\"" + 
			new Date(Long.parseLong(response1.getContentAsString())) +
			": Wash Car\",\"1\":\"" + 
			new Date(Long.parseLong(response2.getContentAsString())) +
			": Feed Dog\"}";
		assertEquals(response3.getContentAsString(), expectedResponse3);
	}
	
	@Test
	public void deleteItem() throws UnsupportedEncodingException {
		ToDoServlet servlet = new ToDoServlet();
		
    	MockHttpServletRequest request1 = new MockHttpServletRequest();
    	request1.setRequestURI("/addItem");
    	request1.setMethod("POST");
    	request1.setParameter("description", "Wash Car");
    	
    	MockHttpServletRequest request2 = new MockHttpServletRequest();
    	request2.setRequestURI("/addItem");
    	request2.setMethod("POST");
    	request2.setParameter("description", "Feed Dog");
    	
    	MockHttpServletResponse response1 = new MockHttpServletResponse();
    	MockHttpServletResponse response2 = new MockHttpServletResponse();
    	
    	servlet.doPost(request1, response1);
    	servlet.doPost(request2, response2);
    	
    	MockHttpServletRequest request3 = new MockHttpServletRequest();
    	request3.setRequestURI("/deleteItem");
    	request3.setMethod("POST");
    	request3.setParameter("id", response1.getContentAsString());
    	
    	MockHttpServletResponse response3 = new MockHttpServletResponse();
    	
    	servlet.doPost(request3, response3);
    	
    	assertEquals(response3.getContentAsString(), "Wash Car");
    	assertEquals(servlet.todoList.size(), 1);
    	assertEquals(servlet.todoList.get(0).description, "Feed Dog");
	}
	
	@Test
	public void showHistory() throws UnsupportedEncodingException {
		ToDoServlet servlet = new ToDoServlet();
		
    	MockHttpServletRequest request1 = new MockHttpServletRequest();
    	request1.setRequestURI("/addItem");
    	request1.setMethod("POST");
    	request1.setParameter("description", "Wash Car");
    	
    	MockHttpServletRequest request2 = new MockHttpServletRequest();
    	request2.setRequestURI("/addItem");
    	request2.setMethod("POST");
    	request2.setParameter("description", "Feed Dog");
    	
    	MockHttpServletResponse response1 = new MockHttpServletResponse();
    	MockHttpServletResponse response2 = new MockHttpServletResponse();
    	
    	servlet.doPost(request1, response1);
    	servlet.doPost(request2, response2);
    	
    	MockHttpServletRequest request3 = new MockHttpServletRequest();
    	request3.setRequestURI("/deleteItem");
    	request3.setMethod("POST");
    	request3.setParameter("id", response1.getContentAsString());
    	
    	MockHttpServletResponse response3 = new MockHttpServletResponse();
    	
    	servlet.doPost(request3, response3);
    	
    	assertEquals(servlet.history.size(), 3);
    	assertEquals(servlet.history.get(0).description, "Added Wash Car");
    	assertEquals(servlet.history.get(1).description, "Added Feed Dog");
    	assertEquals(servlet.history.get(2).description, "Deleted Wash Car");
    	assertEquals(servlet.history.get(0).date.getTime(), 
    			Long.parseLong(response1.getContentAsString()));
    	assertEquals(servlet.history.get(1).date.getTime(), 
    			Long.parseLong(response2.getContentAsString()));
    	
    	assertNotSame(servlet.history.get(2).date.getTime(), 
    			Long.parseLong(response1.getContentAsString()));
    	
    	MockHttpServletRequest request4 = new MockHttpServletRequest();
    	request4.setRequestURI("/showHistory");
    	request4.setMethod("GET");
    	
    	MockHttpServletResponse response4 = new MockHttpServletResponse();
    	servlet.doGet(request4, response4);
    	String response_string = response4.getContentAsString();
    	
    	assertThat(response_string, containsString(servlet.history.get(0).description));
    	assertThat(response_string, containsString(servlet.history.get(0).date.toString()));
    	assertThat(response_string, containsString(servlet.history.get(1).description));
    	assertThat(response_string, containsString(servlet.history.get(1).date.toString()));
    	assertThat(response_string, containsString(servlet.history.get(2).description));
    	assertThat(response_string, containsString(servlet.history.get(2).date.toString()));
	}
}