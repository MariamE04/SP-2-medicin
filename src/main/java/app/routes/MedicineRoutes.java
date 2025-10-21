package app.routes;

import Security.enums.Role;
import app.controllers.MedicineController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class MedicineRoutes {
    MedicineController medicineController = new MedicineController();

   public EndpointGroup getRoutes() {
        return () -> {
            get(medicineController::getAllMedicine, Role.USER, Role.ADMIN);
            post(medicineController::createMedicine, Role.USER);
            path("/{id}", () -> {
                get(medicineController::getById, Role.USER, Role.ADMIN);
                put(medicineController::updateMedicine, Role.USER);
                delete(medicineController::medicineToDelete, Role.USER);
        });
        };
    }
}