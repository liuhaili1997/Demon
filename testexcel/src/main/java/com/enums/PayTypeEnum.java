package com.enums;


/**
 * 功能描述: 第一张表的支付方式
 * 1.刷卡/余额扣款 2.人脸 3.微信 4.支付宝 5.农行掌银二维码 6.农行快e付 7.取餐码'
 *
 * @Author: liuhaili
 * @Date: 2020-07-15, 周三, 20:28
 */
public enum PayTypeEnum {

    BRUSH_CARD(1,"刷卡/余额扣款"),
    PERSON_FACE(2, "人脸"),
    WECHAT_PAY(3, "微信"),
    ALI_PAY(4, "支付宝"),
    AGRICULTURAL_BANK_QR_CODE(5,"农行掌银二维码"),
    AGRICULTURAL_BANK_QUICK_E_PAY(6, "农行快e付"),
    TAKE_FOOD_CODE(7,"取餐码")
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

    PayTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getValueOfCode(Integer number) {
        for (PayTypeEnum value : PayTypeEnum.values()) {
            if (value.getCode().equals(number)) {
                return value.getMessage();
            }
        }
        return "null";
    }

}
