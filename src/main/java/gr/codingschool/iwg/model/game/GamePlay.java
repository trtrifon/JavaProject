package gr.codingschool.iwg.model.game;

import gr.codingschool.iwg.model.user.User;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "games_plays")
public class GamePlay {
    @Id
    @SequenceGenerator(name = "games_plays_id_seq", sequenceName = "games_plays_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "games_plays_id_seq")
    @Column(name = "id", nullable = false)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "gameId", referencedColumnName = "id")
    private Game game;

    @Column(name = "date",nullable = false,insertable = false,updatable = false,
            columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(name = "outcome")
    private Boolean outcome;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getOutcome(){ return outcome; }

    public void setOutcome(Boolean outcome) { this.outcome = outcome; }
}
