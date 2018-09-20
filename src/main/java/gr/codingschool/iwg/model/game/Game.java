package gr.codingschool.iwg.model.game;

import javax.persistence.*;

/**
 * 
 */

@Entity
@Table(name = "games")
public class Game {

    @Id
    @SequenceGenerator(name = "games_id_seq", sequenceName = "games_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "games_id_seq")
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "odds", nullable = false)
    private int odds;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "prize", nullable = false)
    private int prize;

    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "rating", nullable = false, columnDefinition = "Double(1,2) default '0.00'")
    private double rating;

    @Column(name = "usersRated", nullable = false, columnDefinition = "Integer default '0'")
    private int usersRated;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOdds() {
        return odds;
    }

    public void setOdds(int odds) {
        this.odds = odds;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrize() {
        return prize;
    }

    public void setPrize(int prize) {
        this.prize = prize;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getUsersRated() {
        return usersRated;
    }

    public void setUsersRated(int usersRated) {
        this.usersRated = usersRated;
    }
}
