package com.example.screenmanagement.model.response.region;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegionDto {
    private String id;
    private String name;
    private String parentId;
    private List<RegionDto> subs;
}
