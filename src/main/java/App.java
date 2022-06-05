import controllers.CountryController;
import models.CountryFilter;
import models.HttpTrustedServiceApi;
import views.CountryView;
import views.ProviderView;
import views.StatusView;
import views.TypeView;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class App extends JFrame {
    private JPanel filtersPanel;
    private JPanel servicesPanel;
    public App(){
        setTitle("Trusted Services");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        FlowLayout layout = new FlowLayout();

        //Inizializzo le views
        CountryView countryView = new CountryView();
        ProviderView providerView = new ProviderView();
        StatusView statusView = new StatusView();
        TypeView typeView = new TypeView();

        //Inizializzo l'oggetto HttpTrustedServiceApi che verrà poi passato a tutti i filters tramite i relativi controllers
        HttpTrustedServiceApi service = null;
        try {
            service = new HttpTrustedServiceApi();
            //Inizializzo i filters passando il servizio
            CountryFilter countryFilter = null;
            try {
                countryFilter = new CountryFilter(service);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //Inizializzo i controllers passando view e filter (i filter sarebbero i model nel pattern mvc)
            CountryController countryController = new CountryController(countryView, countryFilter);

            filtersPanel = new JPanel();
            filtersPanel.add(countryView);
            filtersPanel.add(providerView);
            filtersPanel.add(statusView);
            filtersPanel.add(typeView);
            Container pane = getContentPane();
            pane.add(filtersPanel, BorderLayout.PAGE_START);

            servicesPanel = new JPanel();
            servicesPanel.add(new JLabel("Services go here"));
            pane.add(servicesPanel, BorderLayout.PAGE_END);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            var app = new App();
            app.setVisible(true);
        });
    }
}
