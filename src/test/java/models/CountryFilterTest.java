package models;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
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
        List<ServiceProvider> allProviders = serviceApi.GetServiceProviders(null);
        CountryFilter model = new CountryFilter(serviceApi);

        int totalSelectableEntities = model.getSelectableEntities().size();

        assertThrows(IllegalArgumentException.class, () -> {
            model.FilterSelectableEntities(null);
        });

        Filter f = new Filter();
        model.FilterSelectableEntities(f);
        assertTrue(model.getSelectableEntities().size() == totalSelectableEntities);
        assertTrue(model.getSelectedEntities().size() == 0);

        List<Country> countries = new ArrayList<>();
        countries.add(new Country("DE", "Germany"));
        f.setCountries(countries);
        model.FilterSelectableEntities(f);
        assertTrue(model.getSelectableEntities().size() == totalSelectableEntities);
        assertTrue(model.getSelectedEntities().size() == 0);

        f = new Filter();
        List<ServiceProvider> providers = new ArrayList<>();
        ServiceProvider firstProv = allProviders.stream().filter(p -> p.getName().equals("1&1 De-Mail GmbH")).findFirst().get();
        providers.add(firstProv);
        f.setProviders(providers);
        model.FilterSelectableEntities(f);
        List<Country> filteredCountries = model.getSelectableEntities();
        int firstProvCountryNumber = filteredCountries.size();
        List<Service> filteredServices = serviceApi.GetServices(f);
        List<String> firstProvServiceCountryCodes = filteredServices.stream()
                        .map(s -> s.getCountryCode())
                        .toList();
        firstProvServiceCountryCodes = new ArrayList<>(new HashSet<>(firstProvServiceCountryCodes));
        List<String> finalfirstProvServiceCountryCodes = firstProvServiceCountryCodes;
        assertTrue(firstProvCountryNumber <= totalSelectableEntities);
        assertTrue(model.getSelectableEntities().stream().allMatch(s -> finalfirstProvServiceCountryCodes.contains(s.GetCountryCode())));

        ServiceProvider secondProv = allProviders.stream().filter(p -> p.getName().equals("Audkenni ehf.")).findFirst().get();
        providers.add(secondProv);
        f.setProviders(providers);
        model.FilterSelectableEntities(f);
        filteredCountries = model.getSelectableEntities();
        int firstSecondProvCountryNumber = filteredCountries.size();
        filteredServices = serviceApi.GetServices(f);
        List<String> firstSecondProvCountryCodes = filteredServices.stream()
                .map(s -> s.getCountryCode())
                .toList();
        firstSecondProvCountryCodes = new ArrayList<>(new HashSet<>(firstSecondProvCountryCodes));
        List<String> finalfirstSecondProvCountryCodes = firstSecondProvCountryCodes;
        assertTrue(firstSecondProvCountryNumber <= totalSelectableEntities);
        assertTrue(firstProvCountryNumber <= firstSecondProvCountryNumber);
        assertTrue(model.getSelectableEntities().stream().allMatch(s -> finalfirstSecondProvCountryCodes.contains(s.GetCountryCode())));

        f = new Filter();
        List<ServiceType> types = new ArrayList<>();
        types.add(new ServiceType("QCertESig"));
        f.setTypes(types);
        model.FilterSelectableEntities(f);
        filteredCountries = model.getSelectableEntities();
        int QCertESigCountryNumber = filteredCountries.size();
        assertTrue(QCertESigCountryNumber <= totalSelectableEntities);

        filteredServices = serviceApi.GetServices(f);
        List<String> QCertESigCountryCodes = new ArrayList<>();
        for(Service s : filteredServices)
            QCertESigCountryCodes.add(s.getCountryCode());
        QCertESigCountryCodes = new ArrayList<>(new HashSet<>(QCertESigCountryCodes));
        List<String> finalQCertESigCountryCodes = QCertESigCountryCodes;
        assertTrue(filteredCountries.stream().allMatch(s -> finalQCertESigCountryCodes.contains(s.GetCountryCode())));

        types.add(new ServiceType("Timestamp"));
        f.setTypes(types);
        model.FilterSelectableEntities(f);
        filteredCountries = model.getSelectableEntities();
        int TimestampQCertESigCountryNumber = filteredCountries.size();
        assertTrue(TimestampQCertESigCountryNumber <= totalSelectableEntities);
        assertTrue(QCertESigCountryNumber <= TimestampQCertESigCountryNumber);
        filteredServices = serviceApi.GetServices(f);
        List<String> QCertESigTimestampCountryCodes = new ArrayList<>();
        for(Service s : filteredServices)
            QCertESigTimestampCountryCodes.add(s.getCountryCode());
        QCertESigTimestampCountryCodes = new ArrayList<>(new HashSet<>(QCertESigTimestampCountryCodes));
        List<String> finalQCertESigTimestampCountryCodes = QCertESigTimestampCountryCodes;
        assertTrue(filteredCountries.stream().allMatch(s -> finalQCertESigTimestampCountryCodes.contains(s.GetCountryCode())));

        f = new Filter();
        List<ServiceStatus> statuses = new ArrayList<>();
        statuses.add(new ServiceStatus("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/recognisedatnationallevel"));
        f.setStatuses(statuses);
        model.FilterSelectableEntities(f);
        filteredCountries = model.getSelectableEntities();
        int recognisedatnationallevelCountryNumber = filteredCountries.size();
        assertTrue(recognisedatnationallevelCountryNumber <= totalSelectableEntities);
        filteredServices = serviceApi.GetServices(f);
        List<String> recognisedatnationallevelCountryCodes = new ArrayList<>();
        for(Service s : filteredServices)
            recognisedatnationallevelCountryCodes.add(s.getCountryCode());
        recognisedatnationallevelCountryCodes = new ArrayList<>(new HashSet<>(recognisedatnationallevelCountryCodes));
        List<String> finalrecognisedatnationallevelCountryCodes = recognisedatnationallevelCountryCodes;
        assertTrue(filteredCountries.stream().allMatch(s -> finalrecognisedatnationallevelCountryCodes.contains(s.GetCountryCode())));

        statuses.add(new ServiceStatus("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/granted"));
        f.setStatuses(statuses);
        model.FilterSelectableEntities(f);
        filteredCountries = model.getSelectableEntities();
        int grantedNationalCountryNumber = filteredCountries.size();
        assertTrue(grantedNationalCountryNumber <= totalSelectableEntities);
        assertTrue(recognisedatnationallevelCountryNumber <= grantedNationalCountryNumber);
        filteredServices = serviceApi.GetServices(f);
        List<String> grantedNationalCountryCodes = new ArrayList<>();
        for(Service s : filteredServices)
            grantedNationalCountryCodes.add(s.getCountryCode());
        grantedNationalCountryCodes = new ArrayList<>(new HashSet<>(grantedNationalCountryCodes));
        List<String> finalgrantedNationalCountryCodes = grantedNationalCountryCodes;
        assertTrue(filteredCountries.stream().allMatch(s -> finalgrantedNationalCountryCodes.contains(s.GetCountryCode())));

        f = new Filter();
        types = new ArrayList<>();
        providers = new ArrayList<>();
        statuses = new ArrayList<>();
        types.add(new ServiceType("Timestamp"));
        providers.add(firstProv);
        statuses.add(new ServiceStatus("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/recognisedatnationallevel"));
        f.setProviders(providers);
        f.setCountries(countries);
        f.setTypes(types);
        model.FilterSelectableEntities(f);
        filteredCountries = model.getSelectableEntities();
        filteredServices = serviceApi.GetServices(f);
        List<String> allFilteredCountryCodes = new ArrayList<>();
        for(Service s : filteredServices)
            allFilteredCountryCodes.add(s.getCountryCode());
        allFilteredCountryCodes = new ArrayList<>(new HashSet<>(allFilteredCountryCodes));
        List<String> finalallFilteredCountryCodes = allFilteredCountryCodes;
        assertTrue(filteredCountries.stream().allMatch(s -> finalallFilteredCountryCodes.contains(s.GetCountryCode())));

    }
}