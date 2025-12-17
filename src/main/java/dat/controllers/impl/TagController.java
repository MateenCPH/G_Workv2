package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.daos.impl.TagDAO;
import dat.dtos.TagDTO;
import dat.exceptions.ApiException;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;

import java.util.List;

public class TagController implements IController<TagDTO, Long> {
    private final TagDAO dao;

    public TagController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = TagDAO.getInstance(emf);
    }

    @Override
    public void readById(Context ctx) {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));

            TagDTO tagDTO = dao.readById(id);
            ctx.res().setStatus(200);
            ctx.json(tagDTO);
        } catch (IllegalArgumentException e) {
            throw new ApiException(400, "Invalid id: " + ctx.pathParam("id"));
        } catch (NoResultException | EntityNotFoundException e) {
            throw new ApiException(404, "Tag with id " + ctx.pathParam("id") + " not found");
        }
    }

    @Override
    public void readAll(Context ctx) {
        try {
            List<TagDTO> tagDTOs = dao.readAll();
            ctx.res().setStatus(200);
            ctx.json(tagDTOs);
        } catch (EntityNotFoundException e) {
            throw new ApiException(404, "Tags not found");
        }
    }

    @Override
    public void create(Context ctx) {
        return;
    }

    @Override
    public void update(Context ctx) {
        return;
    }

    @Override
    public void delete(Context ctx) {
        return;
    }
}
