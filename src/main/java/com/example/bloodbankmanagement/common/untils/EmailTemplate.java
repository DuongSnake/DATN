package com.example.bloodbankmanagement.common.untils;

public enum EmailTemplate {
    FORGOT_PASSWORD("", "forgot_password_template", "ĐỀ NGHỊ THAY ĐỔI MẬT KHẨU"),
    RESET_PASSWORD("", "reset_password", "ĐỀ NGHỊ THIẾT LẬP LẠI MẬT KHẨU"),
    REG_NEW("", "reg_new", "ĐĂNG KÝ NGƯỜI DÙNG MỚI");

    private final String id;
    private final String name;
    private final String subject;

    private EmailTemplate(String id, String name, String subject) {
        this.id = id;
        this.name = name;
        this.subject = subject;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getSubject() {
        return this.subject;
    }
}
