//package uit.core.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import uit.core.entity.GroupInvitation;
//import uit.core.service.GroupInvitationService;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/groupInvitation")
//public class GroupInvitationController {
//    @Autowired
//    private GroupInvitationService groupInvitationService;
//
//    @GetMapping
//    public List<GroupInvitation> getAll() {
//        return groupInvitationService.getAll();
//    }
//
//    @GetMapping("/{id}")
//    public GroupInvitation getById(@PathVariable Long id) {
//        return groupInvitationService.getById(id);
//    }
//
//    @PostMapping
//    public GroupInvitation create(@RequestBody GroupInvitation groupInvitation) {
//        return groupInvitationService.create(groupInvitation);
//    }
//
//    @PutMapping("/{id}")
//    public GroupInvitation update(@RequestBody GroupInvitation groupInvitation, @PathVariable Long id) {
//        return groupInvitationService.update(groupInvitation, id);
//    }
//
//    @DeleteMapping("/{id}")
//    public void deleteById(@PathVariable Long id) {
//        groupInvitationService.deleteById(id);
//    }
//}
