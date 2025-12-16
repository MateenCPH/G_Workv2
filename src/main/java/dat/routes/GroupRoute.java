package dat.routes;

import dat.controllers.impl.GroupController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class GroupRoute {
    private final GroupController controller = new GroupController();

    protected EndpointGroup getRoutes() {

        return () -> {
            get("/", controller::readAll, Role.ANYONE);
            get("/{id}", controller::readById, Role.ANYONE);
        };
    }
}

