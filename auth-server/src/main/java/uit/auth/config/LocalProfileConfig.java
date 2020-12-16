package uit.auth.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import uit.auth.entity.Role;
import uit.auth.entity.User;
import uit.auth.repository.RoleRepository;
import uit.auth.repository.UserRepository;
import uit.auth.service.UserService;

import java.util.*;

@Configuration
@ConditionalOnProperty(prefix = "social", name = "includeLocalDatabase", matchIfMissing = false)
public class LocalProfileConfig implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalProfileConfig.class);

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... arg0) throws Exception {
        LOGGER.info("Init role");
        buildRoleAdmin();
        buildRoleUser();

        LOGGER.info("Init users");
        User user1 = buildUser1();
        User user2 = buildUser2();

        if (!isCreated(user1)) {
            userService.create(user1);
        }

        if (!isCreated(user2)) {
            userService.create(user2);
        }
    }

    private void buildRoleAdmin() {
        Role role = new Role();
        role.setName("ADMIN");
        Optional<Role> roleOptional = roleRepository.findByName(role.getName());
        if (roleOptional.isPresent()) return;
        roleRepository.save(role);
    }

    private void buildRoleUser() {
        Role role = new Role();
        role.setName("USER");
        Optional<Role> roleOptional = roleRepository.findByName(role.getName());
        if (roleOptional.isPresent()) return;
        roleRepository.save(role);
    }

    private boolean isCreated(User user) {
        Optional<User> userOptional = userRepository.findByEmail(user.getEmail());
        return userOptional.isPresent();
    }

    private User buildUser1() {
        User user = new User();
        user.setId((long) 1);
        user.setUsername("Huỳnh Tấn Duy");
        user.setEmail("tanduyht@gmail.com");
        user.setGender(1);
        user.setStatus(1);
        user.setAvatar("https://uit-thesis-media-service.s3-ap-southeast-1.amazonaws.com/avatar.jpg");
        user.setBirthday(new GregorianCalendar(1998, Calendar.APRIL, 7).getTime());
        user.setPassword("abc123");

        Role role = roleRepository.findByName("ADMIN").get();
        Set<Role> roles = new HashSet();
        roles.add(role);
        user.setRoles(roles);

        return user;
    }

    private User buildUser2() {
        User user = new User();
        user.setId((long) 2);
        user.setUsername("Huỳnh Phương Duy");
        user.setEmail("kudophuongduy@gmail.com");
        user.setGender(1);
        user.setStatus(1);
        user.setPassword("abc123");
        user.setAvatar("https://uit-thesis-media-service.s3-ap-southeast-1.amazonaws.com/duybe.jpg");
        user.setBirthday(new GregorianCalendar(1998, Calendar.APRIL, 7).getTime());

        Role role = roleRepository.findByName("USER").get();
        Set<Role> roles = new HashSet();
        roles.add(role);
        user.setRoles(roles);

        return user;
    }
}
