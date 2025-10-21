package Security.rest;

import Security.enums.Role;
import app.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class SecurtiyRoutes {
    ISecurityController securityController = new SecurityController();
    private static ObjectMapper jsonMapper = new Utils().getObjectMapper();

    public EndpointGroup getSecurityRoute = () -> {
        path("/auth", ()->
                post("/login", securityController.login()));
    };

    public static EndpointGroup getSecuredRoutes(){
        return ()->{
            path("/protected", ()->{
                get("/user_demo",(ctx)->ctx.json(jsonMapper.createObjectNode().put("msg",  "Hello from USER Protected")),Role.USER);
                get("/admin_demo",(ctx)->ctx.json(jsonMapper.createObjectNode().put("msg",  "Hello from ADMIN Protected")),Role.ADMIN);
            });
        };
    }
}
