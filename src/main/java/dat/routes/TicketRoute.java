package dat.routes;

import dat.controllers.impl.PlantController;
import dat.controllers.impl.TicketController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.delete;

public class TicketRoute {
    private final TicketController controller = new TicketController();

    protected EndpointGroup getRoutes() {

        return () -> {
            get("/", controller::readAll, Role.ANYONE);
            get("/{id}", controller::readById, Role.ANYONE);
            post("/", controller::create, Role.ANYONE);
            put("/{id}", controller::update, Role.ANYONE);
            delete(controller::delete, Role.ANYONE);
        };
    }
}
