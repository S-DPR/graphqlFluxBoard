package com.example.graphqlfluxboard.dto;

import com.example.graphqlfluxboard.enums.FilterType;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostFilterInput {
    private FilterType type;
    private String keyword;
    private Integer page = 1; // start with 1
    private Integer sizePerPage = 5;
}
