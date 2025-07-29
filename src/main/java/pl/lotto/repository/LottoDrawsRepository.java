package pl.lotto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.lotto.entity.lotto.LottoDraw;

import java.util.List;

@Repository
public interface LottoDrawsRepository extends JpaRepository<LottoDraw, Long> {

    @Query("SELECT n, COUNT(n), MAX(d.drawDate) " +
            "FROM LottoDraw d JOIN d.numbers n " +
            "GROUP BY n")
    List<Object[]> fetchNumberStats();

}
