package uit.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import uit.auth.entity.Role;
import uit.auth.entity.User;
import uit.auth.feign.MediaServiceFeign;
import uit.auth.repository.RoleRepository;
import uit.auth.repository.UserRepository;
import uit.auth.repository.specification.SearchCriteria;
import uit.auth.repository.specification.SearchOperation;
import uit.auth.repository.specification.UserSpecification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserService implements UserDetailsService {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MediaServiceFeign mediaServiceFeign;

    @Autowired
    private RoleRepository roleRepository;

    public List<User> getAll(String id, String username, String email) {
        UserSpecification userSpecification = new UserSpecification();
        if (!StringUtils.isEmpty(id)) {
            userSpecification.add(new SearchCriteria("id", id, SearchOperation.LIKE));
        }
        if (!StringUtils.isEmpty(username)) {
            userSpecification.add(new SearchCriteria("username", username, SearchOperation.LIKE));
        }
        if (!StringUtils.isEmpty(email)) {
            userSpecification.add(new SearchCriteria("email", email, SearchOperation.LIKE));
        }

        List<User> response = userRepository.findAll(userSpecification);
        return response;
    }

    public User getById(Long id) {
        return userRepository.findById(id).get();
    }

    public User create(User user) throws Exception {
        user.setAvatar("https://uit-thesis-media-service.s3-ap-southeast-1.amazonaws.com/fb_avatar.png");
        user.setStatus(1);
        user.setGender(1);
        Date date = new GregorianCalendar(2000, Calendar.APRIL, 11).getTime();
        user.setBirthday(date);
        user.setPassword(encoder.encode(user.getPassword()));
        checkIfUserExists(user.getUsername());
        return userRepository.save(user);
    }

    private void checkIfUserExists(String username) throws Exception {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) throw new RuntimeException("Username was already used");
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
        User user = userRepository.findByEmail(email).get();
        if (user != null) {
            return user;
        }
        else throw new UsernameNotFoundException("Incorrect username/password");
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username).get();
    }

    public User update(Long id, String username, String email, String password, String gender, String birthday, String status, String role, String hometown, String address, MultipartFile file) throws ParseException {
        User currentUser = userRepository.findById(id).get();
        if (!StringUtils.isEmpty(username)) {
            currentUser.setUsername(username);
        }
        if (!StringUtils.isEmpty(email)) {
            currentUser.setEmail(email);
        }
        if (!StringUtils.isEmpty(gender)) {
            currentUser.setGender(Long.valueOf(gender));
        }
        if (!StringUtils.isEmpty(birthday)) {
            Date formattedBirthday=new SimpleDateFormat("dd-MM-yyyy").parse(birthday);
            currentUser.setBirthday(formattedBirthday);
        }
        if (!StringUtils.isEmpty(status)) {
            currentUser.setStatus(Long.valueOf(status));
        }

        if (!StringUtils.isEmpty(hometown)) {
            currentUser.setHometown(hometown);
        }
        if (!StringUtils.isEmpty(address)) {
            currentUser.setAddress(address);
        }
        if (!StringUtils.isEmpty(password)) {
            currentUser.setPassword(encoder.encode(password));
        }
        if (file!=null) {
            String avatarUrl = mediaServiceFeign.uploadSingleFile(file);
            currentUser.setAvatar(avatarUrl);
        }
        return userRepository.save(currentUser);
    }

    public User disableUser(Long userId) throws Exception {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new Exception("UserId not found");
        }
        User user = userOptional.get();
        user.setStatus(0);
        return userRepository.save(user);
    }

    public User enableUser(Long userId) throws Exception {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new Exception("UserId not found");
        }
        User user = userOptional.get();
        user.setStatus(1);
        return userRepository.save(user);
    }

    public User createAdmin(User user) throws Exception {
        Role role = roleRepository.findByName("ADMIN").get();
        Set<Role> roles = new HashSet();
        roles.add(role);
        user.setRoles(roles);
        return this.create(user);
    }
}
