package pl.lotto.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lotto.entity.dto.LottoDrawDTO;
import pl.lotto.entity.lotto.LottoNumberStat;
import pl.lotto.service.LottoOpenAPIService;
import pl.lotto.service.LottoService;

import java.time.LocalDate;
import java.util.List;

@RestController
public class LottoRestController {
    private final LottoOpenAPIService lottoOpenAPIService;
    private final LottoService lottoService;
    public static final String BASE_URL = "/api/lotto";

    public LottoRestController(LottoOpenAPIService lottoOpenAPIService,
                               LottoService lottoService) {
        this.lottoOpenAPIService = lottoOpenAPIService;
        this.lottoService = lottoService;
    }

    @GetMapping(BASE_URL + "/stats")
    public ResponseEntity<List<LottoNumberStat>> getStatsForDates(@RequestParam LocalDate startDate,
                                                                  @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(lottoService.generateNumberStats(startDate, endDate));
    }

    @PostMapping(BASE_URL + "/saveStats")
    public void saveLottoNumberStats() {
        lottoService.generateAndSaveFullNumberStats();
    }

    @PutMapping(BASE_URL + "/updateStats")
    public void updateLottoNumberStats() {
        lottoService.generateAndSaveFullNumberStats();
    }

    @GetMapping(BASE_URL + "/result")
    public ResponseEntity<LottoDrawDTO> getLottoResult(@RequestParam LocalDate date) {
        return ResponseEntity.ok(lottoOpenAPIService.fetchLottoResultForDate(date));
    }

    @PostMapping(BASE_URL + "/saveData")
    public void saveLottoResults(@RequestParam LocalDate startDate,
                                 @RequestParam LocalDate endDate) {
        lottoOpenAPIService.fetchAndSaveResultsForDates(startDate, endDate);
    }
}
