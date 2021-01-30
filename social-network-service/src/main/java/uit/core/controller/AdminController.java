package uit.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uit.core.dto.response.ActionResponse;
import uit.core.dto.response.ReportResponse;
import uit.core.dto.response.UserLevelItem;
import uit.core.dto.response.UserLevelResponse;
import uit.core.entity.Report;
import uit.core.entity.Role;
import uit.core.entity.User;
import uit.core.entity.event.Action;
import uit.core.entity.event.Level;
import uit.core.feign.AuthServerFeign;
import uit.core.service.AdminService;
import uit.core.util.SocialUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private AuthServerFeign authServerFeign;

    @PostMapping("/user/disable/{userId}")
    private User disableUser(@PathVariable long userId) throws Exception {
        if (!isAdmin()) {
            throw new Exception("Only Admin can perform this action");
        }
        return adminService.disableUser(userId);
    }

    @PostMapping("/user/enable/{userId}")
    private User enableUser(@PathVariable long userId) throws Exception {
        if (!isAdmin()) {
            throw new Exception("Only Admin can perform this action");
        }
        return adminService.enableUser(userId);
    }

    @GetMapping("/report")
    private ReportResponse getReports(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int limit) throws Exception {
        if (!isAdmin()) {
            throw new Exception("Only Admin can perform this action");
        }
        return adminService.getReports(page, limit);
    }

    @PutMapping("/report/{id}")
    private Report updateReport(@PathVariable Long id, @RequestBody Report report) throws Exception {
        if (!isAdmin()) {
            throw new Exception("Only Admin can perform this action");
        }
        return adminService.updateReport(id, report);
    }

    @DeleteMapping("/report/{id}")
    private String deleteReport(@PathVariable Long id) throws Exception {
        if (!isAdmin()) {
            throw new Exception("Only Admin can perform this action");
        }
        return adminService.deleteReport(id);
    }



    //API for LEVEL

    @GetMapping("/level")
    private List<Level> getLevels() throws Exception {
        if (!isAdmin()) {
            throw new Exception("Only Admin can perform this action");
        }
        return adminService.getLevels();
    }

    @DeleteMapping("/level/{levelId}")
    private String deleteLevel(@PathVariable Long levelId) throws Exception {
        if (!isAdmin()) {
            throw new Exception("Only Admin can perform this action");
        }
        return adminService.deleteLevel(levelId);
    }

    @PostMapping("/level")
    private Level createLevel(@RequestBody Level level) throws  Exception {
        if (!isAdmin()) {
            throw new Exception("Only Admin can perform this action");
        }
        return adminService.createLevel(level);
    }

    @PutMapping("/level/{levelId}")
    private Level updateLevel(@PathVariable Long levelId, @RequestBody Level level) throws  Exception {
        if (!isAdmin()) {
            throw new Exception("Only Admin can perform this action");
        }
        return adminService.updateLevel(levelId, level);
    }

    //API for ACTION

    @GetMapping("/action")
    private List<ActionResponse> getActions() throws Exception {
        if (!isAdmin()) {
            throw new Exception("Only Admin can perform this action");
        }
        return adminService.getActions();
    }

    @DeleteMapping("/action/{actionId}")
    private String deleteAction(@PathVariable Long actionId) throws Exception {
        if (!isAdmin()) {
            throw new Exception("Only Admin can perform this action");
        }
        return adminService.deleteAction(actionId);
    }

    @PutMapping("/action/{actionId}")
    private Action updateAction(@PathVariable Long actionId, @RequestBody Action action) throws  Exception {
        if (!isAdmin()) {
            throw new Exception("Only Admin can perform this action");
        }
        return adminService.updateAction(actionId, action.getPoint());
    }


    //USER LEVEL
    @GetMapping("/userLevel/{postId}")
    private UserLevelResponse getUserLevelsOfThePost(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int limit, @PathVariable long postId) throws Exception {
        if (!isAdmin()) {
            throw new Exception("Only Admin can perform this action");
        }
        return adminService.getUserLevelsOfThePost(page, limit, postId);
    }

    @PostMapping("/create")
    private User createAdmin(@RequestBody User user) throws Exception {
        if (!isAdmin()) {
            throw new Exception("Only Admin can perform this action");
        }
        return adminService.createAdmin(user);
    }

    private boolean isAdmin() {
        User user = authServerFeign.getByUserName(SocialUtil.getCurrentUserEmail());
        Set<Role> roles = user.getRoles();
        List<String> authorities = new ArrayList<>();

        for (Role role : roles) {
            authorities.add(role.getName());
        }

        return authorities.contains("ADMIN");
    }


}
