package validator;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIInput;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

@FacesValidator("passwordValidator")
public class PasswordValidator implements Validator<String> {

    @Override
    public void validate(FacesContext context, UIComponent component, String value) 
            throws ValidatorException {
        
        UIInput passwordComponent = (UIInput) component.findComponent("password");
        
        if (passwordComponent != null) {
            String password = (String) passwordComponent.getSubmittedValue();
            
            if (password == null) {
                password = (String) passwordComponent.getValue();
            }
            
            String confirmPassword = value;
            
            if (password != null && confirmPassword != null && 
                !password.isEmpty() && !password.equals(confirmPassword)) {
                FacesMessage msg = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Password mismatch",
                    "Passwords do not match"
                );
                throw new ValidatorException(msg);
            }
        }
    }
}
