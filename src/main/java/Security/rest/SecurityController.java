package Security.rest;

import Security.daos.ISecurityDAO;
import Security.daos.SecurityDAO;
import Security.entities.User;
import Security.exceptions.ValidationException;
import app.config.HibernateConfig;
import app.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dk.bugelhartmann.TokenSecurity;
import dk.bugelhartmann.UserDTO;
import io.javalin.http.*;

import java.util.Set;
import java.util.stream.Collectors;


public class SecurityController implements ISecurityController{
    ISecurityDAO securityDAO = new SecurityDAO(HibernateConfig.getEntityManagerFactory());
    ObjectMapper objectMapper = new Utils().getObjectMapper();
    TokenSecurity tokenSecurity = new TokenSecurity();

    /// TODO: implementer alle metoder rigtigt:

    @Override
    public Handler login() {
        return (Context ctx) -> {
            User user = ctx.bodyAsClass(User.class);
            try {
                User verifedUser = securityDAO.getVerifiedUser(user.getUsername(), user.getPassword());
                Set<String> stringRoles = verifedUser.getRoles()
                        .stream().map(role -> role.getRolename())
                        .collect(Collectors.toSet());
                UserDTO userDTO = new UserDTO(verifedUser.getUsername(), stringRoles);
                String token = createToken(userDTO);

                ObjectNode on = objectMapper
                        .createObjectNode()
                        .put("token", token)
                        .put("username", userDTO.getUsername());
                ctx.json(on).status(200);
            } catch (ValidationException ex) {
                ObjectNode on = objectMapper.createObjectNode().put("msg","login failed. Wrong username or password");
                ctx.json(on).status(401);
            }
        };
    }

    @Override
    public Handler register() {
        return null;
    }

    @Override
    public Handler authenticate() {
        return null;
    }

    @Override
    public Handler authorize() {
        return null;
    }

    @Override
    public String createToken(UserDTO user) throws Exception {
        return "";
    }
}