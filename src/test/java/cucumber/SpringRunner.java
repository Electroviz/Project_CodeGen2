package cucumber;

import org.junit.internal.Classes;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(org.springframework.test.context.junit4.SpringRunner.class)
@SpringBootTest(Classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringRunner {
}
