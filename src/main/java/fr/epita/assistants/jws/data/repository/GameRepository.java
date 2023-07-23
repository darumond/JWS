package fr.epita.assistants.jws.data.repository;

import fr.epita.assistants.jws.data.model.GameModel;
import fr.epita.assistants.jws.data.model.PlayerModel;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped

public class GameRepository implements PanacheRepositoryBase<GameModel,Long> {
}
