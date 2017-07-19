package testBlog.bindingModel;

import javax.validation.constraints.NotNull;

public class ResetPasswordBindingModel {

    @NotNull
    private String userEmail;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
