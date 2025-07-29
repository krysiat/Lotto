package pl.lotto.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lotto.entity.dto.LottoDrawDTO;
import pl.lotto.service.LottoOpenAPIService;

import java.time.LocalDate;

@RestController
public class LottoRestController {
    private final LottoOpenAPIService lottoService;
    public static final String BASE_URL = "/api/lotto";

    public LottoRestController(LottoOpenAPIService lottoService) {
        this.lottoService = lottoService;
    }

    @PostMapping(BASE_URL + "/saveStats")
    public void saveLottoNumberStats() {
        lottoService.generateAndSaveNumberStats();
    }

    @PutMapping(BASE_URL + "/updateStats")
    public void updateLottoNumberStats() {
        lottoService.generateAndSaveNumberStats();
    }

    @GetMapping(BASE_URL + "/result")
    public ResponseEntity<LottoDrawDTO> getLottoResult(@RequestParam LocalDate date) {
        return ResponseEntity.ok(lottoService.fetchLottoResultForDate(date));
    }

    @PostMapping(BASE_URL + "/saveData")
    public void saveLottoResults(@RequestParam LocalDate startDate,
                                 @RequestParam LocalDate endDate) {
        lottoService.fetchAndSaveResultsForDates(startDate, endDate);
    }
}
