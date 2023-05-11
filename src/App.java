
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Properties;
import java.io.InputStream;

import javax.swing.*;

import gui.MainScreen;
import api.Features;
import api.EMS;
import api.Feature;

public class App {

    // This will be updated from configuration file & will represent ems URL
    private static String serverUrl = "";

    public static void main(String[] args) throws Exception {

        if (args.length < 2) {
            usage();
        }
        Locale lang = getLang(args);
        boolean isAppProtected = getMode(args);
        boolean custom = getCustom(args);
        // boolean custom = true;

        EMS ems = new EMS();
        ems.loadProperties();
        // System.out.println("Attempting Login........");
        boolean verified = ems.verifyLogin();
        if (verified) {
            // System.out.println("Login Successfull.........");
        } else {
            throw new Exception("Not able to login");
        }

        final long demoFeature = 1001;
        Features features = new Features(demoFeature);
        ResourceBundle bundle = ResourceBundle.getBundle("resources.messages", lang);

        boolean isFeatureAvailable = isAppProtected ? features.login() : false;
        // System.out.println("isAppProtected " + isAppProtected);
        // System.out.println("isFeatureAvailable "+isFeatureAvailable);
        if (isFeatureAvailable) {
            // System.out.println("custom "+custom);
            // get Feature Data
            Feature feature = ems.getFeatureDetails(demoFeature + "");
            if (feature != null) {
                // System.out.println("elemt ====== : " + feature.getFeatureIdentifier());
                // System.out.println("elemt ====== : " + feature.getFeatureName());
                // System.out.println("elemt ====== : " + feature.getFeatureDescription());
                if (custom)
                    JOptionPane.showMessageDialog(null, feature.getFeatureDescription());
                else
                    JOptionPane.showMessageDialog(null, bundle.getString("evaluation.text"));
            }else
            JOptionPane.showMessageDialog(null, bundle.getString("evaluation.text"));
        }

        try {
            new MainScreen(lang, isAppProtected, custom, features);
            // features.logout();
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            System.out.println("deu erro caraca ==============================");
            e1.printStackTrace();
        }

    }

    public static boolean getMode(String[] args) {
        String mode = null;
        switch (args[1]) {
            case "np":
                mode = args[1];
                break;
            case "p":
                mode = args[1];
                break;
        }
        if (mode == null) {
            usage();
        }
        return mode.equals("p");
    }

    public static Locale getLang(String[] args) {
        Locale lang = null;
        switch (args[0]) {
            case "pt":
                lang = new Locale("pt", "BR");
                break;
            case "es":
                lang = new Locale("es", "MX");
                break;
            case "en":
                lang = Locale.US;
                break;
            default:
                Pattern pattern = Pattern.compile("[d|D]([a-z]|[0-9])", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(args[0]);
                boolean matchFound = matcher.find();
                if (matchFound)
                    lang = new Locale(matcher.group(), "MX");

                break;
        }
        if (lang == null) {
            usage();
        }
        return lang;
    }

    public static boolean getCustom(String[] args) {
        String custom = "";
        if (args.length > 2)
            custom = args[2];
        return !custom.isEmpty();
    }

    public static void usage() {
        System.out.println("Usage: XXXXXX   language   mode (custom)");
        System.out.println("  where: language can be:");
        System.out.println("            en = English");
        System.out.println("            pt = Portugues");
        System.out.println("            es = Spanish");
        System.out.println("            dX = Demo number");
        System.out.println("         mode can be:");
        System.out.println("            np = NOT protected");
        System.out.println("            p  = protected");
        System.out.println("         (custom) is optional and can be:");
        System.out.println("            XXXXXXXXXXXXXXXXX TODO");
        System.out.println();
        System.exit(0);
    }
}
