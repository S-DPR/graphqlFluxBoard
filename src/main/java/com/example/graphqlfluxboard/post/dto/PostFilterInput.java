package com.example.graphqlfluxboard.post.dto;

import com.example.graphqlfluxboard.common.validation.DTOValidationMessage;
import com.example.graphqlfluxboard.post.enums.FilterType;
import com.example.graphqlfluxboard.post.enums.SortOrder;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PostFilterInput {
    @NotNull(message = DTOValidationMessage.NOT_NULL_FILTER_TYPE)
    private FilterType type = FilterType.NONE;

    @NotNull(message = DTOValidationMessage.NOT_NULL_KEYWORD)
    private String keyword = "";

    @Min(value = 1, message = DTOValidationMessage.PAGE_SIZE_LIMIT)
    private Integer page = 1; // start with 1

    @Min(value = 1, message = DTOValidationMessage.POST_SIZE_PER_PAGE_LIMIT)
    private Integer sizePerPage = 5;

    @NotNull(message = DTOValidationMessage.POST_SORT_FIELD_ERROR)
    private String sortField = "createdAt";

    @NotNull(message = DTOValidationMessage.SORT_ORDER_ERROR)
    private SortOrder sortOrder = SortOrder.DESC;
}
