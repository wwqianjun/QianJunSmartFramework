package com.qianjun.model;

/**
 * Created by ZiJun
 * Description:客户
 * Date: 2015/11/18 :14:20.
 */
public class TCustomer {
    private long id;

    // 客户名称
    private String name;

    // 联系人
    private String contact;

    // 手机号
    private String telephone;

    // 邮件
    private String email;

    // 备注
    private String remark;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "TCustomer{" +
                "name='" + name + '\'' +
                ", contact='" + contact + '\'' +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
