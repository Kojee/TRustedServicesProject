package models;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProviderFilterTest {

    @Test
    void selectEntity() throws IOException {
        HttpTrustedServiceApi serviceApi = new HttpTrustedServiceApi();
        ProviderFilter model = new ProviderFilter(serviceApi);

        int totalSelectableEntities = model.getSelectableEntities().size();

        model.SelectEntity(null);
        assertTrue(model.getSelectableEntities().size() == totalSelectableEntities);
        assertTrue(model.getSelectedEntities().size() == 0);

        model.SelectEntity("");
        assertTrue(model.getSelectableEntities().size() == totalSelectableEntities);
        assertTrue(model.getSelectedEntities().size() == 0);

        model.SelectEntity("qwfwqdqw");
        assertTrue(model.getSelectableEntities().size() == totalSelectableEntities);
        assertTrue(model.getSelectedEntities().size() == 0);

        model.SelectEntity("Audkenni ehf.");
        assertTrue(model.getSelectableEntities().size() == totalSelectableEntities - 1);
        assertTrue(model.getSelectedEntities().size() == 1);
        assertTrue(model.getSelectedEntities().get(0).getName().equals("Audkenni ehf."));
        assertTrue(model.getSelectableEntities().stream().allMatch(p -> !p.getName().equals("Audkenni ehf.")));

        model.SelectEntity("TrustPro QTSP Ltd");
        assertTrue(model.getSelectableEntities().size() == totalSelectableEntities - 2);
        assertTrue(model.getSelectedEntities().size() == 2);
        assertTrue(model.getSelectedEntities().get(0).getName().equals("Audkenni ehf."));
        assertTrue(model.getSelectedEntities().get(1).getName().equals("TrustPro QTSP Ltd"));
        assertTrue(model.getSelectableEntities().stream().allMatch(p -> !p.getName().equals("Audkenni ehf.")));
        assertTrue(model.getSelectableEntities().stream().allMatch(p -> !p.getName().equals("TrustPro QTSP Ltd")));

    }

    @Test
    void deselectEntity() throws IOException {
        HttpTrustedServiceApi serviceApi = new HttpTrustedServiceApi();
        ProviderFilter model = new ProviderFilter(serviceApi);

        int totalSelectableEntities = model.getSelectableEntities().size();

        model.DeselectEntity(null);
        assertTrue(model.getSelectableEntities().size() == totalSelectableEntities);
        assertTrue(model.getSelectedEntities().size() == 0);

        model.DeselectEntity("");
        assertTrue(model.getSelectableEntities().size() == totalSelectableEntities);
        assertTrue(model.getSelectedEntities().size() == 0);

        model.DeselectEntity("qwfwqdqw");
        assertTrue(model.getSelectableEntities().size() == totalSelectableEntities);
        assertTrue(model.getSelectedEntities().size() == 0);

        model.SelectEntity("Audkenni ehf.");
        assertTrue(model.getSelectableEntities().size() == totalSelectableEntities - 1);
        assertTrue(model.getSelectedEntities().size() == 1);

        model.SelectEntity("TrustPro QTSP Ltd");
        assertTrue(model.getSelectableEntities().size() == totalSelectableEntities - 2);
        assertTrue(model.getSelectedEntities().size() == 2);

        model.DeselectEntity("TrustPro QTSP Ltd");
        assertTrue(model.getSelectableEntities().size() == totalSelectableEntities - 1);
        assertTrue(model.getSelectedEntities().size() == 1);
        assertTrue(model.getSelectedEntities().get(0).getName().equals("Audkenni ehf."));
        assertTrue(model.getSelectableEntities().stream().anyMatch(p -> p.getName().equals("TrustPro QTSP Ltd")));

        model.DeselectEntity("Audkenni ehf.");
        assertTrue(model.getSelectableEntities().size() == totalSelectableEntities);
        assertTrue(model.getSelectedEntities().size() == 0);
        assertTrue(model.getSelectableEntities().stream().anyMatch(p -> p.getName().equals("TrustPro QTSP Ltd")));
        assertTrue(model.getSelectableEntities().stream().anyMatch(p -> p.getName().equals("Audkenni ehf.")));

    }

    @Test
    void filterSelectableEntities() throws IOException {
        HttpTrustedServiceApi serviceApi = new HttpTrustedServiceApi();
        ProviderFilter model = new ProviderFilter(serviceApi);
        int totalSelectableEntities = model.getSelectableEntities().size();
        List<ServiceProvider> allProviders = serviceApi.GetServiceProviders(null);

        assertThrows(IllegalArgumentException.class, () -> {
            model.FilterSelectableEntities(null);
        });

        Filter f = new Filter();
        model.FilterSelectableEntities(f);
        assertTrue(model.getSelectableEntities().size() == totalSelectableEntities);
        assertTrue(model.getSelectedEntities().size() == 0);

        List<ServiceProvider> providers = new ArrayList<>();
        providers.add(allProviders.stream().filter(p -> p.getName().equals("1&1 De-Mail GmbH")).findFirst().get());
        f.setProviders(providers);
        model.FilterSelectableEntities(f);
        assertTrue(model.getSelectableEntities().size() == totalSelectableEntities);
        assertTrue(model.getSelectedEntities().size() == 0);

        List<Country> countries = new ArrayList<>();
        countries.add(new Country("DE", "Germany"));
        f.setCountries(countries);
        model.FilterSelectableEntities(f);
        List<ServiceProvider> filteredProviders = model.getSelectableEntities();
        int germanProvidersNumber = filteredProviders.size();
        assertTrue(germanProvidersNumber < totalSelectableEntities);
        assertTrue(filteredProviders.stream().allMatch(p -> p.getCountryCode().equals("DE")));

        countries.add(new Country("IT", "Italy"));
        f.setCountries(countries);
        model.FilterSelectableEntities(f);
        filteredProviders = model.getSelectableEntities();
        int itDeProvidersNumber = filteredProviders.size();
        assertTrue(itDeProvidersNumber < totalSelectableEntities);
        assertTrue(germanProvidersNumber < itDeProvidersNumber);
        assertTrue(filteredProviders.stream().allMatch(p -> p.getCountryCode().equals("DE") || p.getCountryCode().equals("IT")));

        f = new Filter();
        List<ServiceType> types = new ArrayList<>();
        types.add(new ServiceType("QCertESig"));
        f.setTypes(types);
        model.FilterSelectableEntities(f);
        filteredProviders = model.getSelectableEntities();
        int qCertESigProvidersNumber = filteredProviders.size();
        assertTrue(qCertESigProvidersNumber < totalSelectableEntities);
        assertTrue(filteredProviders.stream().allMatch(p -> p.getqServiceTypes().contains("QCertESig")));

        types.add(new ServiceType("Timestamp"));
        f.setTypes(types);
        model.FilterSelectableEntities(f);
        filteredProviders = model.getSelectableEntities();
        int timestampqCertESigProvidersNumber = filteredProviders.size();
        assertTrue(timestampqCertESigProvidersNumber < totalSelectableEntities);
        assertTrue(qCertESigProvidersNumber < timestampqCertESigProvidersNumber);
        assertTrue(filteredProviders.stream().allMatch(p -> p.getqServiceTypes().contains("QCertESig") || p.getqServiceTypes().contains("Timestamp")));

        f = new Filter();
        List<ServiceStatus> status = new ArrayList<>();
        status.add(new ServiceStatus("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/granted"));
        f.setStatuses(status);
        model.FilterSelectableEntities(f);
        filteredProviders = model.getSelectableEntities();
        int grantedProvidersNumber = filteredProviders.size();
        assertTrue(grantedProvidersNumber <= totalSelectableEntities);
        assertTrue(filteredProviders.stream().allMatch(p -> p.getStatuses().contains("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/granted")));

        types.add(new ServiceType("Timestamp"));
        status.add(new ServiceStatus("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/recognisedatnationallevel"));
        f.setStatuses(status);
        filteredProviders = model.getSelectableEntities();
        int recognizedNationalTimestampqCertESigProvidersNumber = filteredProviders.size();
        assertTrue(recognizedNationalTimestampqCertESigProvidersNumber <= totalSelectableEntities);
        assertTrue(grantedProvidersNumber <= recognizedNationalTimestampqCertESigProvidersNumber);
        assertTrue(filteredProviders.stream().allMatch(p -> p.getStatuses().contains("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/granted")
                || p.getStatuses().contains("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/recognisedatnationallevel")));


        f = new Filter();
        types = new ArrayList<>();
        status = new ArrayList<>();
        countries = new ArrayList<>();
        types.add(new ServiceType("Timestamp"));
        status.add(new ServiceStatus("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/granted"));
        countries.add(new Country("IT", "Italy"));
        f.setStatuses(status);
        f.setCountries(countries);
        f.setTypes(types);
        model.FilterSelectableEntities(f);
        assertTrue(model.getSelectableEntities().size() < totalSelectableEntities);
        assertTrue(model.getSelectableEntities().stream().allMatch(p -> p.getCountryCode().equals("IT")));
        assertTrue(model.getSelectableEntities().stream().allMatch(p -> p.getStatuses().contains("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/granted")));
        assertTrue(model.getSelectableEntities().stream().allMatch(p -> p.getqServiceTypes().contains("Timestamp")));

    }
}