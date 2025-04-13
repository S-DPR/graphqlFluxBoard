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
    private FilterType type = FilterType.NONE;
    private String keyword = "";
    private Integer page = 1; // start with 1
    private Integer sizePerPage = 5;
    private String sortField = "createAt";
    private SortOrder sortOrder = SortOrder.DESC;
}
