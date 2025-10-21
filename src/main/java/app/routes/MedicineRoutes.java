package app.routes;

import app.controllers.MedicineController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class MedicineRoutes {
    MedicineController medicineController = new MedicineController();

   public EndpointGroup getRoutes() {
        return () -> {
            get(medicineController::getAllMedicine);
            post(medicineController::createMedicine);
            path("/{id}", () -> {
                get(medicineController::getById);
                put(medicineController::updateMedicine);
                delete(medicineController::medicineToDelete);
        });
        };
    }
}