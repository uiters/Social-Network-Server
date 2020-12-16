package uit.core.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uit.core.dto.response.ActionResponse;
import uit.core.dto.response.ReportResponse;
import uit.core.dto.response.UserLevelResponse;
import uit.core.entity.Report;
import uit.core.entity.User;
import uit.core.entity.event.Action;
import uit.core.entity.event.Level;
import uit.core.entity.event.UserLevel;
import uit.core.feign.AuthServerFeign;
import uit.core.repository.PostRepository;
import uit.core.repository.ReportRepository;
import uit.core.repository.event.ActionRepository;
import uit.core.repository.event.LevelRepository;
import uit.core.repository.event.UserLevelRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    @Autowired
    private AuthServerFeign authServerFeign;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private LevelRepository levelRepository;

    @Autowired
    private ActionRepository actionRepository;

    @Autowired
    private UserLevelRepository userLevelRepository;

    @Autowired
    private PostRepository postRepository;

    private static final ModelMapper modelMapper = new ModelMapper();

    public User disableUser(long userId) {
        return authServerFeign.disableUser(userId);
    }

    public User enableUser(long userId) {
        return authServerFeign.enableUser(userId);
    }

    public ReportResponse getReports(int page, int limit) {
        ReportResponse reportResponse = new ReportResponse();
        Pageable paging = PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Report> reportPage = reportRepository.findAll(paging);

        reportResponse.setItems(reportPage.getContent());

        if (page < reportPage.getTotalPages()-1) {
            reportResponse.setHasNext(true);
        } else {
            reportResponse.setHasNext(false);
        }
        String nextLink = "/admin/reports?&page=".concat(String.valueOf(page+1));
        reportResponse.setNextLink(nextLink);
        return reportResponse;
    }

    public List<Level> getLevels()  {
        return levelRepository.findAll();
    }

    public String deleteLevel(Long levelId) {
        levelRepository.deleteById(levelId);
        return "Delete level successfully";
    }

    public Level updateLevel(Long levelId, Level level) throws Exception {
        level.setId(levelId);

        List<Level> levels = levelRepository.findAll();
        for (Level levelItem : levels) {
            if (levelItem.getId() == level.getId()) {
                continue;
            }
            if (levelItem.getId() == level.getId()) {
                throw new Exception("Level ID was already used");
            }
            if (levelItem.getActivePoint() == level.getActivePoint()) {
                throw new Exception("Level Point was already used");
            }
            if (levelItem.getName() == level.getName()) {
                throw new Exception("Level Name was already used");
            }
        }

        return levelRepository.save(level);
    }

    public Level createLevel(Level level) throws Exception {
        if (level.getActivePoint() <= 0) {
            throw new Exception("point must be > 0");
        }

        List<Level> levels = levelRepository.findAll();
        for (Level levelItem : levels) {
            if (levelItem.getId() == level.getId()) {
                continue;
            }
            if (levelItem.getId() == level.getId()) {
                throw new Exception("Level number was already used");
            }
            if (levelItem.getActivePoint() == level.getActivePoint()) {
                throw new Exception("Level Point was already used");
            }
            if (levelItem.getName() == level.getName()) {
                throw new Exception("Level Name was already used");
            }
        }
        return levelRepository.save(level);
    }


    //ACTION
    public List<ActionResponse> getActions()  {
        List<Action> actions = actionRepository.findAll();

        List<ActionResponse> responses = new ArrayList<>();
        for (Action action : actions) {
            ActionResponse actionResponse = new ActionResponse();
            actionResponse.setId(action.getId());
            actionResponse.setActionName(uit.core.event.Action.getAction(action.getPoint()).getName());
            actionResponse.setPoint(action.getPoint());
            responses.add(actionResponse);
        }
        return responses;
    }

    public String deleteAction(Long actionId) {
        actionRepository.deleteById(actionId);
        return "Delete action successfully";
    }

    public Action updateAction(Long actionId, Long actionPoint) throws Exception {
        Optional<Action> actionOptional = actionRepository.findById(actionId);
        if (actionOptional.isEmpty()) {
            throw new Exception("ActionId not found");
        }
        List<Action> actions = actionRepository.findAll();
        for (Action actionItem : actions) {
            if (actionItem.getPoint() == actionPoint) {
                throw new Exception("Action point was already used");
            }
        }

        Action action = actionOptional.get();
        action.setPoint(actionPoint);
        return actionRepository.save(action);
    }

    public List<UserLevelResponse> getUserLevels() {
        List<UserLevel> userLevels = userLevelRepository.findAll();

        List<UserLevelResponse> userLevelResponses = new ArrayList<>();
        for (UserLevel userLevel : userLevels) {
            UserLevelResponse userLevelResponse = modelMapper.map(userLevel, UserLevelResponse.class);

            String username = authServerFeign.getById(userLevelResponse.getUserId()).getUsername();
            userLevelResponse.setDisplayName(username);

            //TODO refactor LEVEL NUMBER
            String levelName = levelRepository.findById(userLevelResponse.getLevelId()).get().getName();
            userLevelResponse.setLevelName(levelName);

            String postTitle = postRepository.findById(userLevel.getPostId()).get().getTitle();
            userLevelResponse.setPostTitle(postTitle);

            userLevelResponses.add(userLevelResponse);
        }
        return userLevelResponses;
    }


}
