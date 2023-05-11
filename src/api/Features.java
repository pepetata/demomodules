package api;

import java.io.*;
import java.util.*;
// http://www.jdom.org/downloads/
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;

import Aladdin.Hasp;
import Aladdin.HaspStatus;

public class Features {
    // private static final String productID = "1000";

    private static final String vendorCode = new String(
            "AzIceaqfA1hX5wS+M8cGnYh5ceevUnOZIzJBbXFD6dgf3tBkb9cvUF/Tkd/iKu2fsg9wAysYKw7RMA" +
            "sVvIp4KcXle/v1RaXrLVnNBJ2H2DmrbUMOZbQUFXe698qmJsqNpLXRA367xpZ54i8kC5DTXwDhfxWT" +
            "OZrBrh5sRKHcoVLumztIQjgWh37AzmSd1bLOfUGI0xjAL9zJWO3fRaeB0NS2KlmoKaVT5Y04zZEc06" +
            "waU2r6AU2Dc4uipJqJmObqKM+tfNKAS0rZr5IudRiC7pUwnmtaHRe5fgSI8M7yvypvm+13Wm4Gwd4V" +
            "nYiZvSxf8ImN3ZOG9wEzfyMIlH2+rKPUVHI+igsqla0Wd9m7ZUR9vFotj1uYV0OzG7hX0+huN2E/Id" +
            "gLDjbiapj1e2fKHrMmGFaIvI6xzzJIQJF9GiRZ7+0jNFLKSyzX/K3JAyFrIPObfwM+y+zAgE1sWcZ1" +
            "YnuBhICyRHBhaJDKIZL8MywrEfB2yF+R3k9wFG1oN48gSLyfrfEKuB/qgNp+BeTruWUk0AwRE9XVMU" +
            "uRbjpxa4YA67SKunFEgFGgUfHBeHJTivvUl0u4Dki1UKAT973P+nXy2O0u239If/kRpNUVhMg8kpk7" +
            "s8i6Arp7l/705/bLCx4kN5hHHSXIqkiG9tHdeNV8VYo5+72hgaCx3/uVoVLmtvxbOIvo120uTJbuLV" +
            "TvT8KtsOlb3DxwUrwLzaEMoAQAFk6Q9bNipHxfkRQER4kR7IYTMzSoW5mxh3H9O8Ge5BqVeYMEW36q" +
            "9wnOYfxOLNw6yQMf8f9sJN4KhZty02xm707S7VEfJJ1KNq7b5pP/3RjE0IKtB2gE6vAPRvRLzEohu0" +
            "m7q1aUp8wAvSiqjZy7FLaTtLEApXYvLvz6PEJdj4TegCZugj7c8bIOEqLXmloZ6EgVnjQ7/ttys7VF" +
            "ITB3mazzFiyQuKf4J6+b/a/Y");

    private static final String scope1 = new String(
            "<haspscope />\n");
    // "<haspscope><product id=\"" + getProductid() + "\"/></haspscope>\n");

    private static final String view = new String(
            "<haspformat root=\"hasp_info\">\n" +
                    "  <feature>\n" +
                    "    <attribute name=\"id\" />\n" +
                    "    <attribute name=\"usable\" />\n" +
                    "  </feature>\n" +
                    "</haspformat>\n");

    private Hasp hasp;

    private int status;
    public final long MODULE1 = 1100;
    public final long MODULE2 = 1200;
    public final long MODULE3 = 1300;
    public final long FUNCTION31 = 1210;
    public final long FUNCTION32 = 1320;

    public Features(long feature) {
        setHasp(new Hasp(feature));
    }

    public Features() {
    }

    public boolean login() {
        getHasp().login(getVendorcode());
        int status = getHasp().getLastError();
        if (status != HaspStatus.HASP_STATUS_OK) {
            System.out.println("Error to login: " + status);
            return false;
        }
        return true;
    }

    public void logout() {
        getHasp().logout();
        int status = getHasp().getLastError();
        if (status != HaspStatus.HASP_STATUS_OK) {
            // System.out.println("Error to logout: " + status);
            return;
        }
        return;
    }

