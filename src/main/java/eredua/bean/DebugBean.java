package eredua.bean;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.util.Locale;
import java.util.ResourceBundle;

@Named
@RequestScoped
public class DebugBean {
    
    public void checkLocale() {
        FacesContext context = FacesContext.getCurrentInstance();
        Locale locale = context.getViewRoot().getLocale();
        System.out.println("=== DEBUG ===");
        System.out.println("Locale actual: " + locale);
        
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
            System.out.println("Bundle encontrado: " + bundle.getBaseBundleName());
            System.out.println("sarrera.title: " + bundle.getString("sarrera.title"));
        } catch (Exception e) {
            System.out.println("Error cargando bundle: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("=============");
    }
}