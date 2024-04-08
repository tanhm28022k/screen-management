package com.example.screenmanagement.model.response.folder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FolderDto {
    private String id;
    private String name;
    private String parentId;
    private List<FolderDto> subs;
}
