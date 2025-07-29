package pl.lotto.entity.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ResultDTO {
    private LocalDate drawDate;
    private String gameType;
    private List<Integer> resultsJson;
}
