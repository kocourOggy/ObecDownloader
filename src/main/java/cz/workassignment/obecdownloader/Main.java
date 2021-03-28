/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.workassignment.obecdownloader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 *
 * @author stepaiv3
 */
public class Main {

    static public final Map<String, String> mapperXMLObecFields = Map.of(
            "obi:Kod", "Kod",
            "obi:Nazev", "Nazev"
    );
    static public final String XMLClassObecName = "vf:Obec";

    static public final Map<String, String> mapperXMLCastObceFields = Map.of(
            "coi:Kod", "Kod",
            "coi:Nazev", "Nazev",
            "obi:Kod", "KodObce"
    );
    static public final String XMLClassCastObceName = "vf:CastObce";

    static public XMLMapper<Obec> XMLMapperObec = new XMLMapper<Obec>(XMLClassObecName, mapperXMLObecFields);
    static public XMLMapper<CastObce> XMLMapperCastObce = new XMLMapper<CastObce>(XMLClassCastObceName, mapperXMLCastObceFields);

    static public final String dbUrl = "jdbc:sqlite:obec.db";
    static public final String dbUrlInMemory = "jdbc:sqlite::memory:";

    static public Path downloadFile(String urlString) throws MalformedURLException, IOException {
        URL url = new URL(urlString);
        Path pathToWrite = Paths.get(urlString).getFileName();
        try ( var inputStream = url.openStream()) {
            Files.copy(inputStream, pathToWrite, StandardCopyOption.REPLACE_EXISTING);
        }
        return pathToWrite;
    }

    static public Path unzipSingleFile(Path pathToZip) throws FileNotFoundException, IOException {
        Path pathToWrite = null;
        try ( var zipInputStream = new ZipInputStream(Files.newInputStream(pathToZip))) {
            var zipEntry = zipInputStream.getNextEntry();
            if (zipEntry != null && zipEntry.isDirectory() != true) {
                pathToWrite = Paths.get(zipEntry.getName());
                Files.copy(zipInputStream, pathToWrite, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        return pathToWrite;
    }

    static public Path downloadFileFromURLAndUnzip(String urlString) throws FileNotFoundException, IOException {
        var pathToFile = downloadFile(urlString);
        pathToFile = unzipSingleFile(pathToFile);
        return pathToFile;
    }

    static public void main(String[] args) throws Exception {
        var url = "https://vdp.cuzk.cz/vymenny_format/soucasna/20201231_OB_573060_UZSZ.xml.zip";
        var fileToProcess = downloadFileFromURLAndUnzip(url);

        var doc = DocumentParser.createDocument(fileToProcess);
        List<Obec> listOfObec = DocumentParser.parseObjects(doc, new Obec(), XMLMapperObec);
        List<CastObce> listOfCastObce = DocumentParser.parseObjects(doc, new CastObce(), XMLMapperCastObce);

        try {
            var obecDatabase = new ObecDatabase(dbUrl);
            obecDatabase.insertAllObec(listOfObec);
            obecDatabase.insertAllCastObceIfObecExist(listOfCastObce);
            obecDatabase.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
