package uit.core.util;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import uit.core.dto.response.CommentItem;
import uit.core.entity.Comment;
import uit.core.entity.User;
import uit.core.feign.AuthServerFeign;

public class SocialUtil {
    @Autowired
    private AuthServerFeign authServerFeign;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private static final ModelMapper modelMapper = new ModelMapper();

    public static String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return currentUserName;
        }
        throw new UsernameNotFoundException("User is not login yet or wrong username");
    }

}
