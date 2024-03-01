package ru.job4j.dreamjob.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import javax.annotation.concurrent.ThreadSafe;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@ThreadSafe
public class MemoryCandidateRepository implements CandidateRepository {
    private final AtomicInteger nextId = new AtomicInteger(1);
    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private MemoryCandidateRepository() {
        save(new Candidate(0, "Vadim", "Good guy", LocalDateTime.now(), 1, 0));
        save(new Candidate(0, "Sergei", "Very good guy", LocalDateTime.now(), 2, 0));
        save(new Candidate(0, "Sasha", "Bad guy", LocalDateTime.now(), 3, 0));
        save(new Candidate(0, "Anton", "Bad bad guy", LocalDateTime.now(), 4, 0));
        save(new Candidate(0, "Valera", "Top notch guy", LocalDateTime.now(), 5, 0));
        save(new Candidate(0, "Dima", "Asshole", LocalDateTime.now(), 6, 0));
    }

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId.incrementAndGet());
        candidates.put(candidate.getId(), candidate);
        return candidate;
    }

    @Override
    public boolean deleteById(int id) {
        return candidates.remove(id) != null;
    }

    @Override
    public boolean update(Candidate candidate) {
        return candidates.computeIfPresent(candidate.getId(),
                (id, oldCandidate) -> new Candidate(candidate.getId(), candidate.getName(),
                        candidate.getDescription(), candidate.getCreationDate(),
                        candidate.getCityId(), candidate.getFileId())) != null;
    }

    @Override
    public Optional<Candidate> findById(int id) {
        return Optional.ofNullable(candidates.get(id));
    }

    @Override
    public Collection<Candidate> findAll() {
        return candidates.values();
    }
}
