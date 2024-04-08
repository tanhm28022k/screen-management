package com.example.screenmanagement.model.request.document;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SearchDocumentReq {
    @NotNull
    private int page = 0;
    @NotNull
    private int size = 20;
    private String docName;
    private String docType;
    private String docSize;
    private Long uploadedTime;
}
