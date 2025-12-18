package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.daos.impl.UserDAO;
import dat.dtos.UserDTO;
import dat.exceptions.ApiException;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;

import java.util.List;

public class UserController implements IController<UserDTO, Integer> {
    private final UserDAO dao;

    public UserController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = UserDAO.getInstance(emf);
    }

    @Override
    public void readById(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));

            UserDTO userDTO = dao.readById(id);
            ctx.res().setStatus(200);
            ctx.json(userDTO);
        } catch (IllegalArgumentException e) {
            throw new ApiException(400, "Invalid id: " + ctx.pathParam("id"));
        } catch (NoResultException | EntityNotFoundException e) {
            throw new ApiException(404, "User with id " + ctx.pathParam("id") + " not found");
        }
    }

    @Override
    public void readAll(Context ctx) {
        try {
            List<UserDTO> userDTOS = dao.readAll();
            ctx.res().setStatus(200);
            ctx.json(userDTOS);
        } catch (EntityNotFoundException e) {
            throw new ApiException(404, "Users not found");
        }
    }

    @Override
    public void create(Context ctx) {

    }

    @Override
    public void update(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            UserDTO userDTO = ctx.bodyAsClass(UserDTO.class);

            UserDTO updatedUser = dao.update(id, userDTO);
            ctx.res().setStatus(200);
            ctx.json(updatedUser);
        } catch (IllegalArgumentException e) {
            throw new ApiException(400, "Invalid id: " + ctx.pathParam("id"));
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(500, "Error updating user: " + e.getMessage());
        }
    }

    @Override
    public void delete(Context ctx) {

    }
}
