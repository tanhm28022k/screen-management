package com.example.screenmanagement.repository.impl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseResultSelect<T> {
    private Long count;
    private List<T> listData;
}