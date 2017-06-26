import coffee.config.AppConfig;
import coffee.config.TestConfig;
import coffee.model.User;
import coffee.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



/**
 * Created by nguyen.van.tan on 6/13/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class,AppConfig.class})
@WebAppConfiguration
public class UserControllerTest {

    private static final int UNKNOWN_ID = Integer.MAX_VALUE;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserService userService;


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

        when(userService.findBySSO(anyString())).thenAnswer(new Answer<User>(){
            @Override
            public User answer(InvocationOnMock invocationOnMock) throws Throwable {
                User user=new User();
                user.setSsoId("david");
                user.setPassword("tannv");

                return user;
            }
        });

        when(userService.findAllUsers()).thenAnswer(new Answer<List<User>>() {

            @Override
            public List<User> answer(InvocationOnMock invocationOnMock) throws Throwable {
                List<User> users =new ArrayList<>();
                User user=new User();
                user.setSsoId("david");
                user.setPassword("tannv");
                users.add(user);

                User user1=new User();
                user.setSsoId("david1");
                user.setPassword("tannv1");
                users.add(user1);
                return users;
            }
        });

        mockMvc.perform(get("/"))
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
