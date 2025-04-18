package ru.practicum.service.dao.compilation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.practicum.service.exception.NotFoundException;
import ru.practicum.service.model.Compilation;
import ru.practicum.service.repository.CompilationRepository;

import java.util.List;

@Repository
public class CompilationDaoImpl implements CompilationDao {
    CompilationRepository compilationRepository;

    @Autowired
    public CompilationDaoImpl(CompilationRepository compilationRepository) {
        this.compilationRepository = compilationRepository;
    }

    @Override
    public Compilation save(Compilation compilation) {
        return compilationRepository.save(compilation);
    }

    @Override
    public void delete(Long id) {
        compilationRepository.deleteById(id);
    }

    @Override
    public Compilation get(Long id) {
        return compilationRepository.findById(id).orElseThrow(() -> new NotFoundException("Не найдена подборка с id: " + id));
    }

    @Override
    public List<Compilation> getAll(Pageable pageable) {
        return compilationRepository.findAll(pageable).getContent();
    }

    @Override
    public List<Compilation> getAllWithPinned(Boolean pinned, Pageable pageable) {
        return compilationRepository.findAllWithPinned(pinned, pageable);
    }
}
