package ua.nure.kn.morozova.usermanagement.db;

import static org.junit.Assert.*;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import org.dbunit.DatabaseTestCase;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import org.junit.Ignore;
import org.junit.Test;
import junit.framework.TestCase;
import ua.nure.kn.morozova.usermanagement.User;

public class HsqldbUserDaoTest extends DatabaseTestCase {

	private static final String FIRST_NAME_ETALON = "Dasha";
	private static final String LAST_NAME_ETALON = "Morozova";
	private UserDao dao;
	private ConnectionFactory connectionFactory;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		connectionFactory = new ConnectionFactoryImpl("sa", "", "jdbc:hsqldb:file:db/usermanagement",
				"org.hsqldb.jdbcDriver");
		dao = new HsqldbUserDao(connectionFactory);
	}

	@Test
	public void testCreate() {
		try {

			User user = new User();
			user.setFirstName(FIRST_NAME_ETALON);
			user.setLastName(LAST_NAME_ETALON);
			user.setDateOfBirth(new Date());

			assertNull(user.getId());
			user = dao.create(user);

			assertNotNull(user);
			assertNotNull(user.getId());

		} catch (DatabaseException e) {
			e.printStackTrace();
			fail(e.toString());
		}

	}

	@Test
	public void testUpdateAndFind() {
		try {

			User user = new User();
			user.setFirstName(FIRST_NAME_ETALON);
			user.setLastName(LAST_NAME_ETALON);
			user.setDateOfBirth(new Date());

			user = dao.create(user);
			User userFromDB = dao.find(user.getId());
			assertNotNull(userFromDB);
			assertEquals(FIRST_NAME_ETALON, userFromDB.getFirstName());

			User updatedUser = new User();
			updatedUser.setFirstName("updatedFirstName");
			updatedUser.setLastName("updatedLastName");
			updatedUser.setDateOfBirth(new Date());
			updatedUser.setId(user.getId());
			dao.update(updatedUser);
			User updatedUserFromDB = dao.find(user.getId());
			assertNotNull(updatedUserFromDB);
			assertEquals("updatedFirstName", updatedUserFromDB.getFirstName());

		} catch (DatabaseException e) {

			e.printStackTrace();
			fail(e.toString());
		}

	}

	@Test
	public void testDelete() {

		try {
			User user = new User();
			user.setFirstName("deletedFirstName");
			user.setLastName("deletedLastName");
			user.setDateOfBirth(new Date());
			user = dao.create(user);

			assertNotNull(user);

			User chekedUser = dao.find(user.getId());
			assertNotNull(chekedUser);
			dao.delete(user);
			chekedUser = dao.find(user.getId());

			assertNull(chekedUser.getId());

		} catch (DatabaseException e) {

			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void testFindAll() {
		try {
			Collection<?> collection = dao.findAll();
			assertNotNull("Collection is null", collection);
			assertEquals("Collection size:", 2, collection.size());

		} catch (DatabaseException e) {

			e.printStackTrace();
			fail(e.toString());
		}

	}

	@Override
	protected IDatabaseConnection getConnection() throws Exception {
		ConnectionFactory connectionFactory = new ConnectionFactoryImpl("sa", "", "jdbc:hsqldb:file:db/usermanagement",
				"org.hsqldb.jdbcDriver");
		return new DatabaseConnection(connectionFactory.createConnection());
	}

	@Override
	protected IDataSet getDataSet() throws Exception {
		IDataSet dataSet = new XmlDataSet(getClass().getClassLoader().getResourceAsStream("usersDataSet.xml"));
		return dataSet;
	}

}
