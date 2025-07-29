package pl.lotto.entity.lotto;

import jakarta.persistence.*;
import lombok.*;
import pl.lotto.entity.BaseEntity;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "lotto_draws")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
public class LottoDraw extends BaseEntity<Long> {

    @Column(name = "draw_date")
    private LocalDate drawDate;

    @ElementCollection
    @CollectionTable(name = "lotto_draw_numbers",
            joinColumns = @JoinColumn(name = "draw_id"))
    @Column(name = "number")
    private List<Integer> numbers;
}
