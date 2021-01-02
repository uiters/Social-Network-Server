//package uit.core.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import uit.core.entity.GroupInvitation;
//import uit.core.repository.GroupInvitationRepository;
//
//import java.util.List;
//
//@Service
//public class GroupInvitationService {
//    @Autowired
//    private GroupInvitationRepository groupInvitationRepository;
//
//    public List<GroupInvitation> getAll() {
//        return groupInvitationRepository.findAll();
//    }
//
//    public GroupInvitation getById(Long id) {
//        return groupInvitationRepository.findById(id).get();
//    }
//
//    public GroupInvitation create(GroupInvitation groupInvitation) {
//        return groupInvitationRepository.save(groupInvitation);
//    }
//
//    public GroupInvitation update(GroupInvitation groupInvitation, Long id) {
//        groupInvitation.setId(id);
//        return groupInvitationRepository.save(groupInvitation);
//    }
//
//    public void deleteById(Long id) {
//        groupInvitationRepository.deleteById(id);
//    }
//}
