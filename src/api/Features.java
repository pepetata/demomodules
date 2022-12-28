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
        "7QLOrS9a/S+MIJaFuRS+bVKFlfcfDnyXhcVyePiUDpdtzzCAzL+jmQO2t1cMFMQuroQh9bCM8Bkx6/Lo" + 
        "tziQhp1PfdLZOsa7ZKYLFtRbV07EImnGm0+auR/eZtucPtOfY8v+F+jY2um5EFdGRods4/6MesejkUy3" + 
        "wKLi/ZLmAI4aIj9ERYYJVwjRK76SRa8MMvyOUwjInsxIW701JbDn7W+Zce1eSsVQIo1WoDofejgasx4L" + 
        "Yduf5+cI9GPOiLMZCBFmalIGcBnQrGOOboVUyqFwVmPxFM7nGX3ECkJ9R9pp58o2imMnkJ1VLRxlGzmD" + 
        "+5XTQo01FfUSyCChiWiCcduahRlc1cTT1xzcCwpu8FN/xyZjYVU+PDgWyO5nbG1fw3A4Bv9gYhcxqK6X" + 
        "p9aHqmk4f29xGFrqZcT7Ak4TeW0QXheye2oqjsFLFNmHBHhbO0QpKn79NpaiVts9G/CVuGELXY8UMHpC" + 
        "GxqWPXeGB5XL01TQMHsFsmKxdA2wtw3jgDWq2/7QpI3dPcbQdsORhllcMH0gTpRqBEPcYeKbRkZXnetc" + 
        "yX53USINhVeS40wfKat5leEJ1DupBzCyyFaqcHEYwd5mhlyz5fGrs/dDE5nwCOXzXrWjAyx+PHIC7iEp" + 
        "E1687ePIuY4sSpB/pbigz+jHCJ4S0c1rbLrsdkll03ewdjB1+6/5e8TxoP4eeC1+gD/s1m2rXlo6Rh8h" + 
        "4g3wZhUNyJW0Yp8yC8cKayJkK807I49JIfR6+Lq/TMoGwqbjO2r17+4IQK91ZA4/qFw5+7vLmfTC/Y87" + 
        "WGRaVXVqIAu6rEsNm3n4FYjGC/xRvj/vgvJZbQxzs9aEDtzb80hnBIcjA0PFEnAKAZpLRpfj2wDXLiuY" + 
        "3xIyhT2OMtmn3x1m3+czgX2IIowYoWOsnsZjNNc35yTkWPGGgLSH9391zwuYhvNGJkXTVnYFh9zEJDdz" + 
        "fncvArvCrRYw2IUV6AmBjw==");

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
