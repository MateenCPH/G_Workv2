package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.daos.impl.GroupDAO;
import dat.dtos.GroupDTO;
import dat.exceptions.ApiException;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;

import java.util.List;

public class GroupController implements IController<GroupDTO, Long> {
    private final GroupDAO dao;

    public GroupController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = GroupDAO.getInstance(emf);
    }

    @Override
    public void readById(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));

            GroupDTO groupDTO = dao.readById(id);
            ctx.res().setStatus(200);
            ctx.json(groupDTO);
        } catch (IllegalArgumentException e) {
            throw new ApiException(400, "Invalid id: " + ctx.pathParam("id"));
        } catch (NoResultException | EntityNotFoundException | ApiException e) {
            throw new ApiException(404, "Group with id " + ctx.pathParam("id") + " not found");
        }
    }

    @Override
    public void readAll(Context ctx) {
        try {
            List<GroupDTO> groupDTOs = dao.readAll();
            ctx.res().setStatus(200);
            ctx.json(groupDTOs);
        } catch (EntityNotFoundException e) {
            throw new ApiException(404, "Groups not found");
        }
    }

    @Override
    public void create(Context ctx) {
        // Not implemented yet
    }

    @Override
    public void update(Context ctx) {
        // Not implemented yet
    }

    @Override
    public void delete(Context ctx) {
        // Not implemented yet
    }
}

