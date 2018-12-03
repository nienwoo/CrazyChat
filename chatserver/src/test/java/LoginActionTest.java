import com.crazymakercircle.chat.feignClient.UserAction;
import feign.Feign;
import feign.codec.StringDecoder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by 尼恩 at 疯狂创客圈
 */

//@ContextConfiguration(
//        locations = { "classpath:application.properties" })
//@RunWith(SpringJUnit4ClassRunner.class)
//@Configuration
//自动加载配置信息
//@EnableAutoConfiguration
public class LoginActionTest
{
/*    // 服务器ip地址
    @Value("${server.web.user.url}")
    private String userBase;*/

    @Test
    public void testLogin()
    {

        UserAction action = Feign.builder()
//                .decoder(new GsonDecoder())
                .decoder(new StringDecoder())
                .target(
                        UserAction.class,
//                        userBase
                        "http://localhost:8080/user"
                );

        String s = action.loginAction(
                "zhangsan",
                "zhangsan"
        );

        System.out.println("s = " + s);

    }


    @Test
    public void testGetById()
    {

        UserAction action = Feign.builder()
//                .decoder(new GsonDecoder())
                .decoder(new StringDecoder())
                .target(
                        UserAction.class,
                        "http://localhost:8080/user"
                );

        String s = action.getById(2);

        System.out.println("s = " + s);

    }
}
