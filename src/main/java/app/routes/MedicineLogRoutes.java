package app.routes;

import app.controllers.MedicineLogController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.delete;

public class MedicineLogRoutes {
    MedicineLogController medicineLogController = new MedicineLogController();

    public EndpointGroup getRoutes() {
        return () -> {
            get(medicineLogController::getAllLogs);
            post(medicineLogController::createLog);
            path("/{id}", () -> {
                get(medicineLogController::getLogsByMedicine);
                put(medicineLogController::updateLog);
                delete(medicineLogController::deleteLog);
            });
        };
    }
}
