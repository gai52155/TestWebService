package myTest;

import https.rdws_rd_go_th.servicerd3.checktinpinservice.ArrayOfAnyType;
import https.rdws_rd_go_th.servicerd3.checktinpinservice.Checktinpinservice;
import https.rdws_rd_go_th.servicerd3.checktinpinservice.Tinpin;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.xml.ws.WebServiceRef;


/**
 *
 * @author Katawut
 */

@WebService(serviceName = "TestCitizenID")
public class TestCitizenID
{
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/rdws.rd.go.th/serviceRD3/checktinpinservice.asmx.wsdl")
    private Checktinpinservice service;
    /**
     * Web service operation
     */
    @WebMethod(operationName = "CheckCitizenID")
    public String CheckCitizenID(@WebParam(name = "ID") String ID)  throws NoSuchAlgorithmException, KeyManagementException 
    {
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, new TrustManager[] 
        {
           new TrustAllX509TrustManager()
        }, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier( new HostnameVerifier()
        {
            public boolean verify(String string,SSLSession ssls) 
            {
                return true;
            }
        });
        
        
        Response report = new Response();
        if(ID.length()!=13)
        {
            report.message = "ใส่รหัสบัตรประชาชนมาไม่ครบ";
            report.status  = "FAIL";
        }
        else
        {
            Tinpin get1 = serviceTIN("anonymous", "anonymous", ID);
            String g = null;
            ArrayOfAnyType n = get1.getVIsExist();
            String m = n.getAnyType().toString();
            if(m.equals("[Yes]"))
            {
                report.message = m;
                report.status = "YES";
            }
            else
            {
                report.message = "รหัสนี้ ไม่ใช่ผู้ที่เสียภาษี หรือ ไม่มีตัวตน";
                report.status = "No";
            }
        }
        return report.print();
    }

    private Tinpin serviceTIN(java.lang.String username, java.lang.String password, java.lang.String tin){
        https.rdws_rd_go_th.servicerd3.checktinpinservice.ChecktinpinserviceSoap port;
        port = service.getChecktinpinserviceSoap();
        return port.serviceTIN(username, password, tin);
    }
}
