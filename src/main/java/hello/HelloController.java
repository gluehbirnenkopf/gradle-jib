package hello;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Value;

@RestController
public class HelloController {

    @Value("${message.text}")
    private String someOutput;

    @RequestMapping("/")
    public String index() {
        return someOutput;
    }

}
