/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.codingschool.iwg.model.user;

import gr.codingschool.iwg.model.game.Game;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.jpa.repository.Modifying;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 */
@Entity
@Table(name = "users")
public class User{

    @Id
    @SequenceGenerator(name = "users_id_seq", sequenceName = "users_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    @Column(name = "id")
    private int id;

    @Column(name = "username")
    @NotEmpty(message = "*Please provide a username")
    private String username;

    @Column(name = "email")
    @Email(message = "*Please provide a valid Email")
    @NotEmpty(message = "*Please provide an email")
    private String email;

    @Column(name = "password")
    @Length(min = 5, message = "*Your password must have at least 5 characters")
    @NotEmpty(message = "*Please provide your password")
    //@Transient
    private String password;

    @Column(name = "firstName")
    @NotEmpty(message = "*Please provide your name")
    private String firstName;

    @Column(name = "lastName")
    @NotEmpty(message = "*Please provide your last name")
    private String lastName;

    @Column(name = "phoneNumber", nullable = true)
    private String phoneNumber;

    @Column(name = "address", nullable = true)
    private String address;

    @Column(name = "dateOfBirth")
    private Date dateOfBirth;

    @Column(name = "active")
    private int active;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "wallet", referencedColumnName = "id")
    private UserWallet wallet;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "userId", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "roleId", referencedColumnName = "id"))
    private Set<Role> roles;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "users_favourite_games", joinColumns = @JoinColumn(name = "userId", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "gameId", referencedColumnName = "id"))
    private Set<Game> favouriteGames;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "users_rated_games", joinColumns = @JoinColumn(name = "userId", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "gameId", referencedColumnName = "id"))
    private Set<Game> ratedGames;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public UserWallet getWallet(){ return wallet; }

    public void setWallet(UserWallet wallet){ this.wallet = wallet; }

    public Set<Role> getRoles() {
        return roles;
    }

    public String[] getRoleStrings(){
        String[] roleStrings = new String[roles.size()];
        int i = 0;
        for(Role r : roles){
            roleStrings[i] = r.getRole();
            i++;
        }
        return roleStrings;
    }

    public boolean hasRole(String role){
        for(Role r : roles){
            if(r.getRole().equals(role))
                return true;
        }
        return false;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Game> getFavouriteGames() {
        return favouriteGames;
    }

    public void setFavouriteGames(Set<Game> favouriteGames) {
        this.favouriteGames = favouriteGames;
    }

    public List<Integer> getListOfFavouriteGameIds(){
        List<Integer> favouriteGameIds = new ArrayList<>();

        for(Game game : favouriteGames){
            favouriteGameIds.add(game.getId());
        }

        return favouriteGameIds;
    }

    public Set<Game> getRatedGames() {
        return ratedGames;
    }

    public void setRatedGames(Set<Game> ratedGames) {
        this.ratedGames = ratedGames;
    }

    public List<Integer> getListOfRatedGameIds(){
        List<Integer> ratedGameIds = new ArrayList<>();

        for(Game game : ratedGames){
            ratedGameIds.add(game.getId());
        }

        return ratedGameIds;
    }
}
