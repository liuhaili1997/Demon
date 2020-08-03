package com.enums;


/**
 * 功能描述: 第一张表的支付方式
 * 1.刷卡/余额扣款 2.人脸 3.微信 4.支付宝 5.农行掌银二维码 6.农行快e付 7.取餐码'
 *
 * @Author: liuhaili
 * @Date: 2020-07-15, 周三, 20:28
 */
public enum RechargeOperatorEnum {

    Handheld_Banks(1,"掌银"),
    MANAGER(2, "管理员")
    ;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private Integer code;
    private String message;

    RechargeOperatorEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getValueOfCode(Integer number) {
        for (RechargeOperatorEnum value : RechargeOperatorEnum.values()) {
            if (value.getCode().equals(number)) {
                return value.getMessage();
            }
        }
        return "null";
    }

}
