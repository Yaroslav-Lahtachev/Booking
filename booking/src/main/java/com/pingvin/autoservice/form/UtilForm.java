package com.pingvin.autoservice.form;

public class UtilForm {
    private String textField;
    private int intField;
    private boolean check;


    public UtilForm(String textField, int intField) {
        this.textField = textField;
        this.intField = intField;
    }

    public UtilForm() {
    }

    public int getIntField() {
        return intField;
    }

    public void setIntField(int intField) {
        this.intField = intField;
    }

    public String getTextField() {
        return textField;
    }

    public void setTextField(String textField) {
        this.textField = textField;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
