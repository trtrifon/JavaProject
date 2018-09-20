package gr.codingschool.iwg.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "coupons")
public class Coupon {
    @Id
    @SequenceGenerator(name = "coupon_id_seq", sequenceName = "coupon_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "coupon_id_seq")
    @Column(name = "id")
    private int id;

    @Column(name = "date",nullable = false,insertable = false,updatable = false,
            columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;

    @Column(name = "code")
    private String code;

    @Column(name = "value")
    private int value;

    @Column(name = "used")
    private Boolean isUsed = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getCode(){ return code; }

    public void setCode(String code){ this.code = code; }

    public int getValue(){ return value; }

    public void setValue(int value){ this.value = value; }

    public Boolean getIsUsed(){ return isUsed; }

    public void setIsUsed(Boolean isUsed){ this.isUsed = isUsed; }
}
