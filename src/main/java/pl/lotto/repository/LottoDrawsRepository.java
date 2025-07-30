package pl.lotto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.lotto.entity.lotto.LottoDraw;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LottoDrawsRepository extends JpaRepository<LottoDraw, Long> {

    @Query("SELECT n, COUNT(n), MAX(d.drawDate) " +
            "FROM LottoDraw d JOIN d.numbers n " +
            "GROUP BY n")
    List<Object[]> fetchNumberStats();

    @Query("""
            SELECT n, COUNT(n), MAX(d.drawDate)
            FROM LottoDraw d
            JOIN d.numbers n
            WHERE d.drawDate BETWEEN :startDate AND :endDate
            GROUP BY n
    """)
    List<Object[]> fetchNumberStatsInRange(@Param("startDate")LocalDate startDate,
                                            @Param("endDate") LocalDate endDate);

    @Query("SELECT MAX(d.drawDate) FROM LottoDraw d")
    LocalDate findMaxDrawDate();

    @Query("SELECT MIN(d.drawDate) FROM LottoDraw d")
    LocalDate findMinDrawDate();

    List<LottoDraw> findByDrawDateBetween(LocalDate startDate, LocalDate endDate);

}
