package fr.epita.assistants.jws.presentation.rest;


import fr.epita.assistants.jws.domain.entity.GameEntity;
import fr.epita.assistants.jws.domain.service.GameService;
import fr.epita.assistants.jws.presentation.rest.request.CreateGameRequest;
import fr.epita.assistants.jws.presentation.rest.request.JoinGameRequest;
import fr.epita.assistants.jws.presentation.rest.request.MovePlayerRequest;
import fr.epita.assistants.jws.presentation.rest.request.PutBombRequest;
import fr.epita.assistants.jws.presentation.rest.response.GameDetailResponse;
import fr.epita.assistants.jws.presentation.rest.response.GameListResponse;
import fr.epita.assistants.jws.utils.GameState;
import fr.epita.assistants.jws.utils.Player;

import javax.management.BadAttributeValueExpException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Endpoint {
    @Inject
    GameService gameService;

    @Path("/games/{gameId}/start")
    @PATCH
    public Response Start_game(@PathParam("gameId") final long id) throws IOException {
        GameEntity gameEntity = gameService.starting_game(id);
        if (gameEntity == null)
        {
            return Response.status(404, "Game not found").build();

        }
        if (gameEntity.id == 400)
        {
            return Response.status(404,"PAS POSSIBLE LA").build();
        }
        List<String> map = init_map_env();
        return Response.ok(getGameDetailResponse(gameEntity).withMap(map)).build();
    }
    @POST
    @Path("/games")
    public Response create_new_game(CreateGameRequest name) throws IOException {
        if (name.name == null || name == null) {
            return Response.status(400).build();
        }
        List<String> map;
        GameEntity newgame = gameService.newGame(name.name);
        String getmap = System.getenv("JWS_MAP_PATH");
        if (getmap != null)
        {
            map = Files.readAllLines(Paths.get(getmap));
            GameDetailResponse res = new GameDetailResponse();
            res.setMap(map);
            return Response.ok(getGameDetailResponse(newgame).withMap(res.getMap())).build();
        }
        System.out.println("Il n'y a pas de variable env");
        return null;
    }
    @GET
    @Path("/games")
    public Response get_all_games()
    {
        List<GameListResponse> res = new ArrayList<>();
        for (var game: gameService.getAllGame())
        {
            GameListResponse x = new GameListResponse();
            x.state = game.state;
            x.id = (int) game.id;
            x.players = game.players.size();
            res.add(x);
        }
        return Response.ok(res).build();
    }
    private GameDetailResponse getGameDetailResponse(GameEntity gameEntity) {
        List<Player> players = new ArrayList<>();
        for (var play : gameEntity.players)
        {
            players.add(new Player().withName(play.name).withPosX(play.posx).withId(play.id).withPosY(play.posy).withLives(play.lives));
        }
        GameDetailResponse res = new GameDetailResponse();
        res.setState(gameEntity.state);
        res.setMap(new ArrayList<>());
        res.setId((int) gameEntity.id);
        res.setStartTime(String.valueOf(gameEntity.starttime));
        res.setPlayers(players);
        return res;
    }
    @GET
    @Path("/games/{gameId}")
    public Response get_info_game(@PathParam("gameId") final long gameid) throws IOException {
        GameEntity gameEntity = gameService.get_info_game(gameid);
        if (gameEntity == null) {
            return Response.status(404,"Game not found").build();
        }
        List<String> map = init_map_env();

        return Response.ok(getGameDetailResponse(gameEntity).withMap(map)).build();
    }
    @Path("/games/{gameId}")
    @POST
    public Response join_game(@PathParam("gameId") final long id, JoinGameRequest playerRequest) throws IOException {
        if (playerRequest == null || playerRequest.getName() == null)
        {
            return Response.status(400,"PAS POSSIBLE LA").build();
        }
        GameEntity gameEntity = gameService.player_JoinGame(id,playerRequest.getName());
        if (gameEntity == null) {
            return Response.status(404,"Game not found").build();
        }
        if (gameEntity.id == 400)
        {
            return Response.status(400,"PAS POSSIBLE LA").build();
        }
        List<String> map = init_map_env();

        return Response.ok(getGameDetailResponse(gameEntity).withMap(map)).build();
    }

    @Path("/games/{gameId}/players/{playerId}/move")
    @POST
    public Response move_player(@PathParam("gameId") final long game_id, @PathParam("playerId") final long player_id, MovePlayerRequest playerRequest) throws IOException {
        GameEntity gameEntity = gameService.moving_player(game_id,player_id,playerRequest.getPosX(),playerRequest.getPosY());
        if (gameEntity == null) {
            return Response.status(404,"Game not found").build();
        }
        if (gameEntity.id == 400 || playerRequest.getPosX() == -100 || playerRequest.getPosY() == -100)
        {
            return Response.status(400,"PAS POSSIBLE LA").build();
        }
        List<String> map = init_map_env();
        return Response.ok(getGameDetailResponse(gameEntity).withMap(map)).build();
    }

    @Path("/games/{gameId}/players/{playerId}/bomb")
    @POST
    public Response put_bomb(@PathParam("gameId") final long game_id, @PathParam("playerId") final long player_id, PutBombRequest playerRequest) throws IOException {
        GameEntity gameEntity = gameService.put_bomb(game_id,player_id);
        if (gameEntity == null) {
            return Response.status(404,"Game not found").build();
        }
        else
        {
            return Response.status(400,"PAS POSSIBLE LA").build();
        }
//        List<String> map = init_map_env();
//
//        return Response.ok(getGameDetailResponse(gameEntity).withMap(map)).build();

    }
    public List<String> init_map_env() throws IOException {
        String path = System.getenv("JWS_MAP_PATH");
        List<String> res = Files.readAllLines(Paths.get(path));
        return res;
    }
}
