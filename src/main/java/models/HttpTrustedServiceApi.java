package models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class HttpTrustedServiceApi implements ITrustedServiceApi{
    @Override
    public List<Country> GetCountries(Filter filter) throws IOException {
        URL url = new URL("https://esignature.ec.europa.eu/efda/tl-browser/api/v1/search/countries_list");
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        int statusCode = connection.getResponseCode();
        if(statusCode == HttpURLConnection.HTTP_OK){
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }
            in.close();
            Gson gson = new Gson();

            Type collectionType = new TypeToken<ArrayList<Country>>(){}.getType();
            List<Country> countries = gson.fromJson(response.toString(), collectionType);

            if(filter != null){
                //Applichiamo il filtro ai providers se presente
                //TODO: spostare dentro GetServiceProviders
                if(!filter.getProviders().isEmpty()){

                    List<ServiceProvider> providers = GetServiceProviders(null);
                    List<String> providerCountries = providers
                            .stream()
                            .filter(p -> filter.getProviders().contains(p.getName()))
                            .map(p -> p.getCountryCode())
                            .distinct()
                            .collect(Collectors.toList());

                    countries = countries
                            .stream()
                            .filter(c -> providerCountries.contains(c.GetCountryCode()) )
                            .collect(Collectors.toList());
                }

                //Applichiamo il filtro sulle country se presente
                if(!filter.getCountries().isEmpty()){
                    countries = countries
                            .stream()
                            .filter(c -> filter.getCountries().contains(c.GetCountryName()))
                            .collect(Collectors.toList());
                }
            }
            return countries;
        }
        return null;
    }

    @Override
    public List<ServiceProvider> GetServiceProviders(Filter filter) throws IOException {
        URL url = new URL("https://esignature.ec.europa.eu/efda/tl-browser/api/v1/search/tsp_list");
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        int statusCode = connection.getResponseCode();
        if(statusCode == HttpURLConnection.HTTP_OK){
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }
            in.close();
            Gson gson = new Gson();

            Type collectionType = new TypeToken<ArrayList<ServiceProvider>>(){}.getType();
            List<ServiceProvider> providers = gson.fromJson(response.toString(), collectionType);

            //TODO: aggiungere filtro
            if(filter != null){
                //Applichiamo il filtro ai providers
                if(!filter.getProviders().isEmpty()){
                    providers = providers
                            .stream()
                            .filter(p -> filter.getProviders().contains(p.getName()))
                            .collect(Collectors.toList());
                }
                if(!filter.getCountries().isEmpty()){
                    List<Country> countries = GetCountries(null);
                    List<String> validCountryCodes = countries
                            .stream()
                            .filter(c -> filter.getCountries().contains(c.GetCountryName()))
                            .map(c -> c.GetCountryCode())
                            .collect(Collectors.toList());

                    providers = providers
                            .stream()
                            .filter(p -> validCountryCodes.contains(p.getCountryCode()))
                            .collect(Collectors.toList());
                }

                if(!filter.getTypes().isEmpty()){
                    List<ServiceProvider> filteredProviders = new ArrayList<>();
                    for(ServiceProvider p : providers) {
                        Set<String> intersectRes = p.getqServiceTypes()
                                .stream()
                                .filter(filter.getTypes()::contains)
                                .collect(Collectors.toSet());
                        if (!intersectRes.isEmpty())
                            filteredProviders.add(p);
                    }
                    providers = filteredProviders;
                }
            }
            return providers;
        }
        return null;
    }

    @Override
    public List<ServiceStatus> GetServiceStatuses(Filter filter){
        List<ServiceProvider> providers = null;
        try {
            //Ottengo i providers filtrati
            providers = GetServiceProviders(filter);

            List<String> statusNames = new ArrayList<String>();
            for (ServiceProvider p: providers){
                //Ottengo gli stati contenuti nei servizi di ogni providers
                for(String status: p.getStatuses())
                    statusNames.add(status);
            }

            //Dopo aver ottenuto tutti gli stati di tutti i providers, elimino eventuali duplicati
            statusNames = statusNames
                    .stream()
                    .distinct()
                    .collect(Collectors.toList());

            //Eseguo il filtro se presente
            if(filter != null){
                if(!filter.getStatuses().isEmpty()){
                    statusNames = statusNames
                            .stream()
                            .filter(t -> filter.getStatuses().contains(t))
                            .collect(Collectors.toList());
                }
            }
            List<ServiceStatus> statuses = statusNames
                    .stream()
                    .map(x -> new ServiceStatus(x))
                    .collect(Collectors.toList());
            return statuses;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ServiceType> GetServiceTypes(Filter filter) {
        try {
            //Ottengo i providers filtrati
            List<ServiceProvider> providers = GetServiceProviders(filter);

            //Ottengo i tipi di servizio offerti dai provider selezionati
            List<String> typeNames = new ArrayList<String>();
            for(ServiceProvider p : providers){
                typeNames.addAll(p.getqServiceTypes());
            }

            //Tengo solo una copia di ogni tipo di servizio
            typeNames = typeNames
                    .stream()
                    .distinct()
                    .collect(Collectors.toList());

            //Eseguo il filtro se presente
            if(filter != null){
                if(!filter.getTypes().isEmpty()){
                    typeNames = typeNames
                            .stream()
                            .filter(t -> filter.getTypes().contains(t))
                            .collect(Collectors.toList());
                }
            }
            return typeNames
                    .stream()
                    .map(t -> new ServiceType(t))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Service> GetServices(Filter filter) {
        //TODO: test in caso la lista dei providers sia vuota
        try {
            //Ottengo i providers filtrati
            List<ServiceProvider> providers = GetServiceProviders(filter);

            //Ottengo tutti i servizi offerti dai providers selezionati
            List<Service> services = new ArrayList<>();
            for(ServiceProvider p: providers){
                services.addAll(p.getServices());
            }

            if(filter != null){
                List<Service> filteredServices = new ArrayList<>();
                //Il filtro contiene i nomi delle country. Per filtrare i servizi necessito del countryCode
                List<String> countryNames = filter.getCountries();
                if(!countryNames.isEmpty()) {
                    //Estraggo i countryCodes sapendo i nomi delle countries da filtrare
                    List<Country> countries = GetCountries(null);
                    List<String> validCountryCodes = countries
                            .stream()
                            .filter(c -> countryNames.contains(c.GetCountryName()))
                            .map(c -> c.GetCountryCode())
                            .collect(Collectors.toList());
                    for (Service s : services)
                        if (validCountryCodes.contains(s.getCountryCode()))
                            filteredServices.add(s);
                    services = filteredServices;
                    filteredServices = new ArrayList<>();
                }

                List<String> validTypeNames = filter.getTypes();
                if(!validTypeNames.isEmpty()) {
                    for (Service s : services) {
                        Set<String> intersectRes = s.getqServiceTypes()
                                .stream()
                                .filter(validTypeNames::contains)
                                .collect(Collectors.toSet());
                        if (!intersectRes.isEmpty())
                            filteredServices.add(s);
                    }
                    services = filteredServices;
                    filteredServices = new ArrayList<>();
                }

                List<String> validStatuses = filter.getStatuses();
                if(!validStatuses.isEmpty()) {
                    for (Service s : services)
                        if (validStatuses.contains(s.getCurrentStatus()))
                            filteredServices.add(s);
                    services = filteredServices;
                    filteredServices = new ArrayList<>();
                }
            }
            return services;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
