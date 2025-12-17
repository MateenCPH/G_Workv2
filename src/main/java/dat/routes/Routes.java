package dat.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes {

    private final PlantRoute plantRoute = new PlantRoute();
    private final TicketRoute ticketRoute = new TicketRoute();
    private final UserRoute userRoute = new UserRoute();
    private final GroupRoute groupRoute = new GroupRoute();
    private final MessageRoute messageRoute = new MessageRoute();
    private final TagRoute tagRoute = new TagRoute();

    public EndpointGroup getRoutes() {
        return () -> {
                path("/plants", plantRoute.getRoutes());
                path("/tickets", ticketRoute.getRoutes());
                path("/myusers", userRoute.getRoutes());
                path("/groups", groupRoute.getRoutes());
                path("/messages", messageRoute.getRoutes());
                path("/tags", tagRoute.getRoutes());
        };
    }
}
