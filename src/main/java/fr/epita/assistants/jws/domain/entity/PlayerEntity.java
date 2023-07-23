package fr.epita.assistants.jws.domain.entity;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor @With @ToString
public class PlayerEntity {
    public long id;
    public LocalDateTime lastbomb;
    public LocalDateTime lastmovement;
    public int lives;
    public String name;
    public int posx;
    public int posy;
    public int position;
}
