package dat.daos.impl;

import dat.daos.IDAO;
import dat.dtos.GroupDTO;
import dat.entities.Group;
import dat.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class GroupDAO implements IDAO<GroupDTO, Integer> {

    private static GroupDAO instance;
    private static EntityManagerFactory emf;

    public static GroupDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new GroupDAO();
        }
        return instance;
    }


    public GroupDTO readById(Integer id) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            Group group = em.find(Group.class, id);
            if (group == null) {
                throw new ApiException(404, "Group not found");
            }
            return new GroupDTO(group);
        }
    }

    @Override
    public List<GroupDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            List<Group> groups = em.createQuery("SELECT g FROM Group g", Group.class).getResultList();
            return groups.stream().map(GroupDTO::new).toList();
        }
    }

    @Override
    public GroupDTO create(GroupDTO groupDTO) throws Exception {
        // Not required for this task
        return null;
    }

    @Override
    public GroupDTO update(Integer integer, GroupDTO groupDTO) throws ApiException {
        return null;
    }

    @Override
    public void delete(Integer integer) throws ApiException {

    }
}
