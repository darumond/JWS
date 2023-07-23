package fr.epita.assistants.jws.presentation.rest.response;

import java.util.List;

import fr.epita.assistants.jws.utils.GameState;
import fr.epita.assistants.jws.utils.Player;
import lombok.*;

@AllArgsConstructor @NoArgsConstructor @With @ToString @Getter
@Setter
public class GameDetailResponse {

    String startTime;
    GameState state;
    List<Player> players;
    List<String> map;
    int id;

}
