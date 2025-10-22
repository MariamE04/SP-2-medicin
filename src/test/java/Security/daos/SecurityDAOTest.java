package Security.daos;

import Security.entities.User;
import app.config.HibernateConfig;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SecurityDAOTest {
        private static EntityManagerFactory emf;
        private static SecurityDAO securityDAO;

        @BeforeAll
        static void setUp() {
            emf = HibernateConfig.getEntityManagerFactoryForTest();
            securityDAO = new SecurityDAO(emf);
        }

        @Test
        void createUser_and_getUserByUsername() {
            User user = securityDAO.createUser("testuser", "testpass");
            assertNotNull(user);
            assertEquals("testuser", user.getUsername());

            User found = securityDAO.getUserByUsername("testuser");
            assertNotNull(found);
            assertEquals("testuser", found.getUsername());
        }

        @Test
        void createRole_and_addUserRole() throws Exception {
            securityDAO.createUser("roleUser", "1234");
            securityDAO.createRole("ADMIN");

            User updated = securityDAO.addUserRole("roleUser", "ADMIN");
            assertTrue(updated.getRoles().stream()
                    .anyMatch(r -> r.getRolename().equals("ADMIN")));
        }

        @Test
        void getVerifiedUser_returns_user_when_password_correct() throws Exception {
            securityDAO.createUser("verifyUser", "pass123");

            User verified = securityDAO.getVerifiedUser("verifyUser", "pass123");
            assertNotNull(verified);
            assertEquals("verifyUser", verified.getUsername());
        }

        @Test
        void getVerifiedUser_throws_when_password_incorrect() {
            securityDAO.createUser("wrongUser", "rightpass");

            assertThrows(Exception.class, () -> {
                securityDAO.getVerifiedUser("wrongUser", "wrongpass");
            });
        }
    }
