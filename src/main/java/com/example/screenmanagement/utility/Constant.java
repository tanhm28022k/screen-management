package com.example.screenmanagement.utility;

public class Constant {
    public enum ERROR_CODE_MAP {
        SUCESS("200", "Thành công"),
        SAVE_DEVICE_ERROR("DV_001", "Lỗi lưu thông tin devive"),
        DETAIL_DEVICE_ERROR("DV_002", "Không tìm thấy thiết bị"),
        DELETE_DEVICE_ERROR("DV_003", "Xóa thiết bị lỗi "),
        MOVE_DEVICE_ERROR("DV_004", "Xóa thiết bị lỗi "),
        SEARCH_DEVICE_ERROR("DV_005", "Tìm kiếm thiết bị lỗi ");
        private final String code;
        private final String message;

        ERROR_CODE_MAP(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
    public enum STATUS {
        ACTIVE,
        DEACTIVE
    }


    public enum GENDER {
        MALE,
        FEMALE,
        OTHER
    }

}


