package models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class HttpTrustedServiceApiTest {

    @org.junit.jupiter.api.Test
    void getCountries() throws IOException {
        HttpTrustedServiceApi service = new HttpTrustedServiceApi();
        List<ServiceProvider> allProviders = service.GetServiceProviders(null);


        try {
            List<Country> countries = service.GetCountries(null);
            int totalSize = countries.size();
            assertTrue(!countries.isEmpty());

            Filter filter = new Filter();
            countries = service.GetCountries(filter);
            assertTrue(countries.size() == totalSize);

            List<ServiceProvider> filterProviders = new ArrayList<>();
            filterProviders.add(allProviders.stream().filter(p -> p.getName().equals("1&1 De-Mail GmbH")).findFirst().get());
            filter.setProviders(filterProviders);

            countries = service.GetCountries(filter);
            assertTrue(countries.size() == 1
                    && countries.stream().allMatch(c -> c.GetCountryCode().equals("DE")));

            filter = new Filter();
            List<ServiceType> filterTypes = new ArrayList<>();
            filterTypes.add(new ServiceType("QeRDS"));
            filter.setTypes(filterTypes);
            countries = service.GetCountries(filter);
            assertTrue(countries.size() <= totalSize);
            assertTrue(!countries.isEmpty());

            filter = new Filter();
            List<ServiceStatus> filterStatus = new ArrayList<>();
            filterStatus.add(new ServiceStatus("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/withdrawn"));
            filter.setStatuses(filterStatus);
            countries = service.GetCountries(filter);
            assertTrue(countries.size() <= totalSize);
            assertTrue(!countries.isEmpty());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @org.junit.jupiter.api.Test
    void getServiceProviders() throws IOException {
        HttpTrustedServiceApi service = new HttpTrustedServiceApi();

        List<ServiceProvider> providers = service.GetServiceProviders(null);
        int totalSize = providers.size();
        assertTrue(providers.size() != 0);

        Filter filter = new Filter();

        providers = service.GetServiceProviders(filter);
        assertTrue(providers.size() == totalSize);

        List<Country> countries = new ArrayList<>();
        countries.add(new Country("IT", "Italy"));
        filter.setCountries(countries);

        providers = service.GetServiceProviders(filter);
        int italianSize = providers.size();
        assertTrue(providers.size() < totalSize);
        assertTrue(providers.stream().allMatch(p -> p.getCountryCode().equals("IT")));

        countries.add(new Country("DE", "Germany"));
        filter.setCountries(countries);
        providers = service.GetServiceProviders(filter);
        int italianGermanSize = providers.size();
        assertTrue(providers.size() < totalSize);
        assertTrue(providers.size() > italianSize);
        assertTrue(providers.stream().allMatch(p -> p.getCountryCode().equals("IT") || p.getCountryCode().equals("DE") ));

        List<ServiceType> types = new ArrayList<>();
        types.add(new ServiceType("QeRDS"));
        filter.setTypes(types);

        providers = service.GetServiceProviders(filter);
        int italianGermanQeRDSSize = providers.size();
        assertTrue(providers.size() <= italianGermanSize);
        assertTrue(providers.stream().allMatch(p -> p.getCountryCode().equals("IT") || p.getCountryCode().equals("DE") ));
        assertTrue(providers.stream().allMatch(p -> p.getqServiceTypes().contains("QeRDS")));
        assertTrue(!providers.isEmpty());

        List<ServiceStatus> filterStatus = new ArrayList<>();
        filterStatus.add(new ServiceStatus("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/withdrawn"));
        filter.setStatuses(filterStatus);

        providers = service.GetServiceProviders(filter);
        assertTrue(providers.size() <= italianGermanQeRDSSize);
        assertTrue(providers.stream().allMatch(p -> p.getCountryCode().equals("IT") || p.getCountryCode().equals("DE") ));
        assertTrue(providers.stream().allMatch(p -> p.getqServiceTypes().contains("QeRDS")));
        assertTrue(providers.stream().allMatch(p -> p.getStatuses().contains("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/withdrawn")));
        assertTrue(!providers.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void getServiceStatuses() throws IOException {
        HttpTrustedServiceApi service = new HttpTrustedServiceApi();
        List<ServiceProvider> allProviders = service.GetServiceProviders(null);

        List<ServiceStatus> statuses = null;
        statuses = service.GetServiceStatuses(null);
        int totalSize = statuses.size();
        assertTrue(statuses.size() != 0);

        Filter filter = new Filter();
        statuses = service.GetServiceStatuses(filter);
        assertTrue(statuses.size() == totalSize);

        List<ServiceProvider> providers = new ArrayList<>();
        providers.add(allProviders.stream().filter(p -> p.getName().equals("1&1 De-Mail GmbH")).findFirst().get());
        filter.setProviders(providers);

        statuses = service.GetServiceStatuses(filter);
        int providerSize = statuses.size();
        assertTrue(statuses.size() <= totalSize);

        List<ServiceType> types = new ArrayList<>();
        types.add(new ServiceType("QeRDS"));
        filter.setTypes(types);
        statuses = service.GetServiceStatuses(filter);
        int providerTypeSize = statuses.size();
        assertTrue(statuses.size() <= providerSize);

        List<Country> countries = new ArrayList<>();
        countries.add(new Country("IT", "Italy"));
        filter.setCountries(countries);
        statuses = service.GetServiceStatuses(filter);
        assertTrue(statuses.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void getServiceTypes() throws IOException {
        HttpTrustedServiceApi service = new HttpTrustedServiceApi();
        List<ServiceProvider> allProviders = service.GetServiceProviders(null);

        List<ServiceType> types = null;
        types = service.GetServiceTypes(null);
        int totalSize = types.size();
        assertTrue(types.size() != 0);

        Filter filter = new Filter();

        types = service.GetServiceTypes(filter);
        assertTrue(types.size() == totalSize);

        List<ServiceProvider> providers = new ArrayList<>();
        providers.add(allProviders.stream().filter(p -> p.getName().equals("1&1 De-Mail GmbH")).findFirst().get());
        filter.setProviders(providers);

        types = service.GetServiceTypes(filter);
        int providerSize = types.size();
        assertTrue(types.size() <= totalSize);

        List<ServiceType> typeFilter = new ArrayList<>();
        typeFilter.add(new ServiceType("QeRDS"));
        filter.setTypes(typeFilter);

        types = service.GetServiceTypes(filter);
        assertTrue(types.size() == 1
                      && types.stream().allMatch(s -> s.getName().equals("QeRDS")));
    }

    @org.junit.jupiter.api.Test
    void getServices() throws IOException {
        HttpTrustedServiceApi service = new HttpTrustedServiceApi();
        List<ServiceProvider> allProviders = service.GetServiceProviders(null);

        List<Service> services = service.GetServices(null);
        assertTrue(!services.isEmpty());

        List<Country> countries = new ArrayList<>();
        countries.add(new Country("IT", "Italy"));
        Filter filter = new Filter();
        filter.setCountries(countries);

        services = service.GetServices(filter);
        Optional<String> countryCode = services.stream().map(s -> s.getCountryCode()).distinct().findFirst();
        assertTrue(countryCode.isPresent() && countryCode.get().equals("IT"));

        countries.add(new Country("DE", "Germany"));
        filter.setCountries(countries);
        services = service.GetServices(filter);
        List<String> countryCodes = services
                .stream()
                .map(s -> s.getCountryCode())
                .distinct()
                .collect(Collectors.toList());
        assertTrue(countryCodes.size() == 2 && countryCodes.contains("IT") && countryCodes.contains("DE"));

        List<ServiceProvider> providers = new ArrayList<>();
        providers.add(allProviders.stream().filter(p -> p.getName().equals("1&1 De-Mail GmbH")).findFirst().get());
        filter.setProviders(providers);

        services = service.GetServices(filter);
        countryCodes = services
                .stream()
                .map(s -> s.getCountryCode())
                .distinct()
                .collect(Collectors.toList());
        List<Integer> tspIds = services
                .stream()
                .map(s -> s.getTspId())
                .distinct()
                .collect(Collectors.toList());

        assertTrue(countryCodes.size() == 1
                            && countryCodes.contains("DE")
                            && tspIds.size() == 1
                            && tspIds.contains(17));

        List<ServiceType> types = new ArrayList<>();
        types.add(new ServiceType("QeRDS"));
        filter.setTypes(types);

        services = service.GetServices(filter);

        countryCodes = services
                .stream()
                .map(s -> s.getCountryCode())
                .distinct()
                .collect(Collectors.toList());
        tspIds = services
                .stream()
                .map(s -> s.getTspId())
                .distinct()
                .collect(Collectors.toList());
        List<String> returnedTypes = new ArrayList<>();
        for(Service s : services)
            returnedTypes.addAll(s.getqServiceTypes());
        returnedTypes = returnedTypes.stream().distinct().collect(Collectors.toList());

        assertTrue(countryCodes.size() == 1);
        assertTrue(countryCodes.contains("DE"));
        assertTrue(tspIds.size() == 1);
        assertTrue(tspIds.contains(17));
        assertTrue(returnedTypes.size() == 1);
        assertTrue(returnedTypes.contains("QeRDS"));

        List<ServiceStatus> statuses = new ArrayList<>();
        statuses.add(new ServiceStatus("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/granted"));
        filter.setStatuses(statuses);

        services = service.GetServices(filter);

        countryCodes = services
                .stream()
                .map(s -> s.getCountryCode())
                .distinct()
                .collect(Collectors.toList());
        tspIds = services
                .stream()
                .map(s -> s.getTspId())
                .distinct()
                .collect(Collectors.toList());
        returnedTypes = new ArrayList<>();
        for(Service s : services)
            returnedTypes.addAll(s.getqServiceTypes());
        returnedTypes = returnedTypes.stream().distinct().collect(Collectors.toList());

        List<String> returnedStatuses = services.
                stream().
                map(s -> s.getCurrentStatus()).
                distinct().
                collect(Collectors.toList());

        assertTrue(countryCodes.size() == 1
                && countryCodes.contains("DE")
                && tspIds.size() == 1
                && tspIds.contains(17)
                && returnedTypes.size() == 1
                && returnedTypes.contains("QeRDS")
                && returnedStatuses.size() == 1
                && returnedStatuses.contains("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/granted"));
    }
}