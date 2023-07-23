package fr.epita.assistants.jws.utils;
import lombok.*;
@NoArgsConstructor
@AllArgsConstructor
@With
@ToString
@Getter
@Setter
public class Player {
    long id;
    String name;
    long lives;
    long posX;
    long posY;
}
