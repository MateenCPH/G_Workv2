package dat.routes;

import dat.controllers.impl.UserController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class UserRoute {
    private final UserController controller = new UserController();

    protected EndpointGroup getRoutes() {

        return () -> {
            get("/", controller::readAll, Role.ANYONE);
            get("/{id}", controller::readById, Role.ANYONE);
            post("/", controller::create, Role.ANYONE);
            put("/{id}", controller::update, Role.ANYONE);
            delete("/{id}", controller::delete, Role.ANYONE);
        };
    }
}

