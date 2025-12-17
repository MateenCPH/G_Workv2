package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.daos.impl.MessageDAO;
import dat.dtos.MessageDTO;
import dat.exceptions.ApiException;
import io.javalin.http.Context;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;

import java.util.List;

public class MessageController implements IController<MessageDTO, Integer> {
    private final MessageDAO dao;

    public MessageController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = MessageDAO.getInstance(emf);
    }

    @Override
    public void readById(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));

            MessageDTO messageDTO = dao.readById(id);
            ctx.res().setStatus(200);
            ctx.json(messageDTO);
        } catch (IllegalArgumentException e) {
            throw new ApiException(400, "Invalid id: " + ctx.pathParam("id"));
        } catch (NoResultException | EntityNotFoundException e) {
            throw new ApiException(404, "Message with id " + ctx.pathParam("id") + " not found");
        }
    }

    @Override
    public void readAll(Context ctx) {
        try {
            List<MessageDTO> messageDTOs = dao.readAll();
            ctx.res().setStatus(200);
            ctx.json(messageDTOs);
        } catch (EntityNotFoundException e) {
            throw new ApiException(404, "Messages not found");
        }
    }

    @Override
    public void create(Context ctx) {
        try {
            MessageDTO jsonRequest = ctx.bodyAsClass(MessageDTO.class);
            MessageDTO createdMessage = dao.create(jsonRequest);
            ctx.res().setStatus(201);
            ctx.json(createdMessage);
        } catch (ApiException e) {
            throw new ApiException(e.getStatusCode(), e.getMessage());
        } catch (Exception e) {
            throw new ApiException(400, e.getMessage());
        }
    }

    @Override
    public void update(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));

            MessageDTO jsonRequest = ctx.bodyAsClass(MessageDTO.class);
            MessageDTO updatedMessage = dao.update(id, jsonRequest);
            ctx.res().setStatus(201);
            ctx.json(updatedMessage);
        } catch (ApiException e) {
            throw new ApiException(e.getStatusCode(), e.getMessage());
        } catch (NumberFormatException e) {
            throw new ApiException(400, "Invalid id: " + ctx.pathParam("id"));
        } catch (EntityNotFoundException e) {
            throw new ApiException(404, "Message with id " + ctx.pathParam("id") + " not found");
        }
    }

    @Override
    public void delete(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            dao.delete(id);
            ctx.res().setStatus(204);
        } catch (NumberFormatException e) {
            throw new ApiException(400, "Invalid id: " + ctx.pathParam("id"));
        }
    }
}
