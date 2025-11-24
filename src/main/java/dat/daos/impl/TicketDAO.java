package dat.daos.impl;

import dat.daos.IDAO;
import dat.dtos.TicketDTO;
import jakarta.persistence.EntityManagerFactory;

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
    public TicketDTO readById(Integer integer) {
        return null;
    }

    @Override
    public java.util.List<TicketDTO> readAll() {
        return null;
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
