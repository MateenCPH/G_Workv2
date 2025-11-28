package dat.controllers.impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.daos.impl.TicketDAO;
import dat.dtos.TicketDTO;
import dat.exceptions.ApiException;
import io.javalin.validation.ValidationException;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;

import java.time.DateTimeException;
import java.util.List;

public class TicketController implements IController<TicketDTO, Integer> {
    private final TicketDAO dao;

    public TicketController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = TicketDAO.getInstance(emf);
    }

    @Override
    public void readById(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));

            TicketDTO ticketDTO = dao.readById(id);
            ctx.res().setStatus(200);
            ctx.json(ticketDTO);
        } catch (IllegalArgumentException e) {
            throw new ApiException(400, "Invalid id: " + ctx.pathParam("id"));
        } catch (NoResultException | EntityNotFoundException e) {
            throw new ApiException(404, "Ticket with id " + ctx.pathParam("id") + " not found");
        }
    }

    @Override
    public void readAll(Context ctx) {
        try {
            List<TicketDTO> ticketDTOs = dao.readAll();
            ctx.res().setStatus(200);
            ctx.json(ticketDTOs);
        } catch (EntityNotFoundException e) {
            throw new ApiException(404, "Tickets not found");
        }
    }

    @Override
    public void create(Context ctx){
        try {
            TicketDTO jsonRequest = ctx.bodyAsClass(TicketDTO.class);
            TicketDTO createdTicket = dao.create(jsonRequest);
            ctx.res().setStatus(201);
            ctx.json(createdTicket);
        } catch (ApiException e) {
            throw new ApiException(e.getStatusCode(), e.getMessage());
        } catch (InvalidFormatException | JsonParseException | ValidationException | DateTimeException e) {
            throw new ApiException(400, e.getMessage());
        }
    }

    @Override
    public void update(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));

            TicketDTO jsonRequest = ctx.bodyAsClass(TicketDTO.class);
            TicketDTO updatedTicket = dao.update(id, jsonRequest);
            ctx.res().setStatus(201);
            ctx.json(updatedTicket);
        } catch (ApiException e) {
            throw new ApiException(e.getStatusCode(), e.getMessage());
        } catch (NumberFormatException e) {
            throw new ApiException(400, "Invalid id: " + ctx.pathParam("id"));
        } catch (EntityNotFoundException e) {
            throw new ApiException(404, "Ticket with id " + ctx.pathParam("id") + " not found");
        }
    }

    @Override
    public void delete(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            dao.delete(id);
            ctx.res().setStatus(204);
        } catch (IllegalArgumentException e) {
            throw new ApiException(400, "Invalid id: " + ctx.pathParam("id"));
        } catch (EntityNotFoundException e) {
            throw new ApiException(404, "Ticket with id " + ctx.pathParam("id") + " not found");
        }
    }
}
