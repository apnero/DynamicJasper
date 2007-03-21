package ar.com.fdvs.dj.output;

import com.opensymphony.webwork.views.jasperreports.JasperReportConstants;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.JRExporter;

import java.util.HashMap;
import java.util.Map;

import ar.com.fdvs.dj.core.layout.AbstractLayoutManager;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.core.layout.ListLayoutManager;

/**
 * @author Alejandro Gomez
 *         Date: Feb 22, 2007
 *         Time: 4:44:50 PM
 */
public class FormatInfoRegistry {

    private static final Map FORMAT_INFO = new HashMap();
    static {
        FORMAT_INFO.put(JasperReportConstants.FORMAT_CSV, new FormatInfo("text/plain", JRCsvExporter.class, ClassicLayoutManager.class));
        FORMAT_INFO.put(JasperReportConstants.FORMAT_HTML, new FormatInfo("text/html", JRHtmlExporter.class, ClassicLayoutManager.class));
        FORMAT_INFO.put(JasperReportConstants.FORMAT_PDF, new FormatInfo("application/pdf", JRPdfExporter.class, ClassicLayoutManager.class));
        FORMAT_INFO.put(JasperReportConstants.FORMAT_XLS, new FormatInfo("application/vnd.ms-excel", JRXlsExporter.class, ListLayoutManager.class));
        FORMAT_INFO.put(JasperReportConstants.FORMAT_XML, new FormatInfo("text/xml", JRXmlExporter.class, ClassicLayoutManager.class));
    }

    private static final FormatInfoRegistry INSTANCE = new FormatInfoRegistry();

    public String getContentType(final String _format) {
        checkFormat(_format);
        return ((FormatInfo)FORMAT_INFO.get(_format)).getContentType();
    }

    public JRExporter getExporter(final String _format) {
        checkFormat(_format);
        final JRExporter exporter = ((FormatInfo)FORMAT_INFO.get(_format)).getExporterInstance();
        exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE);
        exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
        exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
        exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
        exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
        return exporter;
    }

    public AbstractLayoutManager getLayoutManager(final String _format) {
        checkFormat(_format);
        return ((FormatInfo)FORMAT_INFO.get(_format)).getLayoutManagerInstance();
    }

    private static void checkFormat(final String _format) {
        if (!FORMAT_INFO.containsKey(_format)) {
            throw new IllegalArgumentException("Unsupported format: " + _format);
        }
    }

    public static FormatInfoRegistry getInstance() {
        return INSTANCE;
    }

    private static class FormatInfo {

        private String contentType;
        private Class exporterClass;
        private Class layoutManagerClass;

        private FormatInfo(final String _contentType, final Class _exporterClass, final Class _layoutManagerClass) {
            contentType = _contentType;
            exporterClass = _exporterClass;
            layoutManagerClass = _layoutManagerClass;
        }

        public String getContentType() {
            return contentType;
        }

        public JRExporter getExporterInstance() {
            try {
                return (JRExporter)exporterClass.newInstance();
            } catch (Exception ex) {
                return null;
            }
        }

        public AbstractLayoutManager getLayoutManagerInstance() {
            try {
                return (AbstractLayoutManager)layoutManagerClass.newInstance();
            } catch (Exception ex) {
                return null;
            }
        }
    }
}