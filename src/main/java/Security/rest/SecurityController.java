package Security.rest;

import Security.daos.ISecurityDAO;
import Security.daos.SecurityDAO;
import app.config.HibernateConfig;
import app.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dk.bugelhartmann.TokenSecurity;
import dk.bugelhartmann.UserDTO;
import io.javalin.http.*;

import java.text.ParseException;
import java.util.Set;
import java.util.stream.Collectors;


public class SecurityController implements ISecurityController{
    ISecurityDAO securityDAO = new SecurityDAO(HibernateConfig.getEntityManagerFactory());
    //ObjectMapper objectMapper = new Utils().getObjectMapper();
    TokenSecurity tokenSecurity = new TokenSecurity();

    /// TODO: implementer alle metoder rigtigt:

    @Override
    public Handler login() {
        return null;
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