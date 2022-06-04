import models.*;

import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppCLI {
    private static CountryFilter countryFilter;
    private static TypeFilter typeFilter;
    private static StatusFilter statusFilter;
    private static ProviderFilter providerFilter;
    private static ServiceFilter serviceFilter;
    public static void main(String[] args) {
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.out.println("SYstem does not support mandatory UTF-8 output, falling back to default");
        }
        ITrustedServiceApi serviceApi = new HttpTrustedServiceApi();
        try {
            System.out.print("Loading... ");
            countryFilter = new CountryFilter(serviceApi);
            typeFilter = new TypeFilter(serviceApi);
            statusFilter = new StatusFilter(serviceApi);
            providerFilter = new ProviderFilter(serviceApi);
            serviceFilter = new ServiceFilter(serviceApi,
                    countryFilter,
                    providerFilter,
                    typeFilter,
                    statusFilter);

            System.out.println("done");
            printHelp();
            Scanner scanner = new Scanner(System.in);
            Pattern commandPattern = Pattern.compile("^[\\w-]+", Pattern.CASE_INSENSITIVE);
            while(true){
                String line = scanner.nextLine();
                Matcher m = commandPattern.matcher(line);
                if(m.find()){
                    String command = m.group();
                    switch (command){
                        case "help":
                            printHelp();
                            break;
                        case "list-selectable":
                            Pattern entityNamePattern = Pattern.compile(" (type|country|provider|status)$", Pattern.CASE_INSENSITIVE);
                            Matcher entityNameMatcher = entityNamePattern.matcher(line);
                            if(entityNameMatcher.find()){
                                printSelectable(entityNameMatcher.group().trim());
                            }else{
                                System.out.println("Unsupported entity, entity-name must be one of: type, country, provider, status");
                            }
                            break;
                        case "list-selected":
                            entityNamePattern = Pattern.compile(" (type|country|provider|status)$", Pattern.CASE_INSENSITIVE);
                            entityNameMatcher = entityNamePattern.matcher(line);
                            if(entityNameMatcher.find()){
                                printSelected(entityNameMatcher.group().trim());
                            }else{
                                System.out.println("Unsupported entity, entity-name must be one of: type, country, provider, status");
                            }
                            break;
                        case "add-filter":
                            String removeCommandLine = line.replace("add-filter ", "");
                            entityNamePattern = Pattern.compile("^(type|country|provider|status) ", Pattern.CASE_INSENSITIVE);
                            entityNameMatcher = entityNamePattern.matcher(removeCommandLine);
                            if(entityNameMatcher.find()){
                                String filterString = removeCommandLine.replace(entityNameMatcher.group(), "");
                                filterEntities(entityNameMatcher.group().trim(), filterString, true);
                            }else{
                                System.out.println("Unsupported entity, entity-name must be one of: type, country, provider, status");
                            }
                            break;
                        case "remove-filter":
                            removeCommandLine = line.replace("remove-filter ", "");
                            entityNamePattern = Pattern.compile("^(type|country|provider|status) ", Pattern.CASE_INSENSITIVE);
                            entityNameMatcher = entityNamePattern.matcher(removeCommandLine);
                            if(entityNameMatcher.find()){
                                String filterString = removeCommandLine.replace(entityNameMatcher.group(), "");
                                filterEntities(entityNameMatcher.group().trim(), filterString, false);
                            }else{
                                System.out.println("Unsupported entity, entity-name must be one of: type, country, provider, status");
                            }
                            break;
                        case "get-services":
                            printServices();
                            break;
                        default:
                            System.out.println("Command not found");
                            break;
                    }

                }else{
                    System.out.println("Could not parse command");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void printServices() {
        List<Service> serviceList = serviceFilter.getServices();
        System.out.println("Service name | Country Code | Status | Types");
        for(Service s : serviceList)
            System.out.println(
                    String.format("%s | %s | %s | %s",
                            s.getServiceName(),
                            s.getCountryCode(),
                            s.getCurrentStatus(),
                            String.join(",", s.getqServiceTypes())
                    ));
    }

    private static void filterEntities(String entityName, String filterString, boolean add) {
        if(add)
            System.out.print("Adding filter... ");
        else
            System.out.print("Removing filter... ");
        switch(entityName){
            case "type":
                if(add)
                    typeFilter.SelectEntity(filterString);
                else
                    typeFilter.DeselectEntity(filterString);
            case "country":
                if(add)
                    countryFilter.SelectEntity(filterString);
                else
                    countryFilter.DeselectEntity(filterString);
                    break;
            case "status":
                if(add)
                    statusFilter.SelectEntity(filterString);
                else
                    statusFilter.DeselectEntity(filterString);
                    break;
            case "provider":
                if(add)
                    providerFilter.SelectEntity(filterString);
                else
                    providerFilter.DeselectEntity(filterString);
                    break;
        }

        System.out.println("done");
    }

    private static void printSelected(String entityName) {
        switch(entityName){
            case "type":
                System.out.println("Type Name");
                for(ServiceType e : typeFilter.getSelectedEntities())
                    System.out.println(e.getName());
                break;
            case "country":
                System.out.println("Country Name");
                for(Country e : countryFilter.getSelectedEntities())
                    System.out.println(e.GetCountryName());
                break;
            case "status":
                System.out.println("Status Name");
                for(ServiceStatus e : statusFilter.getSelectedEntities())
                    System.out.println(e.getStatus());
                break;
            case "provider":
                System.out.println("Provider Name");
                for(ServiceProvider e : providerFilter.getSelectedEntities())
                    System.out.println(e.getName());
                break;
        }
    }

    private static void printSelectable(String entityName) {
        switch(entityName){
            case "type":
                System.out.println("Type Name");
                for(ServiceType e : typeFilter.getSelectableEntities())
                    System.out.println(e.getName());
                break;
            case "country":
                System.out.println("Country Name");
                for(Country e : countryFilter.getSelectableEntities())
                    System.out.println(e.GetCountryName());
                break;
            case "status":
                System.out.println("Status Name");
                for(ServiceStatus e : statusFilter.getSelectableEntities())
                    System.out.println(e.getStatus());
                break;
            case "provider":
                System.out.println("Provider Name");
                for(ServiceProvider e : providerFilter.getSelectableEntities())
                    System.out.println(e.getName());
                break;
        }
    }

    public static void printHelp(){
        System.out.println("Available commands:");
        System.out.println("help: prints this page");
        System.out.println("list-selectable entity_name: prints the requested selectable entities based on entity-name");
        System.out.println("\tentity-name: type | status | country | provider");
        System.out.println("list-selected entity_name: prints the requested selected entities based on entity-name");
        System.out.println("\tentity-name: type | status | country | provider");
        System.out.println("add-filter entity_name string: filter entity_name by string");
        System.out.println("\tentity-name: type | status | country | provider");
        System.out.println("remove-filter entity_name string: remove string filter from entity_name");
        System.out.println("\tentity-name: type | status | country | provider");
        System.out.println("get-services: print the list of services based on the currently selected entities");
    }
}
