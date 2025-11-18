package nl.fontys.tweetletimelineservice.business.service;

import nl.fontys.tweetletimelineservice.business.dto.TweetCreatedEvent;
import nl.fontys.tweetletimelineservice.persistence.document.TweetMetadata;
import nl.fontys.tweetletimelineservice.persistence.repository.TweetMetadataRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TweetMetadataService {

    private final TweetMetadataRepository repository;

    public TweetMetadataService(TweetMetadataRepository repository) {
        this.repository = repository;
    }

    public TweetMetadata saveFromEvent(TweetCreatedEvent event) {
        TweetMetadata metadata = TweetMetadata.builder()
                .tweetId(event.getId())
                .authorId(event.getAuthorId())
                .content(event.getContent())
                .createdAt(event.getCreatedAt())
                .build();
        return repository.save(metadata);
    }

    public void deleteById(String tweetId) {
        repository.deleteById(tweetId);
    }

    public Optional<TweetMetadata> findById(String tweetId) {
        return repository.findById(tweetId);
    }

    public List<TweetMetadata> findRecentByAuthor(Long authorId, int limit) {
        List<TweetMetadata> tweets = repository.findByAuthorIdOrderByCreatedAtDesc(authorId);
        if (tweets.size() <= limit) {
            return tweets;
        }
        return tweets.subList(0, limit);
    }
}
