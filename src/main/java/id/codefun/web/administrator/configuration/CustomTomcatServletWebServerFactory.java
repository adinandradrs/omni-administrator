package id.codefun.web.administrator.configuration;

import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.catalina.Context;
import org.apache.tomcat.util.scan.StandardJarScanFilter;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.util.StringUtils;

public class CustomTomcatServletWebServerFactory extends TomcatServletWebServerFactory  {
    
    @Override
    protected void postProcessContext(Context context) {
        Set<String> pattern = new LinkedHashSet<>();
        pattern.add("jaxb*.jar");
        StandardJarScanFilter filter = new StandardJarScanFilter();
        filter.setTldSkip(StringUtils.collectionToCommaDelimitedString(pattern));
        ((StandardJarScanner) context.getJarScanner()).setJarScanFilter(filter);
    }
    
}
