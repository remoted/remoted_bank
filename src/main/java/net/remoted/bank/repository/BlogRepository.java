package net.remoted.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import net.remoted.bank.entity.Blog;

import java.util.List;


@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    @Query("SELECT b.title, COUNT(b) AS count FROM Blog b GROUP BY b.title ORDER BY count DESC")
    List<Object[]> findTop10Blogs();
}