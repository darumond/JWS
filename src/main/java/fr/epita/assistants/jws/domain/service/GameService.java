package fr.epita.assistants.jws.domain.service;

import fr.epita.assistants.jws.converter.Convert;
import fr.epita.assistants.jws.data.model.GameModel;
import fr.epita.assistants.jws.data.model.PlayerModel;
import fr.epita.assistants.jws.data.repository.GameRepository;
import fr.epita.assistants.jws.data.repository.PlayerRepository;
import fr.epita.assistants.jws.domain.entity.GameEntity;
import fr.epita.assistants.jws.utils.GameState;
import fr.epita.assistants.jws.converter.Convert;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class GameService {
    @Inject
    GameRepository game_repository;
    @Inject
    PlayerRepository player_repository;

    @Transactional
    public GameEntity newGame (String name){

        GameModel gameModel = new GameModel().withStart_time(LocalDateTime.now()).withState(GameState.STARTING);
        PlayerModel playerModel = new PlayerModel().withLives(3).withName(name).withGame(gameModel);
        addNewPlayer(gameModel, playerModel);
        game_repository.persist(gameModel);
        return Convert.game_model_to_entity(gameModel);
    }
    @Transactional
    public void addNewPlayer(GameModel game, PlayerModel player) {
        if (game.players.size() == 0)
        {
            player.posx = 1;
            player.posy = 1;
        }
        if (game.players.size() == 1)
        {
            player.posx = 15;
            player.posy = 1;
        }
        if (game.players.size() == 2)
        {
            player.posx = 15;
            player.posy = 13;
        }
        if (game.players.size() == 3)
        {
            player.posx = 1;
            player.posy = 13;
        }
        player_repository.persist(player);
        game.players.add(player);
    }

    public List<GameEntity> getAllGame()
    {
        List<GameEntity> res = new ArrayList<>();
        for (var x : game_repository.listAll())
        {
            res.add(Convert.game_model_to_entity(x));
        }
        return res;
    }

    public GameEntity get_info_game(long gameid) {
        return Convert.game_model_to_entity(game_repository.findById(gameid));
    }

    @Transactional
    public GameEntity player_JoinGame(long gameid, String name) {
        GameModel game = game_repository.findById(gameid);
        if (game == null )
        {
            return  null;
        }
        if (game.players.size() > 3 || game.state == GameState.RUNNING || game.state == GameState.FINISHED)
        {
            GameEntity trop = new GameEntity();
            trop.id = 400;
            return trop;
        }
        PlayerModel playerModel = new PlayerModel();
        playerModel.name = name;
        playerModel.lives = 3;
        playerModel.game = game;
        addNewPlayer(game, playerModel);
        player_repository.persist(playerModel);
        return Convert.game_model_to_entity(game);
    }

    @Transactional

    public GameEntity starting_game(long id)
    {
        GameModel game = game_repository.findById(id);
        if (game == null)
        {
            return  null;
        }
        if (game.state == GameState.FINISHED)
        {
            GameEntity trop = new GameEntity();
            trop.id = 400;
            return trop;
        }
        game.state = GameState.RUNNING;
        if (game.players.size() == 1)
        {
            game.state = GameState.FINISHED;
        }
        return Convert.game_model_to_entity(game);
    }

    @Transactional

    public GameEntity moving_player(long game_id, long player_id, int x, int y) throws IOException {
        GameModel game = game_repository.findById(game_id);
        PlayerModel player = player_repository.findById(player_id);
        if (game == null || player == null)
        {
            return  null;
        }
        String path = System.getenv("JWS_MAP_PATH");
        List<String> res = Files.readAllLines(Paths.get(path));
        List<String> decodeMap = decode_map(res);
        int valid_x = player.posx -x;
        int valid_y = player.posy - y;
        if (decodeMap.get(y).charAt(x) == 'M' || game.state != GameState.RUNNING || valid_coord(valid_x,valid_y) || decodeMap.get(y).charAt(x) == 'W')
        {
            GameEntity trop = new GameEntity();
            trop.id = 400;
            return trop;
        }
            player.posx = x;
            player.posy = y;
            player_repository.persist(player);
            return Convert.game_model_to_entity(game);

    }
    public boolean valid_coord(int x , int y)
    {
        if ((x==1 || x==-1))
        {
            return false;
        }
        if ((y==1 || y==-1))
        {
            return false;
        }
        return true;
    }
    @Transactional
    public GameEntity put_bomb(long game_id, long player_id)
    {
        GameModel game = game_repository.findById(game_id);
        PlayerModel player = player_repository.findById(player_id);
        if (game == null || player == null)
        {
            return  null;
        }
        else
        {
            return Convert.game_model_to_entity(game);
        }
    }

    public static List<String> decode_map(List<String> map)
    {
        List<String> res = new ArrayList<>();

        for (String str : map)
        {
            StringBuilder decode_string = new StringBuilder("                                                                                   ");
            int i = 0;
            int j = 0;
            int index = 0;
            for(; i<str.length() ; i++)
            {
                int number = Character.getNumericValue(str.charAt(i));
                char next_character = str.charAt(i+1);
                for (; j< number; j++)
                {
                    decode_string.setCharAt(index,next_character);
                    index++;
                }
                j=0;
                i++;
            }
            res.add(decode_string.toString().trim());
        }
        return res;

    }
}
