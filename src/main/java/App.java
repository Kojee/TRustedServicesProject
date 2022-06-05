import controllers.*;
import models.*;
import views.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class App extends JFrame {
    private JPanel filtersPanel;
    private JPanel servicesPanel;
    public App(){
        setTitle("Trusted Services");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        FlowLayout layout = new FlowLayout();

        //Inizializzo le views
        CountryView countryView = new CountryView();
        TypeView typeView = new TypeView();
        StatusView statusView = new StatusView();
        ProviderView providerView = new ProviderView();
        ServiceView serviceView = new ServiceView();

        //Inizializzo l'oggetto HttpTrustedServiceApi che verrà poi passato a tutti i filters tramite i relativi controllers
        HttpTrustedServiceApi service = new HttpTrustedServiceApi();
        //Inizializzo i filters passando il servizio
        CountryFilter countryFilter = null;
        TypeFilter typeFilter = null;
        StatusFilter statusFilter = null;
        ProviderFilter providerFilter = null;
        ServiceFilter serviceFilter = null;
        try {
            countryFilter = new CountryFilter(service);
            typeFilter = new TypeFilter(service);
            statusFilter = new StatusFilter(service);
            providerFilter = new ProviderFilter(service);
            serviceFilter = new ServiceFilter(service, countryFilter, providerFilter, typeFilter, statusFilter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //Inizializzo i controllers passando view e filter (i filter sarebbero i model nel pattern mvc)
        CountryController countryController = new CountryController(countryView, countryFilter);
        TypeController typeController = new TypeController(typeView, typeFilter);
        StatusController statusController = new StatusController(statusView, statusFilter);
        ProviderController providerController = new ProviderController(providerView, providerFilter);
        ServiceController serviceController = new ServiceController(serviceView, serviceFilter);

        filtersPanel = new JPanel();
        filtersPanel.add(countryView);
        filtersPanel.add(typeView);
        filtersPanel.add(statusView);
        filtersPanel.add(providerView);
        Container pane = getContentPane();
        pane.add(filtersPanel, BorderLayout.PAGE_START);

        servicesPanel = new JPanel();
        servicesPanel.add(serviceView);
        pane.add(servicesPanel, BorderLayout.PAGE_END);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            var app = new App();
            app.setVisible(true);
        });
    }
}
