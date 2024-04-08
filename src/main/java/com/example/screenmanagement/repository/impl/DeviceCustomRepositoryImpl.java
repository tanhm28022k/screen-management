package com.example.screenmanagement.repository.impl;

import com.example.screenmanagement.model.response.device.DeviceDtoResponse;
import com.example.screenmanagement.repository.custom.DeviceCustomRepository;
import com.example.screenmanagement.utility.UtilsValidate;
import com.google.api.client.googleapis.util.Utils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Locale;

@Repository
public class DeviceCustomRepositoryImpl extends BaseRepositoryImpl implements DeviceCustomRepository {

    @Override
    public BaseResultSelect searchData(String name, Integer page, Integer size, String userId, String regionId) {
        StringBuilder sql = new StringBuilder();
        sql.append("""
                select t.id,
                                 t.available,
                                 t.description,
                                 t.identity_device,
                                 t.ins_datetime,
                                 t.location,
                                 t.name,
                                 t.status,
                                 t.time_off_ago,
                                 t.upd_datetime,
                                 t.user_id,
                                 t.model,
                                 t.is_presenting,
                                 t.is_presenting_schedule,
                                 t.last_active_time,
                                 t.last_disconnected_time,
                                 t.serial_number,
                                 t.manufacturer,
                                 t.app_version,
                                 t.os_version
                          from device t
                          join region_device rd on t.id = rd.device_id
                          join region r on rd.region_id = r.id
                          where 1=1 and t.is_deleted <> 1
                """);
        HashMap<String, Object> params = new HashMap<>();
        if (userId != null) {
            sql.append(" AND t.user_id = :userId");
            params.put("userId", userId);
        }
        if (!UtilsValidate.isNullOrEmpty(name)) {
            sql.append(" AND UPPER(t.name) LIKE :name");
            params.put("name", "%/" + name.toUpperCase(Locale.ROOT) + "/%");
        }
        if (regionId != null) {
            sql.append(" AND r.id = :regionId");
            params.put("regionId", regionId);
        }
        return getListPagination(sql.toString(), params, page, size, DeviceDtoResponse.class);
    }
}
