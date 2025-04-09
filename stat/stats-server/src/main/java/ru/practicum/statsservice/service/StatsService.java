package ru.practicum.statsservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.statscommon.dto.EndpointHit;
import ru.practicum.statscommon.dto.ViewStats;
import ru.practicum.statsservice.mapper.StatsMapper;
import ru.practicum.statsservice.model.StatsData;
import ru.practicum.statsservice.repository.StatsDataRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class StatsService {

    StatsDataRepository statsDataRepository;

    @Autowired
    public StatsService(StatsDataRepository statsDataRepository) {
        this.statsDataRepository = statsDataRepository;
    }

    @Transactional
    public void saveHit(EndpointHit endpointHit) {
        StatsData data = StatsMapper.INSTANCE.fromDto(endpointHit);
        statsDataRepository.save(data);
    }

    public List<ViewStats> getStats(LocalDateTime from, LocalDateTime to, List<String> uris, boolean unique) {
        if (unique) {
            if (Objects.isNull(uris) || uris.isEmpty()) {
                return statsDataRepository.getStatsDataUnique(from, to);
            } else {
                return statsDataRepository.getStatsDataUniqueWithUris(from, to, uris);
            }
        } else {
            if (Objects.isNull(uris) || uris.isEmpty()) {
                return statsDataRepository.getStatsDataNotUnique(from, to);
            } else {
                return statsDataRepository.getStatsDataNotUniqueWithUris(from, to, uris);
            }
        }
    }
}
