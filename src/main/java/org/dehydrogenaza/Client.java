package org.dehydrogenaza;

import org.dehydrogenaza.data.DataSource;
import org.dehydrogenaza.data.Form;
import org.teavm.flavour.templates.BindTemplate;
import org.teavm.flavour.templates.Templates;
import org.teavm.flavour.widgets.ApplicationTemplate;

import java.util.Set;

@BindTemplate("templates/client.html")
public class Client extends ApplicationTemplate {
    private final DataSource source = new DataSource();
    private final Form form = new Form(source);

    public static void main(String[] args) {
        Templates.bind(new Client(), "application-content");
    }

    public Set<String> getVaccines() {
        return form.getVaccines();
    }

    public Form getForm() {
        return form;
    }
}