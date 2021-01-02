//package uit.core.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import uit.core.entity.Group;
//import uit.core.service.GroupService;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/group")
//public class GroupController {
//    @Autowired
//    private GroupService groupService;
//
//    @GetMapping
//    public List<Group> getAll() {
//        return groupService.getAll();
//    }
//
//    @GetMapping("/{id}")
//    public Group getById(@PathVariable Long id) {
//        return groupService.getById(id);
//    }
//
//    @PostMapping
//    public Group create(@RequestBody Group group) {
//        return groupService.create(group);
//    }
//
//    @PutMapping("/{id}")
//    public Group update(@RequestBody Group group, @PathVariable Long id) {
//        return groupService.update(group, id);
//    }
//
//    @DeleteMapping("/{id}")
//    public void deleteById(@PathVariable Long id) {
//        groupService.deleteById(id);
//    }
//}
