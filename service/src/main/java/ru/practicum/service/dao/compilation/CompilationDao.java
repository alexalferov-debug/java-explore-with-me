package ru.practicum.service.dao.compilation;

import org.springframework.data.domain.Pageable;
import ru.practicum.service.model.Compilation;

import java.util.List;

public interface CompilationDao {
    Compilation save(Compilation compilation);

    void delete(Long id);

    Compilation get(Long id);

    List<Compilation> getAll(Pageable pageable);

    List<Compilation> getAllWithPinned(Boolean pinned, Pageable pageable);
}
