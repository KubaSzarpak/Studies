package zad1;

import com.formdev.flatlaf.intellijthemes.FlatArcDarkOrangeIJTheme;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.SwingConstants.CENTER;

public class Frame extends JFrame {
    private JFXPanel jfxPanel;
    private final Frame frame;
    private Service service;
    private String city;
    private String currency;
    private boolean isCountryGiven;
    private boolean isCityGiven;
    private boolean isCurrencyGiven;

    public Frame() {
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(new Dimension(1100, 1100));
        this.frame = this;

        isCountryGiven = false;
        isCityGiven = false;
        isCurrencyGiven = false;

        FlatArcDarkOrangeIJTheme.setup();
        FlatArcDarkOrangeIJTheme.updateUI();

        Font defaultFont = new Font("Comic sans ms", Font.BOLD, 25);

        GridBagLayout gridLayout = new GridBagLayout();


        JPanel panel = new JPanel();
        panel.setMinimumSize(new Dimension(800, 500));
        panel.setMaximumSize(new Dimension(800, 500));
        panel.setLayout(gridLayout);

        JPanel header = new JPanel();
        header.setBorder(new BevelBorder(BevelBorder.RAISED));
        header.setMaximumSize(new Dimension(700, 164));
        JPanel body = new JPanel();
        JPanel wiki = new JPanel();

        jfxPanel = new JFXPanel();
        wiki.add(jfxPanel);

        //Header

        //Labels
        {
            GridLayout labelsLayout = new GridLayout();
            labelsLayout.setColumns(1);
            labelsLayout.setRows(3);

            JPanel labels = new JPanel();
            labels.setLayout(labelsLayout);
            labels.setPreferredSize(new Dimension(300, 150));
            labels.setBorder(new BevelBorder(BevelBorder.RAISED));

            JLabel countryLabel = new JLabel("Podaj kraj: ");
            countryLabel.setHorizontalAlignment(CENTER);
            countryLabel.setFont(defaultFont);
            JLabel cityLabel = new JLabel("Podaj nazwę miasta: ");
            cityLabel.setHorizontalAlignment(CENTER);
            cityLabel.setFont(defaultFont);
            JLabel currencyLabel = new JLabel("Podaj walutę: ");
            currencyLabel.setHorizontalAlignment(CENTER);
            currencyLabel.setFont(defaultFont);

            labels.add(countryLabel, 0);
            labels.add(cityLabel, 1);
            labels.add(currencyLabel, 2);

            header.add(labels, 0);

            //Textfields
            {
                GridLayout textfieldsLayout = new GridLayout();
                textfieldsLayout.setColumns(1);
                textfieldsLayout.setRows(3);

                JPanel textfields = new JPanel();
                textfields.setLayout(textfieldsLayout);

                JTextField countryText = new JTextField();
                countryText.setFont(defaultFont);
                JTextField cityText = new JTextField();
                cityText.setFont(defaultFont);
                JTextField currencyText = new JTextField();
                currencyText.setFont(defaultFont);

                textfields.add(countryText, 0);
                textfields.add(cityText, 1);
                textfields.add(currencyText, 2);

                header.add(textfields, 1);


                //Buttons to textfields
                {
                    GridLayout buttonsLayout = new GridLayout();
                    buttonsLayout.setColumns(1);
                    buttonsLayout.setRows(3);

                    JPanel buttons = new JPanel();
                    buttons.setLayout(buttonsLayout);

                    JButton countryButton = new JButton("Zatwierdź kraj");
                    countryButton.setFont(defaultFont);
                    countryButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            service = new Service(countryText.getText());
                            countryText.setText("");
                            isCountryGiven = true;
                        }
                    });

                    JButton cityButton = new JButton("Zatwierdź miasto");
                    cityButton.setFont(defaultFont);
                    cityButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            city = cityText.getText();
                            cityText.setText("");
                            isCityGiven = true;
                            Platform.runLater(frame::createJFXContent);
                        }
                    });

                    JButton currencyButton = new JButton("Zatwierdź walutę");
                    currencyButton.setFont(defaultFont);
                    currencyButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            currency = countryText.getText();
                            currencyText.setText("");
                            isCurrencyGiven = true;

                        }
                    });

                    buttons.add(countryButton, 0);
                    buttons.add(cityButton, 1);
                    buttons.add(currencyButton, 2);

                    header.add(buttons, 2);
                }
            }
        }


        //Body
        {
            //Lables
            {
                GridLayout labelsLayout = new GridLayout();
                labelsLayout.setColumns(1);
                labelsLayout.setRows(3);

                JPanel labels = new JPanel();
                labels.setLayout(labelsLayout);

                JLabel weatherLabel = new JLabel("Pogoda");
                weatherLabel.setHorizontalAlignment(CENTER);
                weatherLabel.setFont(defaultFont);
                JLabel currencyLabel = new JLabel("Przelicznik");
                currencyLabel.setHorizontalAlignment(CENTER);
                currencyLabel.setFont(defaultFont);
                JLabel NBPLabel = new JLabel("NBP");
                NBPLabel.setHorizontalAlignment(CENTER);
                NBPLabel.setFont(defaultFont);

                labels.add(weatherLabel, 0);
                labels.add(currencyLabel, 1);
                labels.add(NBPLabel, 2);

                body.add(labels, 0);


                //Buttons
                {
                    GridLayout buttonsLayout = new GridLayout();
                    buttonsLayout.setColumns(1);
                    buttonsLayout.setRows(3);

                    JPanel buttons = new JPanel();
                    buttons.setLayout(buttonsLayout);

                    JButton weatherButton = new JButton("\t-\t");
                    weatherButton.setFont(defaultFont);
                    weatherButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (isCountryGiven) {
                                if (isCityGiven) {
                                    weatherLabel.setText(service.getWeather(city));
                                } else {
                                    weatherLabel.setText("Podaj miasto");
                                }
                            } else {
                                weatherLabel.setText("Podaj kraj");
                            }
                        }
                    });

                    JButton currencyButton = new JButton("\t-\t");
                    currencyButton.setFont(defaultFont);
                    currencyButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (isCountryGiven) {
                                if (isCurrencyGiven) {
                                    currencyLabel.setText(String.valueOf(service.getRateFor(currency)));
                                } else {
                                    currencyLabel.setText("Podaj walutę");
                                }
                            } else {
                                currencyLabel.setText("Podaj kraj");
                            }

                        }
                    });

                    JButton NBPButton = new JButton("\t-\t");
                    NBPButton.setFont(defaultFont);
                    NBPButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (isCountryGiven) {
                                NBPLabel.setText(String.valueOf(service.getNBPRate()));
                            } else {
                                NBPLabel.setText("Podaj kraj");
                            }
                        }
                    });

                    buttons.add(weatherButton, 0);
                    buttons.add(currencyButton, 1);
                    buttons.add(NBPButton, 2);

                    body.add(buttons, 1);
                }
            }
        }
        //web panel settings
        jfxPanel = new JFXPanel();
        wiki.add(jfxPanel);


        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridy = 0;
        panel.add(header, gbc);
        gbc.gridy = 1;
        panel.add(body, gbc);
        gbc.gridy = 2;
        panel.add(wiki, gbc);


        panel.setVisible(true);
        this.setResizable(false);
        this.add(panel);
        this.setLocationRelativeTo(null);
    }

    private void createJFXContent() {
        WebView webView = new WebView();
        webView.getEngine().load("https://pl.wikipedia.org/wiki/" + city);
        Scene scene = new Scene(webView);
        jfxPanel.setScene(scene);
    }
}
