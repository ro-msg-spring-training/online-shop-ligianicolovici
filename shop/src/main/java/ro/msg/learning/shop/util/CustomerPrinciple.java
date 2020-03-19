package ro.msg.learning.shop.util;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ro.msg.learning.shop.entities.Customer;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class CustomerPrinciple implements UserDetails {
    private transient Customer customer= null;

    public CustomerPrinciple(Optional<Customer> optionalCustomer){
        PasswordEncoder passwordEncoder= new BCryptPasswordEncoder();
        if(optionalCustomer.isPresent()){
            this.customer= optionalCustomer.get();
            this.customer.setPassword(passwordEncoder.encode(this.customer.getPassword()));
        }else{
            this.customer= null;
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public String getPassword() {
        return customer.getPassword();
    }

    @Override
    public String getUsername() {
        return customer.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
