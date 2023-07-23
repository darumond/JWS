package fr.epita.assistants.jws.domain.entity;

import fr.epita.assistants.jws.utils.GameState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GameEntity {
    public long id;
    public LocalDateTime starttime;
    public GameState state = GameState.STARTING;

    public List<PlayerEntity> players = new ArrayList<>();
}
