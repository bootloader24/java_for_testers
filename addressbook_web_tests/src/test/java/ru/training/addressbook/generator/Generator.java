package ru.training.addressbook.generator;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import ru.training.addressbook.common.CommonFunctions;
import ru.training.addressbook.model.ContactData;
import ru.training.addressbook.model.GroupData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Generator {

    @Parameter(names={"--type", "-t"})
    String type;

    @Parameter(names={"--output", "-o"})
    String output;

    @Parameter(names={"--format", "-f"})
    String format;

    @Parameter(names={"--count", "-n"})
    int count;

    public static void main(String[] args) throws IOException {
        var generator = new Generator();
        JCommander.newBuilder()
                .addObject(generator)
                .build()
                .parse(args);
        generator.run();
    }

    private void run() throws IOException {
        var data = generate();
        save(data);
    }

    private Object generate() {
        if ("groups".equals(type)) {
            return generateGroups();
        } else if ("contacts".equals(type)) {
            return generateContacts();
        } else {
            throw new IllegalArgumentException("Unknown data type: " + type);
        }
    }

    private Object generateData(Supplier<Object> dataSupplier) {
        return Stream.generate(dataSupplier).limit(count).collect(Collectors.toList());
    }

    private Object generateGroups() {
        return generateData(() -> new GroupData()
                .withName(CommonFunctions.randomString(10))
                .withHeader(CommonFunctions.randomString(10))
                .withFooter(CommonFunctions.randomString( 10)));
    }

    private Object generateContacts() {
        return generateData(() ->new ContactData()
                    .withLastname(CommonFunctions.randomString(10))
                    .withFirstname(CommonFunctions.randomString(10))
                    .withAddress(CommonFunctions.randomString(10))
                    .withEmail(CommonFunctions.randomString( 10))
                    .withEmail2("")
                    .withEmail3("")
                    .withPhoneHome(CommonFunctions.randomString( 10))
                    .withPhoneMobile("")
                    .withPhoneWork("")
                    .withPhoneSecondary("")
                    .withPhoto(CommonFunctions.randomFile("src/test/resources/images")));
    }

    private void save(Object data) throws IOException {
        if ("json".equals(format)){
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
//            first variant:
//            mapper.writeValue(new File(output), data);

//            second variant:
            var json = mapper.writeValueAsString(data);
            try (var writer = new FileWriter(output)){
                writer.write(json);
            }
        } else if ("yaml".equals(format)){
            var mapper = new YAMLMapper();
            mapper.writeValue(new File(output), data);
        } else if ("xml".equals(format)){
            var mapper = new XmlMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(new File(output), data);
        } else {
            throw new IllegalArgumentException("Unknown data format: " + format);
        }
    }
}
