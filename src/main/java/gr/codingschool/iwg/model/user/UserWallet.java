package gr.codingschool.iwg.model.user;

import javax.persistence.*;

@Entity
@Table(name = "wallet")
public class UserWallet {
    @Id
    @SequenceGenerator(name = "wallet_id_seq", sequenceName = "wallet_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wallet_id_seq")
    @Column(name = "id")
    private int id;

    @Column(name = "balance")
    private int balance;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
