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
                    Employee newEmployee = new Employee();
                    //Получаем ноды, содержащиеся в Employee
                    NodeList employeeNode = (NodeList) employeeNodes.item(i);

                    //Пробегаем через два
                    for (int j = 1; j < employeeNode.getLength(); j = j + 2) {
                        switch (employeeNode.item(j).getNodeName()) {
                            case "id":
                                newEmployee.setId(Long.parseLong(employeeNode.item(j).getChildNodes().item(0).getNodeValue()));
                                break;
                            case "firstName":
                                newEmployee.setFirstName(employeeNode.item(j).getChildNodes().item(0).getNodeValue());
                                break;
                            case "lastName":
                                newEmployee.setLastName(employeeNode.item(j).getChildNodes().item(0).getNodeValue());
                                break;
                            case "country":
                                newEmployee.setCountry(employeeNode.item(j).getChildNodes().item(0).getNodeValue());
                                break;
                            case "age":
                                newEmployee.setAge(Integer.parseInt(employeeNode.item(j).getChildNodes().item(0).getNodeValue()));
                                break;
                        }
                    }
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