    public boolean isFeatureAvailable(final boolean isAppProtected, final String feature)
            throws JDOMException, IOException {
        if (isAppProtected == false)
            return true;
        boolean available = false;
        String infos = getInfo(getScope1(), getView(), getVendorcode());
        int status = getHasp().getLastError();
        if (status != HaspStatus.HASP_STATUS_OK)
            return available;

        // parse xml
        SAXBuilder saxBuilder = new SAXBuilder();
        InputStream stream = new ByteArrayInputStream(infos.getBytes("UTF-8"));
        Document document = saxBuilder.build(stream);

        Element classElement = document.getRootElement();
        List<Element> content = classElement.getChildren();
        for (int i = 0; i < content.size(); i++) {
            Element feat = content.get(i);
            Attribute id = feat.getAttribute("id");
            String featureValue = id.getValue();
            Attribute usable = feat.getAttribute("usable");
            String usableValue = usable.getValue();
            if (featureValue.equals(feature)) {
                if (usableValue.equals("true")) {
                    available = true;
                    break;
                }
            }
        }
        return available;
    }

    public boolean OLDisFeatureAvailable(final boolean isAppProtected, final String feature)
            throws JDOMException, IOException {
        if (isAppProtected == false)
            return true;
        boolean available = false;
        String infos = getInfo(getScope1(), getView(), getVendorcode());
        int status = getHasp().getLastError();
        if (status != HaspStatus.HASP_STATUS_OK)
            return available;

        // parse xml
        SAXBuilder saxBuilder = new SAXBuilder();
        InputStream stream = new ByteArrayInputStream(infos.getBytes("UTF-8"));
        Document document = saxBuilder.build(stream);

        Element classElement = document.getRootElement();
        List<Element> content = classElement.getChildren();
        for (int i = 0; i < content.size(); i++) {
            Element feat = content.get(i);
            Attribute id = feat.getAttribute("id");
            String featureValue = id.getValue();
            Attribute usable = feat.getAttribute("usable");
            String usableValue = usable.getValue();
            if (featureValue.equals(feature)) {
                if (usableValue.equals("true")) {
                    available = true;
                    break;
                }
            }
        }

        return available;
    }

    public String getInfo(final String scope, final String view, final String vendorCode) {

        String infos = getHasp().getInfo(scope, view, vendorCode);
        int status = getHasp().getLastError();

        switch (status) {
            case HaspStatus.HASP_STATUS_OK:
                /* use the info you received ... */
                // System.out.println("OK\n" + infos);
                break;
            case HaspStatus.HASP_INV_FORMAT:
                System.out.println("invalid XML info format\n");
                break;
            case HaspStatus.HASP_INV_SCOPE:
                System.out.println("invalid XML scope\n");
                break;
            default:
                System.out.println("hasp_get_info failed with status " + status);
        }
        return infos;
    }

    public void hasp_login() {
        /**********************************************************************
         * hasp_login
         * establish a context for Sentinel services
         */

        System.out.print("login to default feature         : ");

        /* login feature 0 */
        /* this default feature is available on any key */
        /* search for local and remote Sentinel key */
        hasp.login(vendorCode);
        status = hasp.getLastError();

        switch (status) {
            case HaspStatus.HASP_STATUS_OK:
                // System.out.println("OK");
                break;
            case HaspStatus.HASP_FEATURE_NOT_FOUND:
                System.out.println("no Sentinel DEMOMA key found");
                break;
            case HaspStatus.HASP_HASP_NOT_FOUND:
                System.out.println("Sentinel key not found");
                break;
            case HaspStatus.HASP_OLD_DRIVER:
                System.out.println("outdated driver version or no driver installed");
                break;
            case HaspStatus.HASP_NO_DRIVER:
                System.out.println("Sentinel key not found");
                break;
            case HaspStatus.HASP_INV_VCODE:
                System.out.println("invalid vendor code");
                break;
            default:
                System.out.println("login to default feature failed");
        }

    }

    public Hasp getHasp() {
        return hasp;
    }

    public void setHasp(Hasp hasp) {
        this.hasp = hasp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static String getVendorcode() {
        return vendorCode;
    }

    public static String getScope1() {
        return scope1;
    }

    public static String getView() {
        return view;
    }

}
