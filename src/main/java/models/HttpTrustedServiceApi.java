package models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class HttpTrustedServiceApi implements ITrustedServiceApi{
    private List<ServiceProvider> cachedProviders;
    private List<Country> cachedCountries;

    public HttpTrustedServiceApi() throws IOException {
        cachedProviders = new ArrayList<>();
        cachedCountries = new ArrayList<>();
        loadProviders();
        loadCountries();
    }

    private void loadProviders() throws IOException {
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

            this.cachedProviders = providers;
        }
    }

    private void loadCountries() throws IOException {
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


            this.cachedCountries = countries;
        }
    }

    @Override
    public List<Country> GetCountries(Filter filter) {
        //Ottengo i providers filtrati
        List<ServiceProvider> providers = GetServiceProviders(filter);
            /*Ottengo lista di providers con le seguenti caratteristiche:
                - Providers presenti tra quelli presenti nel filtro
                - Providers con nazione contenuta tra quelle presenti nel filtro
                - Providers con servizi aventi tipi contenuti tra quelli nel filtro
                - Providers con servizi aventi status contenuti tra quelli nel filtro
              I providers ritornati però contengono tutti i loro servizi, anche quelli che hanno tipo e status non compatibile
              Vanno filtrati ulteriormente. Per fare ciò estraiamo i servizi.
            */
        List<Service> filteredServices = new ArrayList<>();
        for(ServiceProvider p : providers)
            filteredServices.addAll(p.getServices());

        //Andiamo ora a filtrare i servizi per type e status
        if(filter != null){
            if(!filter.getStatuses().isEmpty()) {
                List<String> statusNames = filter.getStatuses().stream().map(s -> s.getStatus()).toList();
                filteredServices = filteredServices
                        .stream()
                        .filter(s -> statusNames.contains(s.getCurrentStatus()))
                        .collect(Collectors.toList());
            }

            if(!filter.getTypes().isEmpty()) {
                List<Service> typeFilteredServices = new ArrayList<>();
                List<String> filterTypeCodes = filter
                        .getTypes()
                        .stream()
                        .map(t -> t.getName())
                        .collect(Collectors.toList());
                for (Service s : filteredServices) {
                    //Eseguo l'intersezione tra i tipi del servizio e i tipi contenuti nel filtro
                    Set<String> intersectRes = s.getqServiceTypes()
                            .stream()
                            .filter(filterTypeCodes::contains)
                            .collect(Collectors.toSet());
                    //Tengo solo i tipi la cui intersezione non è vuota
                    if (!intersectRes.isEmpty())
                        typeFilteredServices.add(s);
                }
                filteredServices = typeFilteredServices;
            }
        }
        //Andiamo ora ad estrarre le countries
        List<String> counrtyCodes = filteredServices
                .stream()
                .map(s -> s.getCountryCode())
                .toList();

        //Otteniamo gli oggetti country dalla lista completa e filtriamo per i country codes trovati
        return this.cachedCountries.stream().filter(c -> counrtyCodes.contains(c.GetCountryCode())).collect(Collectors.toList());
    }

    @Override
    public List<ServiceProvider> GetServiceProviders(Filter filter) {
        List<ServiceProvider> providers = new ArrayList<>(this.cachedProviders);


        if(filter != null){
            //Applichiamo il filtro ai providers
            if(!filter.getProviders().isEmpty()){
                providers = providers
                        .stream()
                        .filter(p -> filter.getProviders().contains(p))
                        .collect(Collectors.toList());
            }
            if(!filter.getCountries().isEmpty()){
                //Estraggo i countryCodes dal filtro
                List<String> validCountryCodes = filter
                        .getCountries()
                        .stream()
                        .map(c -> c.GetCountryCode())
                        .collect(Collectors.toList());

                //Filtro i providers per countryCode
                providers = providers
                        .stream()
                        .filter(p -> validCountryCodes.contains(p.getCountryCode()))
                        .collect(Collectors.toList());
            }

            //Filtro per type
            if(!filter.getTypes().isEmpty()){
                List<ServiceProvider> filteredProviders = new ArrayList<>();
                //Eseguo intersezione tra i tipi nel filtro e i tipi di ogni providers
                List<String> filterTypeCodes = filter
                        .getTypes()
                        .stream()
                        .map(t -> t.getName())
                        .collect(Collectors.toList());
                for(ServiceProvider p : providers) {
                    Set<String> intersectRes = p.getqServiceTypes()
                            .stream()
                            .filter(filterTypeCodes::contains)
                            .collect(Collectors.toSet());
                    //Tengo solo i providers la cui intersezione con i tipi nel filtro non è vuota
                    if (!intersectRes.isEmpty())
                        filteredProviders.add(p);
                }
                providers = filteredProviders;
            }


            //Filtro per status
            if(!filter.getStatuses().isEmpty()){
                List<ServiceProvider> filteredProviders = new ArrayList<>();
                //Estraggo gli status name
                List<String> filterStatusCodes = filter
                        .getStatuses()
                        .stream()
                        .map(s -> s.getStatus())
                        .collect(Collectors.toList());

                for(ServiceProvider p : providers){
                    //Se il provider ha almeno un servizio con lo status tra quelli presenti nel filtro
                    //aggiungo il provider alla lista finale
                    if(p.getServices().stream().anyMatch(s -> filterStatusCodes.contains(s.getCurrentStatus())))
                        filteredProviders.add(p);
                }
                providers = filteredProviders;
            }
        }
        return providers;
    }

    @Override
    public List<ServiceStatus> GetServiceStatuses(Filter filter) {
        List<ServiceProvider> providers = null;
        //Ottengo i providers filtrati
        providers = GetServiceProviders(filter);
            /*Ottengo lista di providers con le seguenti caratteristiche:
                - Providers presenti tra quelli presenti nel filtro
                - Providers con nazione contenuta tra quelle presenti nel filtro
                - Providers con servizi aventi tipi contenuti tra quelli nel filtro
                - Providers con servizi aventi status contenuti tra quelli nel filtro
              I providers ritornati però contengono tutti i loro servizi, anche quelli che hanno tipo e status non compatibile
              Vanno filtrati ulteriormente. Per fare ciò estraiamo i servizi.
            */
        List<Service> filteredServices = new ArrayList<>();
        for(ServiceProvider p : providers)
            filteredServices.addAll(p.getServices());

        //Andiamo ora a filtrare i servizi per type e status
        if(filter != null){
            if(!filter.getStatuses().isEmpty()) {
                List<String> statusNames = filter.getStatuses().stream().map(s -> s.getStatus()).toList();
                filteredServices = filteredServices
                        .stream()
                        .filter(s -> statusNames.contains(s.getCurrentStatus()))
                        .collect(Collectors.toList());
            }

            if(!filter.getTypes().isEmpty()){
                List<Service> typeFilteredServices = new ArrayList<>();
                List<String> filterTypeCodes = filter
                        .getTypes()
                        .stream()
                        .map(t -> t.getName())
                        .collect(Collectors.toList());
                for (Service s : filteredServices) {
                    //Eseguo l'intersezione tra i tipi del servizio e i tipi contenuti nel filtro
                    Set<String> intersectRes = s.getqServiceTypes()
                            .stream()
                            .filter(filterTypeCodes::contains)
                            .collect(Collectors.toSet());
                    //Tengo solo i tipi la cui intersezione non è vuota
                    if (!intersectRes.isEmpty())
                        typeFilteredServices.add(s);
                }
                filteredServices = typeFilteredServices;
            }

        }

        //Ora che ho la lista di servizi filtrata, estraggo tutti gli status
        List<ServiceStatus> statuses = new ArrayList<>();
        statuses.addAll(filteredServices
                .stream()
                .map(s -> new ServiceStatus(s.getCurrentStatus()))
                .collect( Collectors.toList()));

        //Elimino i duplicati
        statuses = new ArrayList<>(new HashSet<>(statuses));
        return statuses;
    }

    @Override
    public List<ServiceType> GetServiceTypes(Filter filter) {
        //Ottengo i providers filtrati
        List<ServiceProvider> providers = GetServiceProviders(filter);
            /*Ottengo lista di providers con le seguenti caratteristiche:
                - Providers presenti tra quelli presenti nel filtro
                - Providers con nazione contenuta tra quelle presenti nel filtro
                - Providers con servizi aventi tipi contenuti tra quelli nel filtro
                - Providers con servizi aventi status contenuti tra quelli nel filtro
              I providers ritornati però contengono tutti i loro servizi, anche quelli che hanno tipo e status non compatibile
              Vanno filtrati ulteriormente. Per fare ciò estraiamo i servizi.
            */
        List<Service> filteredServices = new ArrayList<>();
        for(ServiceProvider p : providers)
            filteredServices.addAll(p.getServices());

        //Andiamo ora a filtrare i servizi per status
        if(filter != null){
            if(!filter.getStatuses().isEmpty()) {
                List<String> statusNames = filter.getStatuses().stream().map(s -> s.getStatus()).toList();
                filteredServices = filteredServices
                        .stream()
                        .filter(s -> statusNames.contains(s.getCurrentStatus()))
                        .collect(Collectors.toList());
            }
        }


        //Ora che ho la lista di servizi filtrata per provider, country e status,
        // estraggo tutti i tipi e elimino i duplicati
        List<ServiceType> types = new ArrayList<>();
        for(Service s : filteredServices)
            types.addAll(s.getqServiceTypes()
                    .stream()
                    .map(t -> new ServiceType(t))
                    .collect(Collectors.toList()));

        //Elimino eventuali duplicati
        types = new ArrayList<>(new HashSet<>(types));

        //Se nel filtro sono impostati dei tipi, filtro
        if(filter != null
                && !filter.getTypes().isEmpty()){
            types = types
                    .stream()
                    .filter(t -> filter.getTypes().contains(t))
                    .collect(Collectors.toList());
        }
        return types;
    }

    @Override
    public List<Service> GetServices(Filter filter) {
        //Ottengo i providers filtrati
        List<ServiceProvider> providers = GetServiceProviders(filter);

        //Ottengo tutti i servizi offerti dai providers selezionati
        List<Service> services = new ArrayList<>();
        for(ServiceProvider p: providers){
            services.addAll(p.getServices());
        }

        if(filter != null){
            //Non filtro per country e providers perchè i provider ottenuti all'inizio sono già filtrati

            //Filtro per type in quanto i provider ottenuti all'inizio potrebbero contenere servizi di tipo diverso
            if(!filter.getTypes().isEmpty()) {
                List<Service> filteredServices = new ArrayList<>();
                List<String> filterTypeCodes = filter
                        .getTypes()
                        .stream()
                        .map(t -> t.getName())
                        .collect(Collectors.toList());
                for (Service s : services) {
                    //Eseguo l'intersezione tra i tipi del servizio e i tipi contenuti nel filtro
                    Set<String> intersectRes = s.getqServiceTypes()
                            .stream()
                            .filter(filterTypeCodes::contains)
                            .collect(Collectors.toSet());
                    //Tengo solo i tipi la cui intersezione non è vuota
                    if (!intersectRes.isEmpty())
                        filteredServices.add(s);
                }
                services = filteredServices;
            }

            //Filtro per status in quanto i provider ottenuti all'inizio potrebbero contenere servizi di status diverso
            if(!filter.getStatuses().isEmpty()) {
                List<String> filterStatusCodes = filter
                        .getStatuses()
                        .stream()
                        .map(s -> s.getStatus())
                        .collect(Collectors.toList());
                services = services
                        .stream()
                        .filter(s -> filterStatusCodes.contains(s.getCurrentStatus()))
                        .collect(Collectors.toList());
            }
        }
        return services;

    }
}
