package ru.practicum.explorewithme.dto.statistic;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ViewStats {
    private String app;
    private String uri;
    private Integer hits;
}
