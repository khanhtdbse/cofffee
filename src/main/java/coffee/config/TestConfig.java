package coffee.config;

import coffee.service.UserService;
import coffee.service.UserServiceImpl;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import static org.mockito.Mockito.*;

/**
 * Created by nguyen.van.tan on 6/21/17.
 */
@Configuration
public class TestConfig {

    @Bean
    public MessageSource messageSource()
    {
        ResourceBundleMessageSource resourceBundleMessageSource =new ResourceBundleMessageSource();
        resourceBundleMessageSource.setBasename("i18n/messages");
        resourceBundleMessageSource.setUseCodeAsDefaultMessage(true);
    return resourceBundleMessageSource;

    }


    @Bean
    public UserService userService()
    {
        return mock(UserServiceImpl.class);
    }
}
