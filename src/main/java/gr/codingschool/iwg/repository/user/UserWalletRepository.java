package gr.codingschool.iwg.repository.user;

import gr.codingschool.iwg.model.user.UserWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserWalletRepository extends JpaRepository<UserWallet,Long>{
    UserWallet save(UserWallet wallet);
}
