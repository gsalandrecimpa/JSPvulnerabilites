package junit.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import model.ModelVuln;
import model.User;

public class ModelVulnTest {
	private static String DEFAULT_PWD = "depart0";
	private ModelVuln model;

	@Before
	public void setUp() throws Exception {
		model = new ModelVuln();
	}

	@Test
	public void testUser() {
		boolean trouve;
		// pas d'exception si double delete
		model.deleteUser("test1");
		assertFalse(model.existsUser("test1"));
		model.deleteUser("test1");
		assertFalse(model.existsUser("test1"));
		assertTrue(model.existsUser("root"));
		
		model.loadUsers();
		assertNotNull(model.getUsers());
		trouve = false;
		for (User u:model.getUsers())
			if (u.getUsername().equals("test1")) fail();
		trouve = false;
		for (User u:model.getUsers()) 
			if (u.getUsername().equals("root")) {
				trouve = true;
				assertEquals("admin", u.getRole());
			}
		assertTrue(trouve);
		
		model.saveUser("test1", "pwd1", "user");
		assertTrue(model.existsUser("test1"));
		
		model.authenticateUser("test1", "pwd2");
		assertFalse(model.isConnected());
		assertFalse(model.isAdmin());
		assertEquals("test1", model.getUsername());
		
		model.authenticateUser("test1", "pwd1");
		assertEquals("test1", model.getUsername());
		assertTrue(model.isConnected());
		assertFalse(model.isAdmin());
		
		model.updatePassword("pwd2");
		model.authenticateUser("test1", "pwd1");
		assertFalse(model.isConnected());
		assertFalse(model.isAdmin());
		model.authenticateUser("test1", "pwd2");
		assertTrue(model.isConnected());
		assertFalse(model.isAdmin());
		model.authenticateUser("test1", "pwd1");
		assertFalse(model.isConnected());
		assertFalse(model.isAdmin());
		
		model.resetPassword("test1");
		model.authenticateUser("test1", "pwd2");
		assertFalse(model.isConnected());
		assertFalse(model.isAdmin());
		model.authenticateUser("test1", DEFAULT_PWD);
		assertTrue(model.isConnected());
		assertFalse(model.isAdmin());
		
		assertTrue(model.verifyPassword(DEFAULT_PWD));
		assertFalse(model.verifyPassword("pwd2"));
		
		model.loadUsers();
		trouve = false;
		for (User u:model.getUsers()) 
			if (u.getUsername().equals("test1")) {
				trouve = true;
				assertEquals("user", u.getRole());
			}
		assertTrue(trouve);
		
		model.changeRole("test1");
		
		model.loadUsers();
		trouve = false;
		for (User u:model.getUsers()) 
			if (u.getUsername().equals("test1")) {
				trouve = true;
				assertEquals("admin", u.getRole());
			}
		assertTrue(trouve);
		
		model.setUsername(null);
		model.setConnected(false);
		model.setAdmin(false);
		model.authenticateUser("test1", DEFAULT_PWD);
		assertEquals("test1", model.getUsername());
		assertTrue(model.isConnected());
		assertTrue(model.isAdmin());
		
		model.changeRole("test1");
		model.setUsername(null);
		model.setConnected(false);
		model.setAdmin(false);
		model.authenticateUser("test1", DEFAULT_PWD);
		assertEquals("test1", model.getUsername());
		assertTrue(model.isConnected());
		assertFalse(model.isAdmin());
		model.loadUsers();
		trouve = false;
		for (User u:model.getUsers()) 
			if (u.getUsername().equals("test1")) {
				trouve = true;
				assertEquals("user", u.getRole());
			}
		assertTrue(trouve);
	
		model.deleteUser("test1");
		assertFalse(model.existsUser("test1"));
		model.setUsername(null);
		model.setConnected(false);
		model.setAdmin(false);
		model.authenticateUser("test1", DEFAULT_PWD);
		assertFalse(model.isConnected());
		assertFalse(model.isAdmin());
		model.loadUsers();
		for (User u:model.getUsers()) 
			if (u.getUsername().equals("test1")) fail();
		
	}

}
