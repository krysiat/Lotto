package pl.lotto.entity.dto;

import lombok.Data;

import java.util.List;

@Data
public class ItemResultDTO {
    private List<ResultDTO> results;
}
