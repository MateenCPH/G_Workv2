package dat.routes;

import dat.controllers.impl.TagController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class TagRoute {
    private final TagController controller = new TagController();

    protected EndpointGroup getRoutes() {

        return () -> {
            get("/", controller::readAll, Role.ANYONE);
            get("/{id}", controller::readById, Role.ANYONE);
        };
    }
}
