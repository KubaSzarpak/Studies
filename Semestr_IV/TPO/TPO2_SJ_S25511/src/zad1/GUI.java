package zad1;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.HorizontalDirection;
import javafx.scene.Scene;
import javafx.scene.web.WebView;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame{

    private JPanel panel;
    private JTextField krajText;
    private JTextField miastoText;
    private JLabel krajLabel;
    private JLabel miastoLabel;
    private JPanel labelsPanel;
    private JPanel bodyPanel;
    private JPanel buttonsPanel;
    private JLabel weatherLabel;
    private JLabel currencyLabel;
    private JLabel NBPLabel;
    private JButton krajButton;
    private JButton miastoButton;
    private JButton weatherButton;
    private JButton currencyButton;
    private JButton NBPButton;
    private JPanel krajPanel;
    private JPanel miastoPanel;
    private JPanel textfieldsPanel;
    private JPanel wikiPanel;
    private JLabel currLabel;
    private JButton currButton;
    private JTextField currText;
    private JPanel textsPanel;
    private JPanel lPanel;
    private JPanel bPanel;
    private Service service;
    private JFXPanel jfxPanel;
    private boolean podanyKraj;
    private boolean podaneMiasto;
    private String miasto;
    private String currency;
    private GUI gui;

    public GUI() {
        this.gui = this;
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);


        Font defaultFont = new Font("Comic sans ms", Font.BOLD, 28);
        Color backgroundColor = new Color(104, 93, 156);
        Color foregroundLight = new Color(0xE1E1E1);

        //Panels settings
        this.panel.setBackground(backgroundColor);
        this.panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.textfieldsPanel.setBackground(backgroundColor);
        this.textfieldsPanel.setBorder(new BevelBorder(BevelBorder.RAISED));
        this.labelsPanel.setBackground(backgroundColor);
        this.labelsPanel.setPreferredSize(new Dimension(300, 180));
        this.buttonsPanel.setPreferredSize(new Dimension(200, 180));
        this.buttonsPanel.setBackground(backgroundColor);
        this.wikiPanel.setBackground(backgroundColor);
        this.bodyPanel.setBackground(backgroundColor);
        this.textsPanel.setBackground(backgroundColor);
        this.lPanel.setBackground(backgroundColor);
        this.bPanel.setBackground(backgroundColor);


        //Kraj field settings
        this.krajLabel.setText("Podaj nazwę kraju:");
        this.krajLabel.setFont(defaultFont);
        this.krajLabel.setForeground(foregroundLight);
        this.krajLabel.setPreferredSize(new Dimension(320, 50));
        this.krajLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.krajButton.setText("Potwierdź kraj");
        this.krajButton.setFont(defaultFont);
        this.krajButton.setPreferredSize(new Dimension(300, 50));
        this.krajText.setFont(defaultFont);
        this.krajText.setPreferredSize(new Dimension(200, 50));

        //Miasto field settings
        this.miastoLabel.setText("Podaj nazwę miasta:");
        this.miastoLabel.setFont(defaultFont);
        this.miastoLabel.setForeground(foregroundLight);
        this.miastoLabel.setPreferredSize(new Dimension(320, 50));
        this.miastoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.miastoButton.setText("Potwierdź miasto");
        this.miastoButton.setFont(defaultFont);
        this.miastoButton.setPreferredSize(new Dimension(300, 50));
        this.miastoText.setFont(defaultFont);
        this.miastoText.setPreferredSize(new Dimension(200, 50));

        //Currency field settings
        this.currLabel.setText("Podaj walutę:");
        this.currLabel.setFont(defaultFont);
        this.currLabel.setForeground(foregroundLight);
        this.currLabel.setPreferredSize(new Dimension(320, 50));
        this.currLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.currButton.setText("Potwierdź walutę");
        this.currButton.setFont(defaultFont);
        this.currButton.setPreferredSize(new Dimension(300, 50));
        this.currText.setFont(defaultFont);
        this.currText.setPreferredSize(new Dimension(200, 50));


        //booleans
        podanyKraj = false;
        podaneMiasto = false;

        this.krajButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!krajText.getText().equals("")) {
                    service = new Service(krajText.getText());
                    podanyKraj = true;
                    krajText.setText("");
                }
            }
        });

        this.miastoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                miasto = miastoText.getText();
                podaneMiasto = true;
                miastoText.setText("");
                Platform.runLater(gui::createJFXContent);
                gui.pack();
                gui.setLocationRelativeTo(null);
            }
        });

        this.currButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currency = currText.getText();
                currText.setText("");
            }
        });

        //Weather settings
        this.weatherLabel.setText("Weather");
        this.weatherLabel.setFont(defaultFont);
        this.weatherLabel.setForeground(foregroundLight);
        this.weatherLabel.setBorder(new BevelBorder(BevelBorder.RAISED));
        this.weatherLabel.setPreferredSize(new Dimension(300, 35));
        this.weatherButton.setText("Wyświetl pogodę");
        this.weatherButton.setFont(defaultFont);
        this.weatherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (podanyKraj) {
                    if (podaneMiasto) {
                        weatherLabel.setText(service.getWeather(miasto));
                    } else {
                        weatherLabel.setText("Podaj miasto");
                    }
                } else {
                    weatherLabel.setText("Podaj kraj");
                }
            }
        });

        //Currency settings
        this.currencyLabel.setText("Currency");
        this.currencyLabel.setFont(defaultFont);
        this.currencyLabel.setForeground(foregroundLight);
        this.currencyLabel.setBorder(new BevelBorder(BevelBorder.RAISED));
        this.currencyLabel.setPreferredSize(new Dimension(300, 35));
        this.currencyButton.setText("Wyświetl kurs waluty");
        this.currencyButton.setFont(defaultFont);
        this.currencyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (podanyKraj) {
                    currencyLabel.setText(String.valueOf(service.getRateFor(currency)));
                } else {
                    currencyLabel.setText("Podaj kraj");
                }
            }
        });

        //NBP settings
        this.NBPLabel.setText("NBP");
        this.NBPLabel.setFont(defaultFont);
        this.NBPLabel.setForeground(foregroundLight);
        this.NBPLabel.setBorder(new BevelBorder(BevelBorder.RAISED));
        this.NBPLabel.setPreferredSize(new Dimension(300, 35));
        this.NBPButton.setText("Wyświetl kurs złotego do waluty podanego kraju");
        this.NBPButton.setFont(defaultFont);
        this.NBPButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (podanyKraj) {
                    NBPLabel.setText(String.valueOf(service.getNBPRate()));
                } else {
                    NBPLabel.setText("Podaj kraj");
                }
            }
        });

        //web panel settings
        jfxPanel = new JFXPanel();
        Platform.runLater(gui::createJFXContent);
        wikiPanel.add(jfxPanel);


        this.add(panel);
        this.pack();

    }

    private void createJFXContent() {
        WebView webView = new WebView();
        webView.getEngine().load("https://pl.wikipedia.org/wiki/" + miasto);
        Scene scene = new Scene(webView);
        jfxPanel.setScene(scene);
    }
}
