package com.example.graphqlfluxboard.post.dto;

import com.example.graphqlfluxboard.post.enums.FilterType;
import com.example.graphqlfluxboard.post.enums.SortOrder;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostFilterInput {
    @Builder.Default
    private FilterType type = FilterType.NONE;

    @Builder.Default
    private String keyword = "";

    @Builder.Default
    private Integer page = 1; // start with 1

    @Builder.Default
    private Integer sizePerPage = 5;

    @Builder.Default
    private String sortField = "createAt";

    @Builder.Default
    private SortOrder sortOrder = SortOrder.DESC;
}
