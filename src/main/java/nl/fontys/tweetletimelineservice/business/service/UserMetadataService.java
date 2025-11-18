package nl.fontys.tweetletimelineservice.business.service;

import nl.fontys.tweetletimelineservice.domain.UserEvent;
import nl.fontys.tweetletimelineservice.persistence.document.UserMetadata;
import nl.fontys.tweetletimelineservice.persistence.repository.UserMetadataRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class UserMetadataService {

    private final UserMetadataRepository repository;

    public UserMetadataService(UserMetadataRepository repository) {
        this.repository = repository;
    }

    public UserMetadata saveOrUpdate(UserEvent event) {
        UserMetadata metadata = repository.findById(event.getUserId())
                .orElse(UserMetadata.builder().userId(event.getUserId()).build());

        metadata.setUsername(event.getUsername());
        metadata.setAvatarUrl(event.getAvatarUrl());
        metadata.setUpdatedAt(Instant.now().toEpochMilli());

        return repository.save(metadata);
    }

    public void deleteUser(Long userId) {
        if (repository.existsById(userId)) {
            repository.deleteById(userId);
        }
    }

    public Optional<UserMetadata> findById(Long userId) {
        return repository.findById(userId);
    }
}
