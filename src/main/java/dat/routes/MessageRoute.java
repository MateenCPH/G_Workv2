package dat.routes;

import dat.controllers.impl.MessageController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class MessageRoute {
    private final MessageController controller = new MessageController();

    protected EndpointGroup getRoutes() {

        return () -> {
            get("/", controller::readAll, Role.ANYONE);
            post("/", controller::create, Role.ANYONE);
        };
    }
}

