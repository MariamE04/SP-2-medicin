package app.routes;

import Security.rest.SecurtiyRoutes;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes {

    private MedicineRoutes medicineRoutes = new MedicineRoutes();
    private MedicineLogRoutes medicineLogRoutes = new MedicineLogRoutes();
    private SecurtiyRoutes securtiyRoutes = new SecurtiyRoutes();

    public EndpointGroup getRoutes() {
        return () -> {
            // root endpoint
            get("/", ctx -> ctx.result("Welcome to Medicine API!"));

            // endpoints
            path("/medicines", medicineRoutes.getRoutes());
            path("/medicineLog", medicineLogRoutes.getRoutes());
            path("/auth", securtiyRoutes.getOpenRoutes());
            path("/protected", securtiyRoutes.getSecuredRoutes());
        };
    }
}
