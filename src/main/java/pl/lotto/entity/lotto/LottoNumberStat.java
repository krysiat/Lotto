package pl.lotto.entity.lotto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "lotto_number_stats")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LottoNumberStat {

    @Id
    @Column(name = "number")
    private Integer number;

    @Column(name = "occurrences")
    private Integer occurrences;

    @Column(name = "last_drawn")
    private LocalDate lastDrawn;

    @Column(name = "skip_count")
    private Integer skipCount;
}
