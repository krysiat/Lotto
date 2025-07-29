package pl.lotto.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.lotto.entity.lotto.LottoDraw;
import pl.lotto.entity.lotto.LottoNumberStat;
import pl.lotto.entity.dto.LottoDrawDTO;
import pl.lotto.entity.dto.LottoResultResponseDTO;
import pl.lotto.repository.LottoDrawsRepository;
import pl.lotto.repository.LottoNumberStatsRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LottoOpenAPIService {
    private final LottoDrawsRepository lottoDrawRepository;
    private final LottoNumberStatsRepository lottoNumberStatsRepository;
    private final RestTemplate restTemplate;
    @Value("${lotto.api.secret}")
    private String secret;

    public LottoOpenAPIService(LottoDrawsRepository lottoDrawRepository,
                               LottoNumberStatsRepository lottoNumberStatsRepository,
                               RestTemplate restTemplate) {
        this.lottoDrawRepository = lottoDrawRepository;
        this.lottoNumberStatsRepository = lottoNumberStatsRepository;
        this.restTemplate = restTemplate;
    }

    public void generateAndSaveNumberStats() {
        List<Object[]> stats = lottoDrawRepository.fetchNumberStats();

        List<LocalDate> allDates = lottoDrawRepository.findAll().stream()
                .map(LottoDraw::getDrawDate)
                .sorted()
                .collect(Collectors.toList());

        List<LottoNumberStat> statsToSave = stats.stream().map(row -> {
            Integer number = ((Number) row[0]).intValue();
            Integer occurrences = ((Number) row[1]).intValue();
            LocalDate lastDrawn = (LocalDate) row[2];

            Integer skipCount = (Integer) (allDates.size() - allDates.indexOf(lastDrawn) - 1);

            return LottoNumberStat.builder()
                    .number(number)
                    .occurrences(occurrences)
                    .lastDrawn(lastDrawn)
                    .skipCount(skipCount)
                    .build();
        }).collect(Collectors.toList());

        lottoNumberStatsRepository.saveAll(statsToSave);
    }


    public void fetchAndSaveResultsForDates(LocalDate startDate, LocalDate endDate) {
        while (!startDate.isAfter(endDate)) {
            LottoDrawDTO dto = fetchLottoResultForDate(startDate);
            if (dto != null && !dto.getGameType().equals("LottoPlus")) {
                lottoDrawRepository.save(LottoDraw.builder()
                        .drawDate(dto.getDrawDate())
                        .numbers(dto.getNumbers())
                        .build());
                try {
                    // Lotto API cannot handle too much requests. Sometimes works with 2s, but 5s is safe.
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.warn("Wątek został przerwany podczas uśpienia", e);
                }
            }
            startDate = startDate.plusDays(1);
        }
        // to update every time there are new records
        generateAndSaveNumberStats();
    }

    public LottoDrawDTO fetchLottoResultForDate(LocalDate drawDate) {
        String baseUrl = "https://developers.lotto.pl/api/open/v1/";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateString = drawDate.format(formatter);
        String url = baseUrl + "/lotteries/draw-results/by-date-per-game?gameType=Lotto&drawDate={dateString}"
                + "&index=1&size=10&sort=drawDate&order=DESC";

        HttpHeaders headers = new HttpHeaders();
        headers.set("secret", secret);
        headers.set("accept", "application/json");

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<LottoResultResponseDTO> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    LottoResultResponseDTO.class,
                    dateString
            );

            LottoResultResponseDTO body = responseEntity.getBody();

            return LottoDrawDTO.builder()
                    // get(0) - gives me Lotto, and ommits Lotto Plus (on purpose)
                    .drawDate(body.getItems().get(0).getResults().get(0).getDrawDate())
                    .gameType(body.getItems().get(0).getResults().get(0).getGameType())
                    .numbers(body.getItems().get(0).getResults().get(0).getResultsJson())
                    .build();
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        } catch (HttpClientErrorException.TooManyRequests e) {
            log.warn("Osiągnięto limit zapytań do Lotto API (kod 429).", e);
            return null;
        } catch (Exception e) {
            log.info("Inny błąd podczas pobierania danych z Lotto API", e);
            return null;
        }
    }

}
