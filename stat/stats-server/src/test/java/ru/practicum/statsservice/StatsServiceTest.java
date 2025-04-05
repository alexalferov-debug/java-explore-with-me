package ru.practicum.statsservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.statscommon.dto.EndpointHit;
import ru.practicum.statsservice.mapper.StatsMapper;
import ru.practicum.statsservice.model.StatsData;
import ru.practicum.statsservice.repository.StatsDataRepository;
import ru.practicum.statsservice.service.StatsService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class StatsServiceTest {

    @Mock
    private StatsDataRepository statsDataRepository;

    @InjectMocks
    private StatsService statsService;

    @Test
    void saveHit_ShouldCallRepositorySave() {
        EndpointHit dto = new EndpointHit("app", "uri", "ip", LocalDateTime.now());
        StatsData expected = StatsMapper.INSTANCE.fromDto(dto);

        statsService.saveHit(dto);

        ArgumentCaptor<StatsData> captor = ArgumentCaptor.forClass(StatsData.class);
        verify(statsDataRepository).save(captor.capture());

        StatsData actual = captor.getValue();
        assertEquals(expected.getApp(), actual.getApp());
        assertEquals(expected.getUri(), actual.getUri());
        assertEquals(expected.getIp(), actual.getIp());
        assertEquals(expected.getTimestamp().truncatedTo(ChronoUnit.SECONDS),
                actual.getTimestamp().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    void getStats_UniqueFalseAndUrisNull_ShouldCallNotUniqueMethod() {
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = LocalDateTime.now().plusHours(1);

        statsService.getStats(from, to, null, false);

        verify(statsDataRepository).getStatsDataNotUnique(from, to);
    }

    @Test
    void getStats_UniqueTrueAndUrisPresent_ShouldCallUniqueWithUrisMethod() {
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = LocalDateTime.now().plusHours(1);
        List<String> uris = List.of("/test");

        statsService.getStats(from, to, uris, true);

        verify(statsDataRepository).getStatsDataUniqueWithUris(from, to, uris);
    }
}