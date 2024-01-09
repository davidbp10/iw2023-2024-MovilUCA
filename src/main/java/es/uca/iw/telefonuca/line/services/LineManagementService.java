package es.uca.iw.telefonuca.line.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import es.uca.iw.telefonuca.line.domain.Line;
import es.uca.iw.telefonuca.line.repositories.LineRepository;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
public class LineManagementService {
    private final LineRepository lineRepository;

    public LineManagementService(LineRepository lineManagementRepository) {
        this.lineRepository = lineManagementRepository;
    }

    @Transactional
    public List<Line> loadAll() {
        return lineRepository.findAll();
    }

    @Transactional
    public Line loadLineById(UUID id) {
        Optional<Line> line = lineRepository.findById(id);
        if (line.isEmpty()) {
            throw new RuntimeException("No line present with id: " + id);
        } else {
            return line.get();
        }
    }

    @Transactional
    public Line loadLineByName(String name) {
        Optional<Line> line = lineRepository.findByName(name);
        if (line.isEmpty()) {
            throw new RuntimeException("No line present with name: " + name);
        } else {
            return line.get();
        }
    }

    @Transactional
    public List<Line> loadLineByDescription(String description) {
        List<Line> lines = lineRepository.findByDescription(description);
        if (lines.isEmpty()) {
            throw new RuntimeException("No lines present with description: " + description);
        } else {
            return lines;
        }
    }

    @Transactional
    public List<Line> loadLinesByPricePerMinute(int pricePerMinute) {
        List<Line> lines = lineRepository.findByPricePerMinute(pricePerMinute);
        if (lines.isEmpty()) {
            throw new RuntimeException("No lines present with pricePerMinute: " + pricePerMinute);
        } else {
            return lines;
        }
    }

    @Transactional
    public List<Line> loadLinesByPricePerMegabyte(int pricePerMegabyte) {
        List<Line> lines = lineRepository.findByPricePerMegabyte(pricePerMegabyte);
        if (lines.isEmpty()) {
            throw new RuntimeException("No lines present with pricePerMegabyte: " + pricePerMegabyte);
        } else {
            return lines;
        }
    }

    @Transactional
    public List<Line> loadLinesByMinimumMonths(int minimumMonths) {
        List<Line> lines = lineRepository.findByMinimumMonths(minimumMonths);
        if (lines.isEmpty()) {
            throw new RuntimeException("No lines present with minimumMonths: " + minimumMonths);
        } else {
            return lines;
        }
    }

    @Transactional
    public Line saveLine(Line line) {
        return lineRepository.save(line);
    }

    @Transactional
    public void deleteLine(Line line) {
        lineRepository.delete(line);
    }

}
