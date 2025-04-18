package ru.practicum.service.controller.compilation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.dto.compilation.CompilationDto;
import ru.practicum.service.service.compilation.CompilationService;

import java.util.List;

@RestController
@RequestMapping("/compilations")
public class PublicCompilationsController {

    CompilationService compilationService;

    public PublicCompilationsController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @GetMapping
    public ResponseEntity<List<CompilationDto>> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                                @RequestParam(defaultValue = "0") Integer from,
                                                                @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(compilationService.getCompilations(pinned, from, size));
    }

    @GetMapping("/{compId}")
    public ResponseEntity<CompilationDto> getCompilation(@PathVariable Long compId) {
        return ResponseEntity.ok(compilationService.getCompilation(compId));
    }
}
