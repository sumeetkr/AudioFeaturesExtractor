package audioFeaturesExtractor.tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import audioFeaturesExtractor.Database.MySql;

public class MySqlTests {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMySqlConnection() {
		assertTrue(MySql.connectToDatabase());
	}

}
