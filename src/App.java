
import java.util.Locale;
import java.util.ResourceBundle;

import api.Features;
import gui.MainScreen;

/*
 * Feature 1000 - protected App
 * 
 * Featura 1100 - Module 1
 * Featura 1200 - Module 2
 * Featura 1300 - Module 3
 * 
 * Featura 1310 - Function 1 in Module 3
 * Featura 1320 - Function 2 in Module 3
 */
public class App {
    ResourceBundle bundle;

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            usage();
        }
        Locale lang = getLang(args);
        boolean isAppProtected = getMode(args);
        System.out.println(isAppProtected);

        // Features features = new Features();
        // boolean isFeature10Available = features.isFeatureAvailable("10");
        // if (isFeature10Available)
        // System.out.println("Feature 10 is available\n");
        // else
        // System.out.println("Feature 10 is NOT available\n");
        // System.out.println("OK\n");

        new MainScreen(lang, isAppProtected);

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
        }
        if (lang == null) {
            usage();
        }
        return lang;
    }

    public static void usage() {
        System.out.println("Usage: XXXXXX   language   mode");
        System.out.println("  where: language can be:");
        System.out.println("            en = English");
        System.out.println("            pt = Portugues");
        System.out.println("            es = Spanish");
        System.out.println("         mode can be:");
        System.out.println("            np = NOT protected");
        System.out.println("            p  = protected");
        System.out.println();
        System.exit(0);
    }
}
