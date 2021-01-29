import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String fileName = "data.xml";

        List<Employee> list = parseXML(fileName);

        String json = listToJson(list);

        writeString(json);
    }

    private static List<Employee> parseXML(String fileName) {
        List<Employee> employeeList = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(fileName));
            Node root = doc.getDocumentElement();
            NodeList employeeNodes = root.getChildNodes();
            for (int i = 0; i < employeeNodes.getLength(); i++) {
                if (employeeNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element elementEmployee = (Element) employeeNodes.item(i);
                    elementEmployee.getElementsByTagName("firstName").item(0).getTextContent();

                    Employee newEmployee = new Employee(
                            Long.parseLong(elementEmployee.getElementsByTagName("id").item(0).getTextContent()),
                            elementEmployee.getElementsByTagName("firstName").item(0).getTextContent(),
                            elementEmployee.getElementsByTagName("lastName").item(0).getTextContent(),
                            elementEmployee.getElementsByTagName("country").item(0).getTextContent(),
                            Integer.parseInt(elementEmployee.getElementsByTagName("age").item(0).getTextContent())
                    );

                    System.out.println(newEmployee);
                    employeeList.add(newEmployee);
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return employeeList;
    }

    private static void writeString(String str) {
        try (FileWriter fileWriter = new FileWriter("data.json")) {
            fileWriter.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String listToJson(List<Employee> list) {
        Gson gson = new Gson();
        String json = "";

        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        try {
            json = gson.toJson(list, listType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return json;
    }
}
