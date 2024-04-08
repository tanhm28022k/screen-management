package com.example.screenmanagement.model.request.folder;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateFolderReq {
    @NotBlank(message = "Region name can not blank")
    private String name;
    private String description;
    private String parentId;
}
