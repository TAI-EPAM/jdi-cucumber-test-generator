package com.epam.test_generator.file_generator;

import com.epam.test_generator.controllers.caze.response.CaseDTO;
import com.epam.test_generator.controllers.suit.response.SuitDTO;
import com.epam.test_generator.services.SuitService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.stereotype.Component;


/**
 * This class is used to create information about suit with all test cases according to specified template.
 * For text generation FreeMaker API is used. {@Link Configuration} object is used for configure information about
 * template that is used for text creation and {@Link Template} object stores already parsed template that is ready
 * for work.
 *
 */
@Component
public class FileGenerator {

    public String generate(SuitDTO suit, List<CaseDTO> cases) throws IOException {

        if (suit == null || cases == null) {
            throw new NullPointerException(suit == null ? "suit is null" : "cases list is null");
        }

        Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setLocale(Locale.getDefault());

        configuration.setClassForTemplateLoading(SuitService.class, "/templates/");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        Map<String, Object> input = new HashMap<>();

        cases.removeIf(c -> c.getSteps() == null || c.getSteps().size() == 0);
        suit.setCases(cases);
        input.put("suit", suit);

        Template template = configuration.getTemplate("featureFileTemplate.ftl");

        try (StringWriter stringWriter = new StringWriter()) {
            template.process(input, stringWriter);
            stringWriter.flush();
            return stringWriter.toString();
        } catch (TemplateException e) {
            e.printStackTrace();
            return "";
        }
    }
}
