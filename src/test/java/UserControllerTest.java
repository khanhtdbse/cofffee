import coffee.controller.AppController;
import coffee.model.User;
import coffee.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 * Created by nguyen.van.tan on 6/13/17.
 */
public class UserControllerTest {

    private static final int UNKNOWN_ID = Integer.MAX_VALUE;

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private AppController appController;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(appController)
                .build();
    }

    // =========================================== Get All Users ==========================================

    @Test
    public void test_get_all_success() throws Exception {
        User  user1=new User();
        user1.setId(1);
        user1.setSsoId("tannv1");
        user1.setEmail("tannv1@framgia.com");
        user1.setFirstName("Nguyen Van Tan1");

        User  user2=new User();
        user2.setId(2);
        user1.setSsoId("tannv2");
        user1.setEmail("tannv2@framgia.com");
        user1.setFirstName("Nguyen Van Tan2");

        User  user3=new User();
        user3.setId(3);
        user1.setSsoId("tannv3");
        user1.setEmail("tannv3@framgia.com");
        user1.setFirstName("Nguyen Van Tan3");

        List<User> users = Arrays.asList(
                user1,user2,user3);

        when(userService.findAllUsers()).thenReturn(users);

        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].ssoId", is("tannv1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].firstName", is("Nguyen Van Tan2")));

        verify(userService, times(1)).findAllUsers();
        verifyNoMoreInteractions(userService);
    }

    // =========================================== Get User By ID =========================================

    @Test
    public void test_get_by_id_success() throws Exception {

        User  user1=new User();
        user1.setSsoId("tannv1");
        user1.setEmail("tannv1@framgia.com");
        user1.setFirstName("Nguyen Van Tan1");

        when(userService.findById(1)).thenReturn(user1);

        mockMvc.perform(get("/users/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Nguyen Van Tan1")));

        verify(userService, times(1)).findById(1);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void test_get_by_id_fail_404_not_found() throws Exception {

        when(userService.findById(1)).thenReturn(null);

        mockMvc.perform(get("/users/{id}", 1))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).findById(1);
        verifyNoMoreInteractions(userService);
    }

}
