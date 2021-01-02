//package uit.core.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import uit.core.entity.Share;
//import uit.core.service.ShareService;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/share")
//public class ShareController {
//    @Autowired
//    private ShareService shareService;
//
//    @GetMapping
//    public List<Share> getAll() {
//        return shareService.getAll();
//    }
//
//    @GetMapping("/{id}")
//    public Share getById(@PathVariable Long id) {
//        return shareService.getById(id);
//    }
//
//    @PostMapping
//    public Share create(@RequestBody Share share) {
//        return shareService.create(share);
//    }
//
//    @PutMapping("/{id}")
//    public Share update(@RequestBody Share share, @PathVariable Long id) {
//        return shareService.update(share, id);
//    }
//
//    @DeleteMapping("/{id}")
//    public void deleteById(@PathVariable Long id) {
//        shareService.deleteById(id);
//    }
//}
