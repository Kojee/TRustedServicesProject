package models;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CountryFilterTest {

    @Test
    void selectCountry() throws IOException {
        HttpTrustedServiceApi serviceApi = new HttpTrustedServiceApi();
        CountryFilter model = new CountryFilter(serviceApi);
        int totalEntities = model.getSelectableEntities().size();

        model.SelectCountry("Italy");
        List<Country> selectedEntities = model.getSelectedEntities();
        assertTrue(selectedEntities.size() == 1
            && selectedEntities.get(0).GetCountryName().equals("Italy"));

        model.SelectCountry("ABC");
        selectedEntities = model.getSelectedEntities();
        List<Country> selectableEntities = model.getSelectableEntities();
        assertTrue(selectedEntities.size() == 1);
        assertTrue(selectedEntities.get(0).GetCountryName().equals("Italy"));

        model.SelectCountry("Germany");
        selectedEntities = model.getSelectedEntities();
        selectableEntities = model.getSelectableEntities();
        assertTrue(selectedEntities.size() == 2);
        assertTrue(selectedEntities.get(0).GetCountryName().equals("Italy"));
        assertTrue(selectedEntities.get(1).GetCountryName().equals("Germany"));

        model.SelectCountry(null);
        selectedEntities = model.getSelectedEntities();
        selectableEntities = model.getSelectableEntities();
        assertTrue(selectedEntities.size() == 2);
        assertTrue(selectedEntities.get(0).GetCountryName().equals("Italy"));
        assertTrue(selectedEntities.get(1).GetCountryName().equals("Germany"));
    }

    @Test
    void deselectCountry() throws IOException {
        HttpTrustedServiceApi serviceApi = new HttpTrustedServiceApi();
        CountryFilter model = new CountryFilter(serviceApi);
        int totalEntities = model.getSelectableEntities().size();

        model.DeselectCountry("Italy");
        assertTrue(model.getSelectedEntities().isEmpty());
        assertTrue(model.getSelectableEntities().size() == totalEntities);

        model.SelectCountry("Italy");

        model.DeselectCountry("Italy");
        assertTrue(model.getSelectedEntities().isEmpty());
        assertTrue(model.getSelectableEntities().size() == totalEntities);

        model.DeselectCountry(null);
        assertTrue(model.getSelectedEntities().isEmpty());
        assertTrue(model.getSelectableEntities().size() == totalEntities);

        model.DeselectCountry("ABC");
        assertTrue(model.getSelectedEntities().isEmpty());
        assertTrue(model.getSelectableEntities().size() == totalEntities);

        model.SelectCountry("Italy");
        model.SelectCountry("Germany");

        model.DeselectCountry("Italy");
        assertTrue(model.getSelectedEntities().size() == 1);
        assertTrue(model.getSelectableEntities().size() == totalEntities - 1);
        assertTrue(model.getSelectedEntities().get(0).GetCountryName().equals("Germany"));
    }

    @Test
    void filterSelectableEntities() throws IOException {
        HttpTrustedServiceApi serviceApi = new HttpTrustedServiceApi();
        CountryFilter model = new CountryFilter(serviceApi);
        int totalEntities = model.getSelectableEntities().size();

        Filter f = new Filter();
        model.FilterSelectableEntities(f);
        assertTrue(model.getSelectableEntities().size() == totalEntities);


        List<String> countries = new ArrayList<>();
        countries.add("Italy");
        f.setCountries(countries);
        model.FilterSelectableEntities(f);
        assertTrue(model.getSelectableEntities().size() == totalEntities);

        List<String> providers = new ArrayList<>();
        providers.add("1&1 De-Mail GmbH");
        f.setProviders(providers);
        model.FilterSelectableEntities(f);
        assertTrue(model.getSelectableEntities().size() == 1);
        assertTrue(model.getSelectableEntities().get(0).GetCountryName().equals("Germany"));

        f = new Filter();
        model.FilterSelectableEntities(f);
        assertTrue(model.getSelectableEntities().size() == totalEntities);

    }
}