package eredua.bean;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Locale;
import java.util.Map;
import java.util.LinkedHashMap;
import jakarta.inject.Named;

@Named("languageBean")
@SessionScoped
public class LanguageBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String selectedLanguage;
    private Map<String, Object> languages;
    
    @PostConstruct
    public void init() {
        languages = new LinkedHashMap<>();
        languages.put("Español", new Locale("es"));
        languages.put("English", new Locale("en"));
        languages.put("Euskera", new Locale("eu"));

        // MEJORAR: Forzar euskera como idioma por defecto
        selectedLanguage = "eu";
        
        // Establecer el locale inmediatamente
        forceLocale();
    }
    
    private void forceLocale() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context != null && context.getViewRoot() != null) {
            Locale locale = new Locale(selectedLanguage);
            context.getViewRoot().setLocale(locale);
            System.out.println("Locale forzado a: " + locale);
        }
    }
    
    public void changeLanguage() {
        Locale locale = new Locale(selectedLanguage);
        FacesContext context = FacesContext.getCurrentInstance();
        if (context != null && context.getViewRoot() != null) {
            context.getViewRoot().setLocale(locale);
            System.out.println("Idioma cambiado a: " + locale);
        }
    }
    
    // Getters y Setters
    public String getSelectedLanguage() {
        return selectedLanguage;
    }
    
    public void setSelectedLanguage(String selectedLanguage) {
        this.selectedLanguage = selectedLanguage;
        changeLanguage();
    }
    
    public Map<String, Object> getLanguages() {
        return languages;
    }
    
    public void setLanguages(Map<String, Object> languages) {
        this.languages = languages;
    }
    
    public String getCurrentLocale() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context != null && context.getViewRoot() != null) {
            return context.getViewRoot().getLocale().toString();
        }
        return "unknown";
    }
    
    public void initializeLocaleForView() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context != null && context.getViewRoot() != null) {
            // Forzar a euskera solo en la primera carga
            context.getViewRoot().setLocale(new Locale("eu"));
            System.out.println("Locale inicial establecido a euskera (postAddToView)");
        }
    }
}