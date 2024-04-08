package com.example.screenmanagement.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
@Slf4j
public class BaseRepositoryImpl {
    @Autowired
    protected NamedParameterJdbcTemplate jdbcTemplate;
    public Boolean executeSqlDatabase(String queryString, HashMap<String, Object> hmapParams) {
        boolean result = true;
        try {
            jdbcTemplate.update(queryString, hmapParams);
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            result = false;
        }
        return result;
    }
    public <T> T getFirstData(String queryString, Map<String, Object> mapParam, Class<T> className) {
        try {
            return (T) jdbcTemplate.queryForObject(queryString, mapParam, new BeanPropertyRowMapper(className));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    public <T> T queryForObject(String queryString, Map<String, Object> mapParam, Class<T> className) {
        try {
            if (className.isAssignableFrom(Long.class) || className.isAssignableFrom(Integer.class) || className.isAssignableFrom(Double.class) || className.isAssignableFrom(String.class)) {
                return (T) jdbcTemplate.queryForObject(queryString, mapParam, className);
            } else {
                return (T) jdbcTemplate.queryForObject(queryString, mapParam, new BeanPropertyRowMapper(className));
            }
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    public <T> List<T> getListData(String queryString, Map<String, Object> mapParam, Class<T> className) {
        try {
            if (className.isAssignableFrom(Long.class) || className.isAssignableFrom(Integer.class) || className.isAssignableFrom(Double.class) || className.isAssignableFrom(String.class)) {
                return jdbcTemplate.queryForList(queryString, mapParam, className);
            } else {
                return jdbcTemplate.query(queryString, mapParam, new BeanPropertyRowMapper(className));
            }
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    public <T> BaseResultSelect<T> getListPagination(String queryString, Map<String, Object> mapParams, Integer startPage, Integer pageSize, Class<T> className) {
        try {
            startPage = startPage == null || startPage < 0 ? 0 : startPage;
            pageSize = pageSize == null || pageSize < 0 ? 10 : pageSize;
            StringBuilder sqlPage = new StringBuilder();
            sqlPage.append(" SELECT * FROM ( ");
            sqlPage.append(queryString);
            sqlPage.append(" ) a ");
            sqlPage.append(String.format(" LIMIT %d, %d ", startPage, pageSize));
            String sqlCount = "SELECT COUNT(*) FROM (" + queryString + ") as subquery_alias ";
            Long records = jdbcTemplate.queryForObject(sqlCount, mapParams, Long.class);
            List<T> resultQuery = getListData(sqlPage.toString(), mapParams, className);
            BaseResultSelect<T> result = new BaseResultSelect<>();
            if (resultQuery != null) {
                result.setListData(resultQuery);
            }
            result.setCount(records);
            return result;
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}