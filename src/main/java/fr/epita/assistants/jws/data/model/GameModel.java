package fr.epita.assistants.jws.data.model;

import fr.epita.assistants.jws.utils.GameState;
import io.quarkus.runtime.Startup;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.With;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name =  "game")
@AllArgsConstructor
@NoArgsConstructor
@With
@ToString
public class GameModel {
    public @Id @Startup(1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    public GameState state;
    public @Column(name = "starttime")
    LocalDateTime start_time;
    public @OneToMany
    List<PlayerModel> players = new ArrayList<>();
    @ElementCollection
    @CollectionTable(name = "game_map", joinColumns = @JoinColumn(name = "gamemodel_id"))
    @Column(name = "map")
    public List<String> map = new ArrayList<>();
}
