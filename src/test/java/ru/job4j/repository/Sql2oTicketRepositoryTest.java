package ru.job4j.repository;

import org.junit.jupiter.api.*;
import org.sql2o.Sql2o;
import ru.job4j.configuration.DatasourceConfiguration;
import ru.job4j.model.Ticket;
import java.util.List;
import java.util.Properties;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oTicketRepositoryTest {
    private static Sql2oTicketRepository sql2oTicketRepository;
    private static Sql2o sql2o;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oUserRepositoryTest.class.getClassLoader()
                .getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        sql2o = configuration.databaseClient(datasource);
        sql2oTicketRepository = new Sql2oTicketRepository(sql2o);

    }

    @AfterEach
    public void clearTickets() {
        var tickets = sql2oTicketRepository.findAll();
        for (var ticket : tickets) {
            sql2oTicketRepository.deleteById(ticket.getId());
        }
    }

    @Test
    public void whenSaveThenGetSame() {
        var ticket = sql2oTicketRepository.save(
                new Ticket(0, 1, 10, 10, 1)
        ).get();
        var savedTicket = sql2oTicketRepository.findById(ticket.getId()).get();
        assertThat(savedTicket).usingRecursiveComparison().isEqualTo(ticket);
    }

    @Test
    public void whenSaveExistThenGetEmptyOptional() {
        sql2oTicketRepository.save(new Ticket(0, 1, 10, 10, 1));
        var savedTicket = sql2oTicketRepository.save(
                new Ticket(0, 1, 10, 10, 1)
        );
        assertThat(savedTicket).isEmpty();
    }

    @Test
    public void whenSaveSeveralAndGetAll() {
        var ticket1 = sql2oTicketRepository.save(new Ticket(0, 1, 10, 10, 1)).get();
        var ticket2 = sql2oTicketRepository.save(new Ticket(0, 2, 10, 10, 1)).get();
        var ticket3 = sql2oTicketRepository.save(new Ticket(0, 3, 10, 10, 1)).get();
        var result = sql2oTicketRepository.findAll();

        assertThat(result).isEqualTo(List.of(ticket1, ticket2, ticket3));
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        assertThat(sql2oTicketRepository.findAll()).isEqualTo(emptyList());
        assertThat(sql2oTicketRepository.findById(0)).isEqualTo(empty());
    }

    @Test
    public void whenDeleteThenGetEmptyOptional() {
        var ticket = sql2oTicketRepository.save(new Ticket(0, 1, 10, 10, 1)).get();
        var isDeleted = sql2oTicketRepository.deleteById(ticket.getId());
        var savedTicket = sql2oTicketRepository.findById(ticket.getId());
        assertThat(isDeleted).isTrue();
        assertThat(savedTicket).isEqualTo(empty());
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(sql2oTicketRepository.deleteById(0)).isFalse();
    }

    @Test
    public void whenUpdateThenGetUpdated() {
        var ticket = sql2oTicketRepository.save(new Ticket(0, 1, 10, 10, 1)).get();
        var updatedTicket = new Ticket(ticket.getId(), 2, 11, 11, 2);
        var isUpdated = sql2oTicketRepository.update(updatedTicket);
        var savedTicket = sql2oTicketRepository.findById(updatedTicket.getId()).get();
        assertThat(isUpdated).isTrue();
        assertThat(updatedTicket).usingRecursiveComparison().isEqualTo(savedTicket);
    }

    @Test
    public void whenUpdateUnExistingTicketThenGetFalse() {
        var ticket = new Ticket(0, 1, 10, 10, 1);
        var isUpdated = sql2oTicketRepository.update(ticket);
        assertThat(isUpdated).isFalse();
    }
}