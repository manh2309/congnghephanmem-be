package org.example.techstore.utils;

public class Constant {
    public static final class IS_ACTIVE {
        public static final Long ACTIVE = 1L;
        public static final Long INACTIVE = 0L;
    }

    public static final class ERROR_MESSAGE {
            public static final String NOT_FOUND = "%s không tồn tại";
            public static final String EXISTS_FOUND = "%s đã tồn tại";
            public static final String USER_PASS_FOUND = "Sai username hoặc password!";
            public static final String TOKEN_REQUIRE_FOUND = "Token không được để trống";
            public static final String TOKEN_FOUND = "Token lỗi";
            public static final String TOKEN_NOT_VALID = "Token không hợp lệ";
            public static final String UPLOAD_FILE_FOUND = "Upload anh that bat";
    }

    public static final class MESSAGE {
        public static final String SEARCH_SUCCESS = "Tìm kiếm %s thành công";
        public static final String CREATE_SUCCESS = "Tạo %s thành công";
        public static final String UPDATE_SUCCESS = "Cập nhật %s thành công";
        public static final String DELETE_SUCCESS = "Xóa %s thành công";
        public static final String RESTORE_SUCCESS = "Khôi phục %s thành công";
        public static final String LOGIN_SUCCESS = "Đăng nhập thành công";
        public static final String REFRESH_TOKEN_SUCCESS = "Refresh token thành công";
        public static final String LOGOUT_SUCCESS = "Logout thành công";
    }

    public static final class MODULE {
        public static final String BRAND = "Nhãn hàng";
        public static final String ROLE = "Chức vụ";
        public static final String IMAGE = "Hình ảnh";
        public static final String ACCOUNT = "Người dùng";
        public static final String PRODUCT = "Sản phẩm";

    }
}
