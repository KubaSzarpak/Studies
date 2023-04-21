/**
 *
 *  @author Szarpak Jakub S25511
 *
 */

package zad1;


import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Tools {
    public static Options createOptionsFromYaml(String fileName) {

        Yaml yaml = new Yaml();

        try (
        FileInputStream reader = new FileInputStream(new File(fileName))
        ){
             Map<String, Object> mapa = yaml.load(reader);

            Options options = new Options((String) mapa.get("host"), (Integer) mapa.get("port"), (Boolean) mapa.get("concurMode"), (Boolean)mapa.get("showSendRes"), (Map<String, List<String>>)mapa.get("clientsMap"));

            return options;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

