package ucl.group.talentManageSystem.api.controller.form;

public class EmailRequestForm {
    private String email;
    private String lang;

    private int systemCode;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public int getSystemCode() {
        return systemCode;
    }
}
