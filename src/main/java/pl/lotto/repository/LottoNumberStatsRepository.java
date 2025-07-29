package pl.lotto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lotto.entity.lotto.LottoNumberStat;

@Repository
public interface LottoNumberStatsRepository extends JpaRepository<LottoNumberStat, Long> {

}
