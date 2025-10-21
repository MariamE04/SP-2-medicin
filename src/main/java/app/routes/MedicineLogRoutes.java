package app.routes;

import Security.enums.Role;
import app.controllers.MedicineLogController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.delete;

public class MedicineLogRoutes {
    MedicineLogController medicineLogController = new MedicineLogController();

    public EndpointGroup getRoutes() {
        return () -> {
            get(medicineLogController::getAllLogs, Role.USER, Role.ADMIN);
            post(medicineLogController::createLog, Role.USER);
            path("/{id}", () -> {
                get(medicineLogController::getLogsByMedicine, Role.USER, Role.ADMIN);
                put(medicineLogController::updateLog, Role.USER);
                delete(medicineLogController::deleteLog, Role.USER);
            });
        };
    }
}
