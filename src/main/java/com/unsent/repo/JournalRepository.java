package com.unsent.repo;
import com.unsent.model.JournalItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface JournalRepository extends JpaRepository<JournalItem,Long> {
 List<JournalItem> findByKindOrderByCreatedAtDesc(String kind);
 List<JournalItem> findAllByOrderByCreatedAtDesc();
}
