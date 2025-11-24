package dat.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes {

    private final PlantRoute plantRoute = new PlantRoute();
    private final TicketRoute ticketRoute = new TicketRoute();

    public EndpointGroup getRoutes() {
        return () -> {
                path("/plants", plantRoute.getRoutes());
                path("/tickets", ticketRoute.getRoutes());
        };
    }
}
