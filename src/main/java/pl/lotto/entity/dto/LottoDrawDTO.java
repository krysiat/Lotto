package pl.lotto.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class LottoDrawDTO {
    private LocalDate drawDate;
    private String gameType;
    private List<Integer> numbers;
}
