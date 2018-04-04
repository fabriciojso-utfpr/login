package PM.login.PM;

import PM.login.model.UserType;
import PM.login.model.User;
import PM.login.DAO.UserDAO;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;

/**
 *
 * @author andreendo
 */
public class AddUserPMTest {
    
    public AddUserPMTest() {
    }

    @Test
    public void testDifferentPasswords() {
        AddUserPM addUserPM = new AddUserPM();
        addUserPM.setUsername("andre");
        addUserPM.setPassword("123");
        addUserPM.setConfirmPassword("1234");
        
        try {
            addUserPM.save();       
            fail();
        } catch(Exception e) {
            assertEquals("Passwords are different", e.getMessage());
        }
    }
    
    @Test
    public void testUsernameAlreadyExists() {
        UserDAO userDAOMock = mock(UserDAO.class);
        when(userDAOMock.exist("andre"))
                .thenReturn(true);
        
        AddUserPM addUserPM = new AddUserPM();
        addUserPM.setUsername("andre");
        addUserPM.setPassword("123");
        addUserPM.setConfirmPassword("123");
        
        addUserPM.setUserDao(userDAOMock);
        
        try {
            addUserPM.save();       
            fail();
        } catch(Exception e) {
            assertEquals("Username already exists", e.getMessage());
        }
    }

    @Test
    public void testNewUserSuccess() throws Exception {
        UserDAO userDAOMock = mock(UserDAO.class);
        when(userDAOMock.exist("joao"))
                .thenReturn(false);
        
        AddUserPM addUserPM = new AddUserPM();
        addUserPM.setUsername("joao");
        addUserPM.setPassword("123");
        addUserPM.setConfirmPassword("123");
        addUserPM.setSelectedType("admin");
        
        addUserPM.setUserDao(userDAOMock);
        
        assertEquals("New user added", addUserPM.save());
        assertEquals("", addUserPM.getUsername());
        assertEquals("", addUserPM.getPassword());
        assertEquals("", addUserPM.getConfirmPassword());
        assertEquals("normal", addUserPM.getSelectedType());
        
        ArgumentCaptor<User> userArg = ArgumentCaptor.forClass(User.class);
        verify(userDAOMock).save( userArg.capture() );
        assertEquals("joao", userArg.getValue().getUsername());
        assertEquals("123", userArg.getValue().getPassword());
        assertEquals(UserType.ADMIN, userArg.getValue().getType());
    }    
}
