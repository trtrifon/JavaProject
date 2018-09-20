package gr.codingschool.iwg.model.game;

import gr.codingschool.iwg.model.user.User;

import javax.persistence.*;

/**
 * 
 */
@Entity
@Table(name = "games_tries")
public class GameTries{
    @Id
    @SequenceGenerator(name = "game_tries_id_seq", sequenceName = "game_tries_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "game_tries_id_seq")
    @Column(name = "id", nullable = false)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "gameId", referencedColumnName = "id")
    private Game game;

    @Column(name = "tries")
    private int tries;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public int getTries() {
        return tries;
    }

    public void setTries(int tries) {
        this.tries = tries;
    }
}
