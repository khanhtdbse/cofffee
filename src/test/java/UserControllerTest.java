import coffee.config.AppConfig;
import coffee.controller.AppController;
import coffee.model.User;
import coffee.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;



/**
 * Created by nguyen.van.tan on 6/13/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@WebAppConfiguration
public class UserControllerTest {

    private static final int UNKNOWN_ID = Integer.MAX_VALUE;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;


    private UserService userService;

    @InjectMocks
    private AppController appController;


    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .defaultRequest(get("/").with(user("david").roles("ADMIN")))
                .apply(springSecurity())
                .build();
    }


    @Test
    public void testGetSigin() throws Exception {

        userService =mock(UserService.class);
        when(userService.findBySSO(anyString())).thenAnswer(new Answer<User>(){
            @Override
            public User answer(InvocationOnMock invocationOnMock) throws Throwable {
                User user=new User();
                user.setSsoId("david");
                user.setPassword("tannv");

                return user;
            }
        });

        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("users",hasSize(2)));
    }





    @Test
    @WithMockUser
    public  void testCsrf() throws Exception
    {

        mockMvc.perform(post("/").with(csrf()));
    }


    public static RequestPostProcessor david() {
        return user("david").password("tannv").roles("ADMIN");
    }


//    @Test
//    public void testUserLogin() throws Exception {
//
//        mockMvc.perform(post("/login").param("ssoId","david").param("password","tannv")).andExpect(status().isOk()).andExpect(cookie().exists("JSESSIONID"));
//
//    }

}
