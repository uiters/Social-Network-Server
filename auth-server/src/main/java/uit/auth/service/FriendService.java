package uit.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uit.auth.entity.Friend;
import uit.auth.repository.FriendRepository;

import java.util.List;

@Service
public class FriendService {
    @Autowired
    private FriendRepository friendRepository;

    public List<Friend> getAll() {
        return friendRepository.findAll();
    }

    public Friend getById(Long id) {
        return friendRepository.findById(id).get();
    }

    public Friend create(Friend friend) {
        return friendRepository.save(friend);
    }

    public Friend update(Friend friend, Long id) {
        friend.setId(id);
        return friendRepository.save(friend);
    }

    public void deleteById(Long id) {
        friendRepository.deleteById(id);
    }
}
