package facades;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.UserDTO;
import entities.Role;
import entities.User;
import errorhandling.MissingInput;
import errorhandling.NotFoundException;
import security.errorhandling.AuthenticationException;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class UserFacadeTest {

    private static EntityManagerFactory emf;
    private static UserFacade facade;
    private static User user, admin, both;
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public UserFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = UserFacade.getUserFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        user = new User("user", "test123");
        admin = new User("admin", "test123");
        both = new User("user_admin", "test123");
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Roles.deleteAllRows").executeUpdate();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            Role userRole = new Role("user");
            Role adminRole = new Role("admin");
            user.addRole(userRole);
            admin.addRole(adminRole);
            both.addRole(userRole);
            both.addRole(adminRole);
            em.persist(userRole);
            em.persist(adminRole);
            em.persist(user);
            em.persist(admin);
            em.persist(both);
            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }


    @Test
    public void getAllUsersTest() {
        List<UserDTO> userDTOList = facade.getAllUsers();

        assertTrue(userDTOList.size() == 3);
    }

    @Test
    public void testDeleteUser() {
        String userName = user.getUsername();

        UserDTO userDTO = facade.deleteUser(userName);

        assertTrue(userDTO.getUsername().equals(userName));
    }

    @Test
    public void testAddUser() throws AuthenticationException, MissingInput {

        String user = "{\"username\":\"Test\",\"password\":\"TestTest\"}";
        UserDTO userDTO = gson.fromJson(user, UserDTO.class);
        UserDTO newUser = facade.addUser(userDTO);

        List<UserDTO> userDTOList = facade.getAllUsers();

        assertTrue(userDTOList.size() == 4);

        assertTrue(newUser.getUsername().equalsIgnoreCase(userDTO.getUsername()));
    }

}

