package uit.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uit.core.entity.Group;
import uit.core.repository.GroupRepository;

import java.util.List;

@Service
public class GroupService {
    @Autowired
    private GroupRepository groupRepository;

    public List<Group> getAll() {
        return groupRepository.findAll();
    }

    public Group getById(Long id) {
        return groupRepository.findById(id).get();
    }

    public Group create(Group group) {
        return groupRepository.save(group);
    }

    public Group update(Group group, Long id) {
        group.setId(id);
        return groupRepository.save(group);
    }

    public void deleteById(Long id) {
        groupRepository.deleteById(id);
    }
}
