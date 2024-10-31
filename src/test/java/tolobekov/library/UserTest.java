package tolobekov.library;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import tolobekov.library.model.User;

public class UserTest {

    private User user;

    @Before
    public void setUp() {
        user = new User("johndoe", "password123", "member");
    }

    @Test
    public void testUserConstructorAndGetters() {
        assertEquals("johndoe", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals("member", user.getUserType());
    }

    @Test
    public void testSetUsername() {
        user.setUsername("janedoe");
        assertEquals("janedoe", user.getUsername());
    }

    @Test
    public void testSetPassword() {
        user.setPassword("newpassword");
        assertEquals("newpassword", user.getPassword());
    }

    @Test
    public void testSetUserType() {
        user.setUserType("librarian");
        assertEquals("librarian", user.getUserType());
    }

    @Test
    public void testEquals() {
        User sameUser = new User("johndoe", "password123", "member");
        User differentUser = new User("janedoe", "password123", "member");

        assertTrue(user.equals(sameUser));
        assertFalse(user.equals(differentUser));
    }

    @Test
    public void testHashCode() {
        User sameUser = new User("johndoe", "password123", "member");
        assertEquals(user.hashCode(), sameUser.hashCode());
    }

    @Test
    public void testToString() {
        String expected = "User{username='johndoe', userType='member'}";
        assertEquals(expected, user.toString());
    }
}
