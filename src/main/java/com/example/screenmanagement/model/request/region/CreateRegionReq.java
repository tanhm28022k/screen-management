package com.example.screenmanagement.model.request.region;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRegionReq {
    @NotBlank(message = "Region name can not blank")
    private String name;
    private String description;
    private String parentId;
}
