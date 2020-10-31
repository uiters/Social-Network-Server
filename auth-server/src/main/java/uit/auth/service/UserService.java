package uit.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import uit.auth.entity.User;
import uit.auth.repository.UserRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(Long id) {
        return userRepository.findById(id).get();
    }

    public User create(User user) {
        user.setAvatar("https://uit-thesis-media-service.s3-ap-southeast-1.amazonaws.com/fb_avatar.png");
        user.setRole(1);
        user.setStatus(1);
        user.setGender(1);
        Date date = new GregorianCalendar(2000, Calendar.APRIL, 11).getTime();
        user.setBirthday(date);
        user.setPassword(encoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public User update(User user, Long id) {
        user.setId(id);
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return user;
        }
        else throw new UsernameNotFoundException("Incorrect username/password");
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
