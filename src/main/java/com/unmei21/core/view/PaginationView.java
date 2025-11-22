package com.unmei21.core.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaginationView {
    private Integer page;
    private Integer pageSize;
    private Boolean hasNext;
}
