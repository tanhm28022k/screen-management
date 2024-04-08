package com.example.screenmanagement.repository.impl;

import com.example.screenmanagement.model.request.user.SearchUserReq;
import com.example.screenmanagement.model.response.device.DeviceDtoResponse;
import com.example.screenmanagement.repository.custom.UserRepositoryCustom;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class UserRepositoryCustomImpl extends BaseRepositoryImpl implements UserRepositoryCustom {
    @Override
    public BaseResultSelect searchUser(SearchUserReq req) {
        StringBuilder sql = new StringBuilder();
        sql.append("""
                select t.id,
                       birthday,
                       gender,
                       ins_datetime,
                       mail,
                       fullname,
                       password,
                       phone_numb,
                       roles,
                       upd_datetime,
                       username,
                       status
                from user t
                where 1=1
                """);
        HashMap<String, Object> params = new HashMap<>();
        if (!Strings.isEmpty(req.getUserName())) {
            sql.append(" AND t.username = :username");
            params.put("username", req.getUserName());
        }
        return getListPagination(sql.toString(), params, req.getPage(), req.getSize(), DeviceDtoResponse.class);
    }
}
