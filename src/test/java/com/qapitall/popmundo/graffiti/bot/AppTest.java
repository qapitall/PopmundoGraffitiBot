package com.qapitall.popmundo.graffiti.bot;

import static org.junit.Assert.assertTrue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by Qapitall on 28/08/19.
 */

class App extends JFrame implements Runnable {

    private JLabel labelUsername = new JLabel("Enter username: ");
    private JLabel labelPassword = new JLabel("Enter password: ");
    private JTextField textUsername = new JTextField(15);
    private JPasswordField fieldPassword = new JPasswordField(15);
    private JButton buttonLogin = new JButton("Start");
    private String[] locales = { "Abandoned Warehouse","Airport","Arms Dealer","Artist Agency","Bank","Black Market","Bus Terminal","Car Dealer",
            "Car Park","City Hall","Club","Construction Company","Countryside","Court House","Estate Agency","Factory","Festival Grounds","Fire Station",
            "Generic Shop","Graveyard","Grocery Shop","Gym","Headquarters","Hospital","Hotel","Jam place","Law firm","Lost and Found","Motorway out of town",
            "Museum","Nursery School","Park","Pet Shop","Plastic Surgery Centre","Police Station","Post Office","Power Plant","Prison","Psychology Clinic",
            "Public Beach","Recording Studio","Restaurant","Rubbish dump","School","Security firm","Shop","Social Club","Stadium","Stage Equipment Shop",
            "Tattoo Parlour","Temple","TV Station","Underground","University","Water Reservoir" };
    private JLabel labelLocale = new JLabel("Select a Location: ");
    private JComboBox locatesBox = new JComboBox(locales);
    private JTextArea logArea = new JTextArea(20,60);
    private JScrollPane taScroll = new JScrollPane(logArea);
    protected WebDriver driver;
    private Random randomDelay = new Random();
    private int completedGraffiti = 0;
    private int localNumber = 1;
    private String txtMsg="";
    private Thread thread;
    private App app = this;
    private Calendar calendar;
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    private boolean tearFlag = true;

    public App() {
        super("Popmundo Graffiti Bot v1.0");

        Container contentPane = getContentPane();
        GridBagLayout mainGridBagLayout = new GridBagLayout();
        GridBagConstraints mainConstraints = new GridBagConstraints();
        contentPane.setLayout(mainGridBagLayout);

        JPanel loginPanel = new JPanel(new GridBagLayout());

        GridBagConstraints loginConstraints = new GridBagConstraints();
        loginConstraints.anchor = GridBagConstraints.WEST;
        loginConstraints.insets = new Insets(10, 32, 10, 32);

        loginConstraints.gridx = 0;
        loginConstraints.gridy = 0;
        loginPanel.add(labelUsername, loginConstraints);

        loginConstraints.gridx = 1;
        loginPanel.add(textUsername, loginConstraints);

        loginConstraints.gridx = 0;
        loginConstraints.gridy = 1;
        loginPanel.add(labelPassword, loginConstraints);

        loginConstraints.gridx = 1;
        loginPanel.add(fieldPassword, loginConstraints);

        loginPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Login Panel"));

        mainConstraints.gridx=0;
        mainConstraints.gridy=0;
        mainGridBagLayout.setConstraints(loginPanel,mainConstraints);
        contentPane.add(loginPanel);

        JPanel controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Control Panel"));

        GridBagConstraints controlConstraints = new GridBagConstraints();
        controlConstraints.anchor = GridBagConstraints.WEST;
        controlConstraints.insets = new Insets(10, 32, 10, 32);

        controlConstraints.gridx = 0;
        controlConstraints.gridy = 0;
        controlPanel.add(labelLocale, controlConstraints);

        controlConstraints.gridx = 1;
        controlConstraints.gridy = 0;
        controlPanel.add(locatesBox, controlConstraints);

        controlConstraints.gridx = 0;
        controlConstraints.gridy = 1;
        controlConstraints.gridwidth = 2;
        controlConstraints.anchor = GridBagConstraints.CENTER;
        controlPanel.add(buttonLogin, controlConstraints);

        mainConstraints.gridx=1;
        mainConstraints.gridy=0;
        mainGridBagLayout.setConstraints(controlPanel,mainConstraints);
        contentPane.add(controlPanel);

        JPanel logPanel = new JPanel(new GridBagLayout());
        logPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Log Panel"));

        GridBagConstraints logConstraints = new GridBagConstraints();
        logConstraints.anchor = GridBagConstraints.WEST;
        logConstraints.insets = new Insets(10, 2, 10, 2);

        logArea.setEditable(false);
        logArea.setFocusable(false);
        logArea.setFont(new Font(Font.SANS_SERIF, 0, 14));

        logConstraints.gridx = 0;
        logConstraints.gridy = 0;
        logConstraints.gridwidth = 10;
        logConstraints.anchor = GridBagConstraints.CENTER;
        logPanel.add(taScroll, logConstraints);

        mainConstraints.gridx=0;
        mainConstraints.gridy=1;
        mainConstraints.gridwidth=2;
        mainGridBagLayout.setConstraints(logPanel,mainConstraints);
        contentPane.add(logPanel);

        buttonLogin.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(textUsername.getText().length()>3 && fieldPassword.getText().length()>3){
                        if(buttonLogin.getText().equals("Start")){
                            calendar = Calendar.getInstance();
                            txtMsg = txtMsg + sdf.format(calendar.getTime()) +" - Starting process\n";
                            logArea.setText(txtMsg);

                            thread = new Thread(app);
                            thread.start();

                            new Thread(new Runnable() {
                                @Override
                                public void run(){
                                    try {
                                        buttonLogin.setEnabled(false);
                                        Thread.sleep(1000);
                                        buttonLogin.setEnabled(true);
                                        buttonLogin.setText("Stop");
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }).start();

                            textUsername.setEnabled(false);
                            fieldPassword.setEnabled(false);
                            locatesBox.setEnabled(false);
                        }else{
                            Thread.sleep(1000);
                            tearDown();
                            tearFlag = false;
                            buttonLogin.setText("Start");
                            textUsername.setEnabled(true);
                            fieldPassword.setEnabled(true);
                            locatesBox.setEnabled(true);
                        }
                    }else{
                        calendar = Calendar.getInstance();
                        txtMsg = txtMsg + sdf.format(calendar.getTime()) +" - Username or Password is empty!\n";
                        logArea.setText(txtMsg);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new App().setVisible(true);
            }
        });
    }

