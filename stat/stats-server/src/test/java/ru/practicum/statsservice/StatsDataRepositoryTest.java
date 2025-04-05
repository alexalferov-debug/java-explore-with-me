package ru.practicum.statsservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.statscommon.dto.ViewStats;
import ru.practicum.statsservice.model.StatsData;
import ru.practicum.statsservice.repository.StatsDataRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
public class StatsDataRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StatsDataRepository repository;

    @Test
    void getStatsDataUnique_ShouldCountDistinctIps() {
        StatsData data1 = StatsData
                .builder()
                .app("app1")
                .ip("192.168.1.1")
                .uri("/test")
                .timestamp(LocalDateTime.now())
                .build();
        StatsData data2 = StatsData
                .builder()
                .app("app1")
                .ip("192.168.1.1")
                .uri("/test")
                .timestamp(LocalDateTime.now())
                .build();
        entityManager.persist(data1);
        entityManager.persist(data2);

        List<ViewStats> result = repository.getStatsDataUnique(
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now().plusHours(1)
        );

        assertEquals(1, result.size());
        assertEquals(1, result.getFirst().getHits());
    }

    @Test
    void getStatsDataNotUniqueWithUris_ShouldFilterByUris() {
        StatsData data1 = StatsData
                .builder()
                .app("app1")
                .ip("192.168.1.1")
                .uri("/test")
                .timestamp(LocalDateTime.now())
                .build();
        StatsData data2 =StatsData
                .builder()
                .app("app1")
                .ip("192.168.1.2")
                .uri("/test")
                .timestamp(LocalDateTime.now())
                .build();
        entityManager.persist(data1);
        entityManager.persist(data2);

        List<ViewStats> result = repository.getStatsDataNotUniqueWithUris(
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now().plusHours(1),
                List.of("/test")
        );

        assertEquals(1, result.size());
        assertEquals("/test", result.getFirst().getUri());
    }
}
