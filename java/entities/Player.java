package entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "player_generator")
    @SequenceGenerator(name="player_generator", sequenceName = "seq_player", allocationSize = 1)
    private long id;

    private long discord_id;

    private String nickname;

    private LocalDate joined_clan;

    private long steam64_id;

    private LocalDate recruit_start;

    private LocalDate recruit_finish;

    private LocalDate holiday_start;

    private LocalDate holiday_finish;

}