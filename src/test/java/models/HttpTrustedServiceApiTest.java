package models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class HttpTrustedServiceApiTest {

    @org.junit.jupiter.api.Test
    void getCountries() {
        HttpTrustedServiceApi service = new HttpTrustedServiceApi();

        try {
            List<Country> countries = service.GetCountries(null);
            assertTrue(countries.size() == 32);

            Filter filter = new Filter();

            countries = service.GetCountries(filter);
            assertTrue(countries.size() == 32);

            List<String> providers = new ArrayList<>();
            providers.add("1&1 De-Mail GmbH");
            filter.setProviders(providers);

            countries = service.GetCountries(filter);
            assertTrue(countries.size() == 1
                    && countries.stream().allMatch(c -> c.GetCountryCode().equals("DE")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @org.junit.jupiter.api.Test
    void getServiceProviders() {
        HttpTrustedServiceApi service = new HttpTrustedServiceApi();

        try {
            List<ServiceProvider> providers = service.GetServiceProviders(null);
            int totalSize = providers.size();
            assertTrue(providers.size() != 0);

            Filter filter = new Filter();

            providers = service.GetServiceProviders(filter);
            assertTrue(providers.size() == totalSize);

            List<String> countries = new ArrayList<>();
            countries.add("Italy");
            filter.setCountries(countries);

            providers = service.GetServiceProviders(filter);
            int italianSize = providers.size();
            assertTrue(providers.size() < totalSize
                    && providers.stream().allMatch(p -> p.getCountryCode().equals("IT")));

            List<String> types = new ArrayList<>();
            types.add("QeRDS");
            filter.setTypes(types);

            providers = service.GetServiceProviders(filter);
            assertTrue(providers.size() <= italianSize
                    && providers.stream().allMatch(p -> p.getCountryCode().equals("IT"))
                    && providers.stream().allMatch(p -> p.getqServiceTypes().contains("QeRDS")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @org.junit.jupiter.api.Test
    void getServiceStatuses() {
        HttpTrustedServiceApi service = new HttpTrustedServiceApi();
        List<ServiceStatus> statuses = null;
        statuses = service.GetServiceStatuses(null);
        int totalSize = statuses.size();
        assertTrue(statuses.size() != 0);

        Filter filter = new Filter();

        statuses = service.GetServiceStatuses(filter);
        assertTrue(statuses.size() == totalSize);

        List<String> providers = new ArrayList<>();
        providers.add("1&1 De-Mail GmbH");
        filter.setProviders(providers);

        statuses = service.GetServiceStatuses(filter);
        int providerSize = statuses.size();
        assertTrue(statuses.size() <= totalSize);

    }

    @org.junit.jupiter.api.Test
    void getServiceTypes() {
        HttpTrustedServiceApi service = new HttpTrustedServiceApi();
        List<ServiceType> types = null;
        types = service.GetServiceTypes(null);
        int totalSize = types.size();
        assertTrue(types.size() != 0);

        Filter filter = new Filter();

        types = service.GetServiceTypes(filter);
        assertTrue(types.size() == totalSize);

        List<String> providers = new ArrayList<>();
        providers.add("1&1 De-Mail GmbH");
        filter.setProviders(providers);

        types = service.GetServiceTypes(filter);
        int providerSize = types.size();
        assertTrue(types.size() <= totalSize);

        List<String> typeFilter = new ArrayList<>();
        typeFilter.add("QeRDS");
        filter.setStatuses(typeFilter);

        types = service.GetServiceTypes(filter);
        assertTrue(types.size() == 1
                      && types.stream().allMatch(s -> s.getName().equals("QeRDS")));
    }

    @org.junit.jupiter.api.Test
    void getServices() {
        HttpTrustedServiceApi service = new HttpTrustedServiceApi();
        List<Service> services = service.GetServices(null);
        assertTrue(!services.isEmpty());

        List<String> countries = new ArrayList<>();
        countries.add("Italy");
        Filter filter = new Filter();
        filter.setCountries(countries);

        services = service.GetServices(filter);
        Optional<String> countryCode = services.stream().map(s -> s.getCountryCode()).distinct().findFirst();
        assertTrue(countryCode.isPresent() && countryCode.get().equals("IT"));

        countries.add("Germany");
        filter.setCountries(countries);
        services = service.GetServices(filter);
        List<String> countryCodes = services
                .stream()
                .map(s -> s.getCountryCode())
                .distinct()
                .collect(Collectors.toList());
        assertTrue(countryCodes.size() == 2 && countryCodes.contains("IT") && countryCodes.contains("DE"));

        List<String> providers = new ArrayList<>();
        providers.add("1&1 De-Mail GmbH");
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

        List<String> types = new ArrayList<>();
        types.add("QeRDS");
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

        assertTrue(countryCodes.size() == 1
                && countryCodes.contains("DE")
                && tspIds.size() == 1
                && tspIds.contains(17)
                && returnedTypes.size() == 1
                && returnedTypes.contains("QeRDS"));

        List<String> statuses = new ArrayList<>();
        statuses.add("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/granted");
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