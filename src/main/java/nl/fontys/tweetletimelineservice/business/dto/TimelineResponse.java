package nl.fontys.tweetletimelineservice.business.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimelineResponse {
    private Long userId;
    private List<TimelineTweetDto> tweets;
}
