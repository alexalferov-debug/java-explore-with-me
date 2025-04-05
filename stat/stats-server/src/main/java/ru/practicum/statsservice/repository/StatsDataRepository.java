package ru.practicum.statsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.statscommon.dto.ViewStats;
import ru.practicum.statsservice.model.StatsData;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsDataRepository extends JpaRepository<StatsData, Long> {
    @Query("select new ru.practicum.statscommon.dto.ViewStats(hit.app,hit.uri,count(hit.ip))" +
            "from StatsData as hit " +
            "where hit.timestamp between ?1 and ?2 " +
            "group by hit.uri, hit.app " +
            "order by count(hit.id) desc")
    List<ViewStats> getStatsDataNotUnique(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.statscommon.dto.ViewStats(hit.app, hit.uri, count(DISTINCT hit.ip)) " +
            "from StatsData as hit " +
            "where hit.timestamp between ?1 and ?2 " +
            "group by hit.uri, hit.app " +
            "order by count(hit.id) desc")
    List<ViewStats> getStatsDataUnique(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.statscommon.dto.ViewStats(hit.app, hit.uri, count(hit.ip)) " +
            "from StatsData as hit " +
            "where hit.uri IN ?3 " +
            "and hit.timestamp between ?1 and ?2 " +
            "group by hit.uri, hit.app " +
            "order by count(hit.id) desc")
    List<ViewStats> getStatsDataNotUniqueWithUris(LocalDateTime start, LocalDateTime end, List<String> uris);


    @Query("select new ru.practicum.statscommon.dto.ViewStats(hit.app, hit.uri, count(DISTINCT hit.ip)) " +
            "from StatsData as hit " +
            "where hit.uri IN ?3 " +
            "and hit.timestamp between ?1 and ?2 " +
            "group by hit.uri, hit.app " +
            "order by count(hit.id) desc")
    List<ViewStats> getStatsDataUniqueWithUris(LocalDateTime start, LocalDateTime end, List<String> uris);
}