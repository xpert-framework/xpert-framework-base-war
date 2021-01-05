package com.base.mb.tema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 *
 * @author ayslanms
 */
@Named
@ApplicationScoped
public class ApplicationThemeMB implements Serializable {

    private List<Theme> themes;

    @PostConstruct
    public void init() {
        themes = new ArrayList<>();
        themes.add(new Theme("Nova-Light", "nova-light"));
        themes.add(new Theme("Nova-Dark", "nova-dark"));
        themes.add(new Theme("Nova-Colored", "nova-colored"));
        themes.add(new Theme("Luna-Blue", "luna-blue"));
        themes.add(new Theme("Luna-Amber", "luna-amber"));
        themes.add(new Theme("Luna-Green", "luna-green"));
        themes.add(new Theme("Luna-Pink", "luna-pink"));
        themes.add(new Theme("Omega", "omega"));
    }

    public Theme getByName(String name) {
        for (Theme theme : themes) {
            if (name.equals(theme.getName())) {
                return theme;
            }
        }
        return null;
    }

    public List<Theme> getThemes() {
        return themes;
    }
}
