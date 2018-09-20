/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.codingschool.iwg.service;

import gr.codingschool.iwg.model.Coupon;
import gr.codingschool.iwg.model.user.Role;
import gr.codingschool.iwg.model.user.User;
import gr.codingschool.iwg.model.user.UserWallet;
import gr.codingschool.iwg.repository.CouponRepository;
import gr.codingschool.iwg.repository.user.RoleRepository;
import gr.codingschool.iwg.repository.user.UserRepository;
import gr.codingschool.iwg.repository.user.UserWalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 *
 */
@Service("userService")
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserWalletRepository walletRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private UserWalletRepository userWalletRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByUsername(String username) { return userRepository.findByUsername(username); }

    public List<User> findAll(){ return userRepository.findAll(); }

    public UserWallet initWalletForUser(String username){
        UserWallet wallet = new UserWallet();
        wallet.setBalance(0);
        wallet = walletRepository.save(wallet);
        User user = userRepository.findByUsername(username);
        user.setWallet(wallet);
        userRepository.save(user);
        return wallet;
    }

    @Transactional
    public UserWallet getWalletForUser(String username) {
        User user = userRepository.findByUsername(username);
        return user.getWallet();
    }

    @Transactional
    public Boolean addBalance(String username, int addedBalance) {
        User user = userRepository.findByUsername(username);
        UserWallet wallet = user.getWallet();
        wallet.setBalance(wallet.getBalance() + addedBalance);
        walletRepository.save(wallet);
        return true;
    }

    @Transactional
    @SuppressWarnings("Duplicates")
    public Boolean withdrawBalance(String username, int withdrawnBalance) {
        User user = userRepository.findByUsername(username);
    UserWallet wallet = user.getWallet();
        if(wallet.getBalance() < withdrawnBalance)
            return false;
        wallet.setBalance(wallet.getBalance() - withdrawnBalance);
        walletRepository.save(wallet);
        return true;
    }

    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
        Role userRole = roleRepository.findByRole("ROLE_USER");
        user.setRoles(new HashSet<>(Collections.singletonList(userRole)));
        user.setFavouriteGames(new HashSet<>());
        user.setRatedGames(new HashSet<>());
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public User authenticate(String username, String password){
        User user =  userRepository.findByUsername(username);

        if(user == null){
            return null;
        }
        else {
            if(bCryptPasswordEncoder.matches(password, user.getPassword()))
                return user;
            else
                return null;
        }
    }

    public Coupon getCouponIfAvailable(String code) {
        Coupon coupon = couponRepository.findByCode(code);
        if(!(coupon == null) && !coupon.getIsUsed())
            return coupon;
        else
            return null;
    }

    @Transactional
    public Boolean useCoupon(String code) {
        Coupon coupon = couponRepository.findByCode(code);
        if(!coupon.getIsUsed()) {
            coupon.setIsUsed(true);
            couponRepository.save(coupon);
            return true;
        }
        else
            return false;
    }

    @Transactional
    public UserWallet saveWallet(UserWallet wallet) {
        return userWalletRepository.save(wallet);
    }
}
