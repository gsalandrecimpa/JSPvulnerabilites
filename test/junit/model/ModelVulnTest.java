package junit.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import model.Logiciel;
import model.ModelVuln;
import model.Solution;
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

	@Test
	public void testLogiciel() {
		boolean trouve;

		model.deleteLogiciel("test1");
		model.deleteLogiciel("test2");
		assertFalse(model.existsLogiciel("test1"));
		assertFalse(model.existsLogiciel("test2"));

		model.loadLogiciels();
		assertNotNull(model.getLogiciels());
		for (Logiciel log:model.getLogiciels()) {
			if (log.getNom().equals("test1")) fail();
			if (log.getNom().equals("test2")) fail();
		}

		model.saveLogiciel("test1");
		assertTrue(model.existsLogiciel("test1"));
		assertFalse(model.existsLogiciel("test2"));

		model.loadLogiciels();
		assertNotNull(model.getLogiciels());
		trouve=false;
		for (Logiciel log:model.getLogiciels()) {
			if (log.getNom().equals("test1")) trouve=true;
			if (log.getNom().equals("test2")) fail();
		}
		assertTrue(trouve);


		model.saveLogiciel("test2");
		assertTrue(model.existsLogiciel("test1"));
		assertTrue(model.existsLogiciel("test2"));

		model.loadLogiciels();
		assertNotNull(model.getLogiciels());
		trouve=false;
		for (Logiciel log:model.getLogiciels()) 
			if (log.getNom().equals("test1")) trouve=true;
		assertTrue(trouve);
		trouve=false;
		for (Logiciel log:model.getLogiciels()) 
			if (log.getNom().equals("test2")) trouve=true;
		assertTrue(trouve);

		model.deleteLogiciel("test2");
		assertTrue(model.existsLogiciel("test1"));
		assertFalse(model.existsLogiciel("test2"));

		model.loadLogiciels();
		assertNotNull(model.getLogiciels());
		trouve=false;
		for (Logiciel log:model.getLogiciels()) {
			if (log.getNom().equals("test1")) trouve=true;
			if (log.getNom().equals("test2")) fail();
		}
		assertTrue(trouve);

		model.deleteLogiciel("test1");
		assertFalse(model.existsLogiciel("test1"));
		assertFalse(model.existsLogiciel("test2"));

		model.loadLogiciels();
		assertNotNull(model.getLogiciels());
		for (Logiciel log:model.getLogiciels()) {
			if (log.getNom().equals("test1")) fail();
			if (log.getNom().equals("test2")) fail();
		}
	}

	
	@Test
	public void testSol() {
		Solution sol1 = null, sol2=null, sol3=null;
		boolean trouve1, trouve2;
		
		
		sol1 = model.returnSolFromRef("reftest1");
		if (sol1 != null) model.deleteSol(sol1.getId());
		sol2 = model.returnSolFromRef("reftest2");
		if (sol2 != null) model.deleteSol(sol2.getId());
		
		model.saveLogiciel("logtest1");
		
		model.setSol(new Solution(0, "reftest1", "titre1", "description1\r\nsuite.", false));
		model.getSol().addLogiciel("logtest1");
		model.saveSol();
		
		sol1 = null;
		sol1 = model.returnSolFromRef("reftest1");
		assertTrue(model.existsSolution(sol1.getId()));
		assertFalse(model.existsSolution(Integer.MIN_VALUE));
		
		model.setSol(new Solution(0, "reftest2", "titre2", "description2", false));
		model.saveSol();
		assertTrue(model.existsSolution(sol1.getId()));
		sol2 = null;
		sol2 = model.returnSolFromRef("reftest2");
		assertTrue(model.existsSolution(sol1.getId()));
		assertTrue(model.existsSolution(sol2.getId()));
		
		model.loadAllSolutions();
		assertNotNull(model.getSolutions());
		trouve1 = trouve2 = false;
		for(Solution sol:model.getSolutions()) {
			if (sol.getReference().equals("reftest1")) {
				trouve1 = true;
				assertEquals(sol1.getId(), sol.getId());
				assertEquals("titre1", sol1.getTitre());
				assertEquals("titre1", sol.getTitre());
				assertEquals("description1\r\nsuite.", sol1.getDescription());
				assertEquals("description1\r\nsuite.", sol.getDescription());
				assertEquals("logtest1", sol1.getLogiciels().get(0));
				assertEquals("logtest1", sol.getLogiciels().get(0));
			}
			if (sol.getReference().equals("reftest2")) {
				trouve2 = true;
				assertEquals(sol2.getId(), sol.getId());
				assertEquals("titre2", sol2.getTitre());
				assertEquals("titre2", sol.getTitre());
				assertEquals("description2", sol2.getDescription());
				assertEquals("description2", sol.getDescription());
				if (sol2.getLogiciels() != null) assertEquals(0, sol2.getLogiciels().size());
				if (sol.getLogiciels() != null) assertEquals(0, sol.getLogiciels().size());
			}
		}
		assertTrue(trouve1);
		assertTrue(trouve2);
		
		model.deleteSol(sol2.getId());
		assertTrue(model.existsSolution(sol1.getId()));
		assertFalse(model.existsSolution(sol2.getId()));
		assertNull(model.returnSolFromRef("reftest2"));
		assertNotNull(sol3 = model.returnSolFromRef("reftest1"));
		assertEquals(sol1,sol3);
		assertEquals(sol1.getDescription(),sol3.getDescription());
		assertEquals(sol1.getTitre(), sol3.getTitre());
		assertEquals(sol1.getReference(), sol3.getReference());
		assertEquals(sol1.getLogiciels().get(0), sol3.getLogiciels().get(0));
				
		model.deleteSol(sol1.getId());
		assertFalse(model.existsSolution(sol1.getId()));
		assertFalse(model.existsSolution(sol2.getId()));
		model.loadAllSolutions();
		if (model.getSolutions() != null)
			for(Solution sol:model.getSolutions()) 
				if (sol.getReference().equals("restest1") || sol.getReference().equals("restest2")) fail(); 
		
	}
	
	
	
} //class
