package models;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TypeFilterTest {

    @Test
    void selectEntity() throws IOException {
        HttpTrustedServiceApi serviceApi = new HttpTrustedServiceApi();
        TypeFilter model = new TypeFilter(serviceApi);

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

        model.SelectEntity("QCertESig");
        assertTrue(model.getSelectableEntities().size() == totalSelectableEntities - 1);
        assertTrue(model.getSelectedEntities().size() == 1);
        assertTrue(model.getSelectedEntities().get(0).getName().equals("QCertESig"));
        assertTrue(model.getSelectableEntities().stream().allMatch(p -> !p.getName().equals("QCertESig")));

        model.SelectEntity("Timestamp");
        assertTrue(model.getSelectableEntities().size() == totalSelectableEntities - 2);
        assertTrue(model.getSelectedEntities().size() == 2);
        assertTrue(model.getSelectedEntities().get(0).getName().equals("QCertESig"));
        assertTrue(model.getSelectedEntities().get(1).getName().equals("Timestamp"));
        assertTrue(model.getSelectableEntities().stream().allMatch(p -> !p.getName().equals("QCertESig")));
        assertTrue(model.getSelectableEntities().stream().allMatch(p -> !p.getName().equals("Timestamp")));

    }

    @Test
    void deselectEntity() throws IOException {
        HttpTrustedServiceApi serviceApi = new HttpTrustedServiceApi();
        TypeFilter model = new TypeFilter(serviceApi);

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

        model.SelectEntity("QCertESig");
        assertTrue(model.getSelectableEntities().size() == totalSelectableEntities - 1);
        assertTrue(model.getSelectedEntities().size() == 1);

        model.SelectEntity("Timestamp");
        assertTrue(model.getSelectableEntities().size() == totalSelectableEntities - 2);
        assertTrue(model.getSelectedEntities().size() == 2);

        model.DeselectEntity("Timestamp");
        assertTrue(model.getSelectableEntities().size() == totalSelectableEntities - 1);
        assertTrue(model.getSelectedEntities().size() == 1);
        assertTrue(model.getSelectedEntities().get(0).getName().equals("QCertESig"));
        assertTrue(model.getSelectableEntities().stream().anyMatch(p -> p.getName().equals("Timestamp")));

        model.DeselectEntity("QCertESig");
        assertTrue(model.getSelectableEntities().size() == totalSelectableEntities);
        assertTrue(model.getSelectedEntities().size() == 0);
        assertTrue(model.getSelectableEntities().stream().anyMatch(p -> p.getName().equals("Timestamp")));
        assertTrue(model.getSelectableEntities().stream().anyMatch(p -> p.getName().equals("QCertESig")));

    }

    @Test
    void filterSelectableEntities() throws IOException {
        HttpTrustedServiceApi serviceApi = new HttpTrustedServiceApi();
        TypeFilter model = new TypeFilter(serviceApi);
        int totalSelectableEntities = model.getSelectableEntities().size();
        List<ServiceProvider> allProviders = serviceApi.GetServiceProviders(null);

        assertThrows(IllegalArgumentException.class, () -> {
            model.FilterSelectableEntities(null);
        });

        Filter f = new Filter();
        model.FilterSelectableEntities(f);
        assertTrue(model.getSelectableEntities().size() == totalSelectableEntities);
        assertTrue(model.getSelectedEntities().size() == 0);

        List<ServiceType> types = new ArrayList<>();
        types.add(new ServiceType("QCertESig"));
        f.setTypes(types);
        model.FilterSelectableEntities(f);
        assertTrue(model.getSelectableEntities().size() == totalSelectableEntities);
        assertTrue(model.getSelectedEntities().size() == 0);

        f = new Filter();
        List<Country> countries = new ArrayList<>();
        countries.add(new Country("DE", "Germany"));
        f.setCountries(countries);
        model.FilterSelectableEntities(f);
        List<ServiceType> filteredTypes = model.getSelectableEntities();
        List<Service> filteredServices = serviceApi.GetServices(f);
        List<String> serviceTypes = new ArrayList<>();
        for(Service s : filteredServices)
            serviceTypes.addAll(s.getqServiceTypes());
        serviceTypes = new ArrayList<>(new HashSet<>(serviceTypes));
        List<String> finalDeServiceTypes = serviceTypes;
        assertTrue(filteredTypes.stream().allMatch(s -> finalDeServiceTypes.contains(s.getName())));
        assertEquals(filteredTypes.size(), finalDeServiceTypes.size());

        countries.add(new Country("IT", "Italy"));
        f.setCountries(countries);
        model.FilterSelectableEntities(f);
        List<Service> filtereditDeServices = serviceApi.GetServices(f);
        List<String> serviceitDeTypes = new ArrayList<>();
        for(Service s : filtereditDeServices)
            serviceitDeTypes.addAll(s.getqServiceTypes());
        serviceitDeTypes = new ArrayList<>(new HashSet<>(serviceitDeTypes));
        List<String> finalitDeServiceTypes = serviceitDeTypes;
        assertTrue(filteredTypes.stream().allMatch(s -> finalitDeServiceTypes.contains(s.getName())));

        f = new Filter();
        List<ServiceStatus> statuses = new ArrayList<>();
        statuses.add(new ServiceStatus("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/recognisedatnationallevel"));
        f.setStatuses(statuses);
        model.FilterSelectableEntities(f);
        filteredTypes = model.getSelectableEntities();
        int recognisedatnationallevelTypesNumber = filteredTypes.size();
        assertTrue(recognisedatnationallevelTypesNumber <= totalSelectableEntities);

        List<Service> recognisedatnationallevelServices = serviceApi.GetServices(f);
        List<String> recognisedatnationallevelTypes = new ArrayList<>();
        for(Service s : recognisedatnationallevelServices)
            recognisedatnationallevelTypes.addAll(s.getqServiceTypes());
        recognisedatnationallevelTypes = new ArrayList<>(new HashSet<>(recognisedatnationallevelTypes));
        List<String> finalrecognisedatnationallevelTypes = recognisedatnationallevelTypes;
        assertTrue(filteredTypes.stream().allMatch(s -> finalrecognisedatnationallevelTypes.contains(s.getName())));
        assertEquals(filteredTypes.size(), finalrecognisedatnationallevelTypes.size());

        statuses.add(new ServiceStatus("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/granted"));
        f.setStatuses(statuses);
        model.FilterSelectableEntities(f);
        filteredTypes = model.getSelectableEntities();
        int grantedNationalTypesNumber = filteredTypes.size();
        assertTrue(grantedNationalTypesNumber <= totalSelectableEntities);
        assertTrue(recognisedatnationallevelTypesNumber <= grantedNationalTypesNumber);
        filteredServices = serviceApi.GetServices(f);
        List<String> grantedNationalTypes = new ArrayList<>();
        for(Service s : filteredServices)
            grantedNationalTypes.addAll(s.getqServiceTypes());
        grantedNationalTypes = new ArrayList<>(new HashSet<>(grantedNationalTypes));
        List<String> finalgrantedNationalTypes = grantedNationalTypes;
        assertTrue(filteredTypes.stream().allMatch(s -> finalgrantedNationalTypes.contains(s.getName())));
        assertEquals(filteredTypes.size(), finalgrantedNationalTypes.size());

        f = new Filter();
        List<ServiceProvider> providers = new ArrayList<>();
        ServiceProvider firstProv = allProviders.stream().filter(p -> p.getName().equals("1&1 De-Mail GmbH")).findFirst().get();
        providers.add(firstProv);
        f.setProviders(providers);
        model.FilterSelectableEntities(f);
        filteredTypes = model.getSelectableEntities();
        int firstProvTypesNumber = filteredTypes.size();
        filteredServices = serviceApi.GetServices(f);
        List<String> firstProvTypes = new ArrayList<>();
        for(Service s : filteredServices)
            firstProvTypes.addAll(s.getqServiceTypes());
        firstProvTypes = new ArrayList<>(new HashSet<>(firstProvTypes));
        List<String> finalfirstProvTypes = firstProvTypes;
        assertTrue(filteredTypes.stream().allMatch(s -> finalfirstProvTypes.contains(s.getName())));
        assertEquals(filteredTypes.size(), finalfirstProvTypes.size());

        ServiceProvider secondProv = allProviders.stream().filter(p -> p.getName().equals("Audkenni ehf.")).findFirst().get();
        providers.add(secondProv);
        f.setProviders(providers);
        model.FilterSelectableEntities(f);
        filteredTypes = model.getSelectableEntities();
        int firstSecondProvStatusNumber = filteredTypes.size();
        filteredServices = serviceApi.GetServices(f);
        List<String> firstSecondProvTypes = new ArrayList<>();
        for(Service s : filteredServices)
            firstSecondProvTypes.addAll(s.getqServiceTypes());
        firstSecondProvTypes = new ArrayList<>(new HashSet<>(firstSecondProvTypes));
        List<String> finalfirstSecondProvTypes = firstSecondProvTypes;
        assertTrue(filteredTypes.stream().allMatch(s -> finalfirstSecondProvTypes.contains(s.getName())));

        f = new Filter();
        statuses = new ArrayList<>();
        providers = new ArrayList<>();
        countries = new ArrayList<>();
        providers.add(firstProv);
        countries.add(new Country("IT", "Italy"));
        statuses.add(new ServiceStatus("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/recognisedatnationallevel"));
        f.setProviders(providers);
        f.setCountries(countries);
        f.setTypes(types);
        model.FilterSelectableEntities(f);
        filteredTypes = model.getSelectableEntities();
        filteredServices = serviceApi.GetServices(f);
        List<String> allFilteredTypes = new ArrayList<>();
        for(Service s : filteredServices)
            allFilteredTypes.addAll(s.getqServiceTypes());
        allFilteredTypes = new ArrayList<>(new HashSet<>(allFilteredTypes));
        List<String> finalallFilteredTypes = allFilteredTypes;
        assertTrue(filteredTypes.stream().allMatch(s -> finalallFilteredTypes.contains(s.getName())));
    }
}