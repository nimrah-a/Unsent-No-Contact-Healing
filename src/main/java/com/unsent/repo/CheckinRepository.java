package com.unsent.repo;
import com.unsent.model.DailyCheckin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.*;
public interface CheckinRepository extends JpaRepository<DailyCheckin,Long>{
 Optional<DailyCheckin> findByJourneyIdAndDate(Long journeyId,LocalDate date);
 List<DailyCheckin> findByJourneyIdOrderByDateDesc(Long journeyId);
}
