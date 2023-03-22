package net.remoted.bank.repository;

import net.remoted.bank.entity.SearchQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SearchQueryRepository extends JpaRepository<SearchQuery, Long> {
    @Query(value = "SELECT query, COUNT(*) as count FROM search_query GROUP BY query ORDER BY count DESC LIMIT 10", nativeQuery = true)
    List<Object[]> findTop10SearchQueries();
}