package dat.daos.impl;

import dat.daos.IDAO;
import dat.dtos.TicketDTO;
import dat.entities.Ticket;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class TicketDAO implements IDAO<TicketDTO, Integer> {

    private static TicketDAO instance;
    private static EntityManagerFactory emf;

    public static TicketDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new TicketDAO();
        }
        return instance;
    }

    @Override
    public TicketDTO readById(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<TicketDTO> query = em.createQuery("SELECT new dat.dtos.TicketDTO(t) FROM Ticket t WHERE t.id = :id", TicketDTO.class);
            query.setParameter("id", id);
            TicketDTO ticketDTO = query.getSingleResult();
            if (ticketDTO == null) {
                throw new EntityNotFoundException("Ticket with ID " + id + " not found");
            }
            return ticketDTO;
        }
    }

    @Override
    public List<TicketDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<TicketDTO> query = em.createQuery("SELECT new dat.dtos.TicketDTO(t) FROM Ticket t", TicketDTO.class);
            List<TicketDTO> tickets = query.getResultList();
            if (tickets.isEmpty()) {
                throw new EntityNotFoundException("No tickets found");
            }
            return tickets;
        }
    }

    @Override
    public TicketDTO create(TicketDTO ticketDTO) {
        return null;
    }

    @Override
    public TicketDTO update(Integer integer, TicketDTO ticketDTO) {
        return null;
    }

    @Override
    public void delete(Integer integer) {

    }
}
