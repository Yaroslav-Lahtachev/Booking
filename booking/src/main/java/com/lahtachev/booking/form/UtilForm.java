package com.lahtachev.booking.form;

public class UtilForm {
    private String textField;
    private int intField;

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
}
