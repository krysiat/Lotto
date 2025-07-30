package pl.lotto.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lotto.entity.lotto.LottoDraw;
import pl.lotto.entity.lotto.LottoNumberStat;
import pl.lotto.repository.LottoDrawsRepository;
import pl.lotto.repository.LottoNumberStatsRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class LottoService {
    private final LottoDrawsRepository lottoDrawRepository;
    private final LottoNumberStatsRepository lottoNumberStatsRepository;

    public List<LottoNumberStat> generateNumberStats(LocalDate startDate, LocalDate endDate) {
        List<LottoDraw> filteredDraws = lottoDrawRepository.findByDrawDateBetween(startDate, endDate);
        List<Object[]> stats = lottoDrawRepository.fetchNumberStatsInRange(startDate, endDate);

        List<LocalDate> allDates = filteredDraws.stream()
                .map(LottoDraw::getDrawDate)
                .sorted()
                .toList();

        return stats.stream().map(row -> {
            Integer number = ((Number) row[0]).intValue();
            Integer occurrences = ((Number) row[1]).intValue();
            LocalDate lastDrawn = (LocalDate) row[2];

            Integer skipCount = (allDates.size() - allDates.indexOf(lastDrawn) - 1);

            return LottoNumberStat.builder()
                    .number(number)
                    .occurrences(occurrences)
                    .lastDrawn(lastDrawn)
                    .skipCount(skipCount)
                    .build();
        }).toList();
    }

    public void generateAndSaveFullNumberStats() {
        LocalDate startDate = lottoDrawRepository.findMinDrawDate();
        LocalDate endDate = lottoDrawRepository.findMaxDrawDate();

        List<LottoNumberStat> stats = generateNumberStats(startDate, endDate);
        lottoNumberStatsRepository.saveAll(stats);
    }
}
