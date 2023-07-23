package fr.epita.assistants.jws.data.model;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.Startup;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.With;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
@Entity @Table(name = "player")
@AllArgsConstructor
@NoArgsConstructor
@With
@ToString
public class PlayerModel  {
    public @Id  @Startup(1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    public int lives;
    public String name;
    public int posx;
    public int posy;
    static public int position;
    public @Column(name = "lastbomb")
    static LocalDateTime lastbomb;
    public @Column(name = "lastmovement")
    static LocalDateTime lastmovement;

    @ManyToOne
    public GameModel game;
}