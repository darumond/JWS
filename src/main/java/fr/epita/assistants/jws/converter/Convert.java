package fr.epita.assistants.jws.converter;

import fr.epita.assistants.jws.data.model.GameModel;
import fr.epita.assistants.jws.data.model.PlayerModel;
import fr.epita.assistants.jws.domain.entity.GameEntity;
import fr.epita.assistants.jws.domain.entity.PlayerEntity;


import java.util.ArrayList;
import java.util.List;

public class Convert {
    public static GameEntity game_model_to_entity(GameModel gameModel)
    {
        if (gameModel == null)
        {
            return null;
        }
        List<PlayerEntity> playerEntities = new ArrayList<>();
        for (var i : gameModel.players)
        {
            playerEntities.add(player_model_to_entity(i));
        }
        GameEntity res = new GameEntity();
        res.id = gameModel.id;
        res.starttime = gameModel.start_time;
        res.state = gameModel.state;
        res.players = playerEntities;
        return res;

    }
    public static PlayerEntity player_model_to_entity(PlayerModel playerModel)
    {
        if (playerModel == null)
        {
            return null;
        }
        PlayerEntity res = new PlayerEntity();
        res.name = playerModel.name;
        res.id = playerModel.id;
        res.lives = playerModel.lives;
        res.posx = playerModel.posx;
        res.posy = playerModel.posy;
        res.lastbomb = playerModel.lastbomb;
        res.lastmovement = playerModel.lastmovement;
        res.position = playerModel.position;
        return res;

    }
}
