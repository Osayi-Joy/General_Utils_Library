package com.osayijoy.common_utils_library.registhentication.common.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PaginatedResponseDTO<T> {
    private List<T> content = new ArrayList<>();
    private int currentPage;
    private long totalPages;
    private long totalItems;
    private Boolean isFirstPage;
    private Boolean isLastPage;
}
