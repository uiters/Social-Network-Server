package uit.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uit.core.entity.Share;
import uit.core.repository.ShareRepository;

import java.util.List;

@Service
public class ShareService {
    @Autowired
    private ShareRepository shareRepository;

    public List<Share> getAll() {
        return shareRepository.findAll();
    }

    public Share getById(Long id) {
        return shareRepository.findById(id).get();
    }

    public Share create(Share share) {
        return shareRepository.save(share);
    }

    public Share update(Share share, Long id) {
        share.setId(id);
        return shareRepository.save(share);
    }

    public void deleteById(Long id) {
        shareRepository.deleteById(id);
    }
}
