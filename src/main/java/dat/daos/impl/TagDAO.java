package dat.daos.impl;

import dat.daos.IDAO;
import dat.dtos.TagDTO;
import dat.entities.Tag;
import dat.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class TagDAO implements IDAO<TagDTO, Long> {

    private static TagDAO instance;
    private static EntityManagerFactory emf;

    public static TagDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new TagDAO();
        }
        return instance;
    }

    @Override
    public TagDTO readById(Long id) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            Tag tag = em.find(Tag.class, id);
            if (tag == null) {
                throw new EntityNotFoundException("Tag with id " + id + " not found");
            }
            return new TagDTO(tag);
        } catch (EntityNotFoundException e) {
            throw new ApiException(404, e.getMessage());
        }
    }

    @Override
    public List<TagDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<TagDTO> query = em.createQuery(
                    "SELECT new dat.dtos.TagDTO(t) FROM Tag t ORDER BY t.name",
                    TagDTO.class
            );
            List<TagDTO> tags = query.getResultList();
            if (tags.isEmpty()) {
                throw new EntityNotFoundException("No tags found");
            }
            return tags;
        }
    }

    @Override
    public TagDTO create(TagDTO tagDTO) {
        return null;
    }

    @Override
    public TagDTO update(Long id, TagDTO tagDTO) throws ApiException {
        return null;
    }

    @Override
    public void delete(Long id) throws ApiException {
        return;
    }
}
