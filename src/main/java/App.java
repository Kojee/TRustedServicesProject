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
        setSize(1500, 800);
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
        try {
            HttpTrustedServiceApi service = new HttpTrustedServiceApi();
            //Inizializzo i filters passando il servizio

            CountryFilter countryFilter = new CountryFilter(service);
            TypeFilter typeFilter = new TypeFilter(service);
            StatusFilter statusFilter = new StatusFilter(service);
            ProviderFilter providerFilter = new ProviderFilter(service);
            ServiceFilter serviceFilter = new ServiceFilter(service, countryFilter, providerFilter, typeFilter, statusFilter);

            //Inizializzo i controllers passando view e filter (i filter sarebbero i model nel pattern mvc)
            CountryController countryController = new CountryController(countryView, countryFilter);
            TypeController typeController = new TypeController(typeView, typeFilter);
            StatusController statusController = new StatusController(statusView, statusFilter);
            ProviderController providerController = new ProviderController(providerView, providerFilter);
            ServiceController serviceController = new ServiceController(serviceView, serviceFilter);
        } catch (IOException e) {
            //TODO: mostra alert
            JOptionPane.showMessageDialog(null,
                    "Could not load data! Check your internet connection, then close and reopen the application!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

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
