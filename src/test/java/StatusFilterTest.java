import models.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class StatusFilterTest {

    @Test
    void selectEntity() throws IOException {
        HttpTrustedServiceApi serviceApi = new HttpTrustedServiceApi();
        StatusFilter model = new StatusFilter(serviceApi);
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

        model.SelectEntity("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/recognisedatnationallevel");
        assertTrue(model.getSelectableEntities().size() == totalSelectableEntities - 1);
        assertTrue(model.getSelectedEntities().size() == 1);
        assertTrue(model.getSelectedEntities().get(0).getStatus().equals("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/recognisedatnationallevel"));
        assertTrue(model.getSelectableEntities().stream().allMatch(p -> !p.getStatus().equals("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/recognisedatnationallevel")));

        model.SelectEntity("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/granted");
        assertTrue(model.getSelectableEntities().size() == totalSelectableEntities - 2);
        assertTrue(model.getSelectedEntities().size() == 2);
        assertTrue(model.getSelectedEntities().get(0).getStatus().equals("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/recognisedatnationallevel"));
        assertTrue(model.getSelectedEntities().get(1).getStatus().equals("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/granted"));
        assertTrue(model.getSelectableEntities().stream().allMatch(p -> !p.getStatus().equals("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/recognisedatnationallevel")));
        assertTrue(model.getSelectableEntities().stream().allMatch(p -> !p.getStatus().equals("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/granted")));


    }

    @Test
    void deselectEntity() throws IOException {
        HttpTrustedServiceApi serviceApi = new HttpTrustedServiceApi();
        StatusFilter model = new StatusFilter(serviceApi);
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

        model.SelectEntity("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/recognisedatnationallevel");
        assertTrue(model.getSelectableEntities().size() == totalSelectableEntities - 1);
        assertTrue(model.getSelectedEntities().size() == 1);

        model.SelectEntity("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/granted");
        assertTrue(model.getSelectableEntities().size() == totalSelectableEntities - 2);
        assertTrue(model.getSelectedEntities().size() == 2);

        model.DeselectEntity("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/granted");
        assertTrue(model.getSelectableEntities().size() == totalSelectableEntities - 1);
        assertTrue(model.getSelectedEntities().size() == 1);
        assertTrue(model.getSelectedEntities().get(0).getStatus().equals("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/recognisedatnationallevel"));
        assertTrue(model.getSelectableEntities().stream().anyMatch(p -> p.getStatus().equals("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/granted")));

        model.DeselectEntity("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/recognisedatnationallevel");
        assertTrue(model.getSelectableEntities().size() == totalSelectableEntities);
        assertTrue(model.getSelectedEntities().size() == 0);
        assertTrue(model.getSelectableEntities().stream().anyMatch(p -> p.getStatus().equals("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/granted")));
        assertTrue(model.getSelectableEntities().stream().anyMatch(p -> p.getStatus().equals("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/recognisedatnationallevel")));

    }

    @Test
    void filterSelectableEntities() throws IOException {
        HttpTrustedServiceApi serviceApi = new HttpTrustedServiceApi();
        StatusFilter model = new StatusFilter(serviceApi);
        int totalSelectableEntities = model.getSelectableEntities().size();
        List<ServiceProvider> allProviders = serviceApi.GetServiceProviders(null);

        assertThrows(IllegalArgumentException.class, () -> {
            model.FilterSelectableEntities(null);
        });

        Filter f = new Filter();
        model.FilterSelectableEntities(f);
        assertTrue(model.getSelectableEntities().size() == totalSelectableEntities);
        assertTrue(model.getSelectedEntities().size() == 0);

        List<ServiceStatus> statuses = new ArrayList<>();
        statuses.add(new ServiceStatus("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/recognisedatnationallevel"));
        f.setStatuses(statuses);
        model.FilterSelectableEntities(f);
        assertTrue(model.getSelectableEntities().size() == totalSelectableEntities);
        assertTrue(model.getSelectedEntities().size() == 0);

        List<Country> countries = new ArrayList<>();
        countries.add(new Country("DE", "Germany"));
        f.setCountries(countries);
        model.FilterSelectableEntities(f);
        List<ServiceStatus> filteredStatuses = model.getSelectableEntities();
        int germanStatusNumber = filteredStatuses.size();
        assertTrue(germanStatusNumber <= totalSelectableEntities);
        List<ServiceProvider> germanProviders = allProviders
                .stream()
                .filter(p -> p.getCountryCode().equals("DE")).toList();
        List<String> germanStatuses = new ArrayList<>();
        for(ServiceProvider p : germanProviders)
            germanStatuses.addAll(p.getStatuses());
        germanStatuses = new ArrayList<>(new HashSet<>(germanStatuses));
        List<String> finalGermanStatuses = germanStatuses;
        assertTrue(filteredStatuses.stream().allMatch(s -> finalGermanStatuses.contains(s.getStatus())));
        assertEquals(filteredStatuses.size(), germanStatuses.size());

        countries.add(new Country("IT", "Italy"));
        f.setCountries(countries);
        model.FilterSelectableEntities(f);
        filteredStatuses = model.getSelectableEntities();
        int itDeStatusesNumber = filteredStatuses.size();
        assertTrue(itDeStatusesNumber <= totalSelectableEntities);
        assertTrue(germanStatusNumber <= itDeStatusesNumber);

        List<ServiceProvider> itDeProviders = allProviders
                .stream()
                .filter(p -> p.getCountryCode().equals("DE") || p.getCountryCode().equals("IT")).toList();
        List<String> itDeStatuses = new ArrayList<>();
        for(ServiceProvider p : itDeProviders)
            itDeStatuses.addAll(p.getStatuses());
        itDeStatuses = new ArrayList<>(new HashSet<>(itDeStatuses));
        List<String> finalItDeStatuses = itDeStatuses;
        assertTrue(filteredStatuses.stream().allMatch(s -> finalItDeStatuses.contains(s.getStatus())));
        assertEquals(filteredStatuses.size(), finalItDeStatuses.size());


        f = new Filter();
        List<ServiceType> types = new ArrayList<>();
        types.add(new ServiceType("QCertESig"));
        f.setTypes(types);
        model.FilterSelectableEntities(f);
        filteredStatuses = model.getSelectableEntities();
        int QCertESigStatusNumber = filteredStatuses.size();
        assertTrue(QCertESigStatusNumber <= totalSelectableEntities);

        List<Service> filteredServices = serviceApi.GetServices(f);
        List<String> serviceStatuses = new ArrayList<>();
        for(Service s : filteredServices)
            serviceStatuses.add(s.getCurrentStatus());
        serviceStatuses = new ArrayList<>(new HashSet<>(serviceStatuses));
        List<String> finalQCertServiceStatuses = serviceStatuses;
        assertTrue(filteredStatuses.stream().allMatch(s -> finalQCertServiceStatuses.contains(s.getStatus())));
        assertEquals(filteredStatuses.size(), finalQCertServiceStatuses.size());

        types.add(new ServiceType("Timestamp"));
        f.setTypes(types);
        model.FilterSelectableEntities(f);
        filteredStatuses = model.getSelectableEntities();
        int TimestampQCertESigStatusNumber = filteredStatuses.size();
        assertTrue(TimestampQCertESigStatusNumber <= totalSelectableEntities);
        assertTrue(QCertESigStatusNumber <= TimestampQCertESigStatusNumber);
        filteredServices = serviceApi.GetServices(f);
        serviceStatuses = new ArrayList<>();
        for(Service s : filteredServices)
            serviceStatuses.add(s.getCurrentStatus());
        serviceStatuses = new ArrayList<>(new HashSet<>(serviceStatuses));
        List<String> finalQCertTimestampServiceStatuses = serviceStatuses;
        assertTrue(filteredStatuses.stream().allMatch(s -> finalQCertTimestampServiceStatuses.contains(s.getStatus())));
        assertEquals(filteredStatuses.size(), finalQCertTimestampServiceStatuses.size());

        f = new Filter();
        List<ServiceProvider> providers = new ArrayList<>();
        ServiceProvider firstProv = allProviders.stream().filter(p -> p.getName().equals("1&1 De-Mail GmbH")).findFirst().get();
        providers.add(firstProv);
        f.setProviders(providers);
        model.FilterSelectableEntities(f);
        filteredStatuses = model.getSelectableEntities();
        int firstProvStatusNumber = filteredStatuses.size();
        assertTrue(firstProvStatusNumber <= totalSelectableEntities);
        assertTrue(model.getSelectableEntities().stream().allMatch(s -> firstProv.getStatuses().contains(s.getStatus())));
        assertEquals(model.getSelectableEntities().size(), firstProv.getStatuses().size());

        ServiceProvider secondProv = allProviders.stream().filter(p -> p.getName().equals("Audkenni ehf.")).findFirst().get();
        providers.add(secondProv);
        f.setProviders(providers);
        model.FilterSelectableEntities(f);
        filteredStatuses = model.getSelectableEntities();
        int firstSecondProvStatusNumber = filteredStatuses.size();
        assertTrue(firstSecondProvStatusNumber <= totalSelectableEntities);
        assertTrue(firstProvStatusNumber <= firstSecondProvStatusNumber);
        assertTrue(model.getSelectableEntities().stream().allMatch(s -> firstProv.getStatuses().contains(s.getStatus())
                || secondProv.getStatuses().contains(s.getStatus())));



        f = new Filter();
        types = new ArrayList<>();
        providers = new ArrayList<>();
        countries = new ArrayList<>();
        types.add(new ServiceType("Timestamp"));
        providers.add(firstProv);
        countries.add(new Country("IT", "Italy"));
        f.setProviders(providers);
        f.setCountries(countries);
        f.setTypes(types);
        model.FilterSelectableEntities(f);
        filteredStatuses = model.getSelectableEntities();
        filteredServices = serviceApi.GetServices(f);
        serviceStatuses = new ArrayList<>();
        for(Service s : filteredServices)
            serviceStatuses.add(s.getCurrentStatus());
        serviceStatuses = new ArrayList<>(new HashSet<>(serviceStatuses));
        List<String> finalAllFilteredServices = serviceStatuses;
        assertTrue(filteredStatuses.stream().allMatch(s -> finalAllFilteredServices.contains(s.getStatus())));

    }
}