package gr.codingschool.iwg.repository;

import gr.codingschool.iwg.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon,Long>{
    Coupon findByCode(String code);
    Coupon save(Coupon coupon);
}