    @Before
    public void setUp(){
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setJavascriptEnabled(true);
        capabilities.setBrowserName("Popmundo Graffiti Bot v1.0 Browser");
        capabilities.setVersion("v1.0");
        capabilities.setCapability("takesScreenshot", true);
        capabilities.setCapability(
                PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                "driver/phantomjs.exe"
        );
        driver = new PhantomJSDriver(capabilities);
        driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    public String paintWall(){
        driver.findElement(By.linkText("Items")).click();
        driver.findElement(By.linkText("Can of spraypaint")).click();
        driver.findElement(By.id("ctl00_cphLeftColumn_ctl00_btnItemUse")).click();
        driver.switchTo().activeElement().sendKeys(Keys.ENTER);
        return driver.findElement(By.xpath("//*[@id='notifications']/div[2]")).getText();
    }

    @After
    public void tearDown(){
        driver.quit();
    }

    @Test
    @Override
    public void run() {
        try {
            setUp();
            driver.get("https://www.popmundo.com/");
            Thread.sleep(1000);
            driver.findElement(By.id("ctl00_cphRightColumn_ucLogin_txtUsername")).sendKeys(textUsername.getText().toString());
            driver.findElement(By.id("ctl00_cphRightColumn_ucLogin_txtPassword")).sendKeys(fieldPassword.getText().toString());
            driver.findElement(By.id("ctl00_cphRightColumn_ucLogin_btnLogin")).click();

            calendar = Calendar.getInstance();
            txtMsg = txtMsg + sdf.format(calendar.getTime()) +" - Logging in \n";
            logArea.setText(txtMsg);
            Thread.sleep(500);
            calendar = Calendar.getInstance();
            if(driver.getCurrentUrl().contains("popmundo.com/World/Popmundo.aspx"))
                txtMsg = txtMsg + sdf.format(calendar.getTime()) +" - Logged in\n";
            else{
                txtMsg = txtMsg + sdf.format(calendar.getTime()) +" - ERROR Something went wrong! (Possibly Username or Password is incorrect)\n ";
                throw new Exception("Session ID is null.");
            }
            logArea.setText(txtMsg);

            while(true){
                Thread.sleep(500);
                driver.findElement(By.linkText("City")).click();
                Thread.sleep(500);
                driver.findElement(By.linkText("Locales")).click();
                Thread.sleep(500);
                driver.findElement(By.id("ctl00_cphLeftColumn_ctl00_ddlLocaleType")).sendKeys(String.valueOf(locatesBox.getSelectedItem()));
                driver.findElement(By.id("ctl00_cphLeftColumn_ctl00_btnFind")).click();
                Thread.sleep(500);
                WebElement simpleTable = driver.findElement(By.id("tablelocales"));
                Thread.sleep(500);
                List<WebElement> rows = simpleTable.findElements(By.tagName("tr"));
                Thread.sleep(500);
                WebElement selectedLocal = rows.get(localNumber).findElements(By.tagName("td")).get(0);
                Thread.sleep(500);

                if(localNumber != rows.size())
                    localNumber++;
                else
                    break;
                calendar = Calendar.getInstance();
                txtMsg = txtMsg + sdf.format(calendar.getTime()) +" - " + (localNumber -1) +". I'm going to  \""+ selectedLocal.findElement(By.tagName("a")).getText()+"\"\n";
                logArea.setText(txtMsg);

                selectedLocal.findElement(By.tagName("a")).click();
                try {
                    if (driver.findElement(By.id("ctl00_cphLeftColumn_ctl00_divGraffiti")) != null){
                        calendar = Calendar.getInstance();
                        txtMsg = txtMsg + sdf.format(calendar.getTime()) + " - \t" + "Wall painted before!!\n";
                        logArea.setText(txtMsg);
                        continue;
                    }
                }catch (Exception e){

                }
                driver.findElement(By.linkText("Move to locale")).click();
                driver.findElement(By.linkText("Character")).click();
                int numberOfTrials = 1;
                while(true){
                    String result = paintWall();
                    calendar = Calendar.getInstance();
                    txtMsg = txtMsg + sdf.format(calendar.getTime()) + " - \t" + numberOfTrials+". try\n";
                    logArea.setText(txtMsg);

                    if(result.contains("Fsssssssst!"))
                    {
                        calendar = Calendar.getInstance();
                        txtMsg = txtMsg + sdf.format(calendar.getTime()) + " - \t" + result+ "\n";
                        completedGraffiti++;
                        txtMsg = txtMsg + sdf.format(calendar.getTime()) + " - I've painted "+ completedGraffiti +"  places so far"+ "\n";
                        logArea.setText(txtMsg);

                        break;
                    }
                    numberOfTrials++;
                    int seconds = 181000 + randomDelay.nextInt(50000);
                    calendar = Calendar.getInstance();
                    txtMsg = txtMsg + sdf.format(calendar.getTime()) + " - \t" + "Waiting for " + seconds/1000 +" seconds"+ "\n";
                    logArea.setText(txtMsg);

                    Thread.sleep(seconds);
                }
                int seconds = 181000 + randomDelay.nextInt(50000);
                calendar = Calendar.getInstance();
                txtMsg = txtMsg + sdf.format(calendar.getTime()) + " - Waiting for " + seconds/1000 +" seconds"+ "\n";
                logArea.setText(txtMsg);

                Thread.sleep(seconds);
            }
            calendar = Calendar.getInstance();
            txtMsg = txtMsg + sdf.format(calendar.getTime()) + " - All locales are visited.\n";
            logArea.setText(txtMsg);
        } catch (Exception e) {
            calendar = Calendar.getInstance();
            if(e.getMessage().contains("Unable to find element with id 'tablelocales'")){
                txtMsg = txtMsg + sdf.format(calendar.getTime()) + " - Not found any local\n";
            }else if(e.getMessage().contains("Session ID is null."))
            {

            } else if(e.getMessage().contains("Error 404: Not Found"))
            {

            } else{
                txtMsg = txtMsg + sdf.format(calendar.getTime()) + " - ERROR\n";
            }
        }finally {
            calendar = Calendar.getInstance();
            txtMsg = txtMsg + sdf.format(calendar.getTime()) + " - Stopping process\n";
            logArea.setText(txtMsg);
            if(tearFlag)
            {
                tearDown();
                tearFlag = true;
            }
            textUsername.setEnabled(true);
            fieldPassword.setEnabled(true);
            locatesBox.setEnabled(true);
            buttonLogin.setText("Start");
        }
    }
}
