package pl.lotto.entity.dto;

import lombok.Data;

import java.util.List;

@Data
public class LottoResultResponseDTO {
    private List<ItemResultDTO> items;
}
