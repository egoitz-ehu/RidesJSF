package eredua.bean;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Locale;
import java.util.Map;
import java.util.LinkedHashMap;
import jakarta.inject.Named;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Locale;
import jakarta.faces.context.FacesContext;

@Named("languageBean")
@SessionScoped
public class LanguageBean implements Serializable {
    private String selectedLanguage = "eu"; 

    public String getSelectedLanguage() {
        return selectedLanguage;
    }

    public void setSelectedLanguage(String selectedLanguage) {
        this.selectedLanguage = selectedLanguage;
    }

    public void initializeLocaleForView() {
        FacesContext.getCurrentInstance().getViewRoot()
            .setLocale(new Locale(selectedLanguage));
    }
    public void updateLocale() {
        FacesContext.getCurrentInstance().getViewRoot()
            .setLocale(new Locale(selectedLanguage));
    }
}
