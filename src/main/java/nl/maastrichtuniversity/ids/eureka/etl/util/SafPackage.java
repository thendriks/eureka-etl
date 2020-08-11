package nl.maastrichtuniversity.ids.eureka.etl.util;

import nl.maastrichtuniversity.ids.eureka.etl.domain.KnowledgeObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/** Modified version of SAFPackage class found at
 *  https://github.com/DSpace-Labs/SAFBuilder/blob/master/src/main/java/safbuilder/SAFPackage.java
 */
//TODO: Refactor/clean to leave only necessary parts for object registration.
public class SafPackage {
    private String seperatorRegex = "\\|\\|";   // Using double pipe || to separate multiple values in a field.
    private Path outputFilePath;
    private Path bitstreamFolderPath;
    private String storeNumber;

    /**
     * Constructor that allows you to set output directory
     * @param outputFilePath Custom directory name that SAF output goes to
     * @param bitstreamFolderPath Directory containing the binary data
     */
    public SafPackage(Path outputFilePath, Path bitstreamFolderPath) {
        this.outputFilePath = outputFilePath;
        this.bitstreamFolderPath = bitstreamFolderPath;
    }

    /**
     * open metafile
     * foreach(metarows as metarow)
     * makeDirectory(increment)
     * copy filenames into directory
     * make contents file with entries for each filename
     * foreach(metarow.columns as column)
     * add meta entry to metadata xml
     *
     * @param knowledgeObjects List of knowledge objects to package
     * @throws java.io.IOException If the files can't be found or created.
     */
    public void processMetaPack(List<KnowledgeObject> knowledgeObjects) throws IOException {         // For Reporting file usage
        Files.createDirectories(outputFilePath);
        processMetaBody(knowledgeObjects);
    }

    /**
     * Method to process the content/body of the metadata csv.
     * Delegate the work of processing each row to other methods.
     * Does not process the header.
     *
     * @throws IOException If the CSV can't be found or read
     * @param knowledgeObjects
     */
    private void processMetaBody(List<KnowledgeObject> knowledgeObjects) throws IOException {
        for(int i = 0; i < knowledgeObjects.size(); i++) {
            KnowledgeObject knowledgeObject = knowledgeObjects.get(i);
            processMetaBodyRow(knowledgeObject, i);
        }
    }

    private void processMetaBodyRow(KnowledgeObject knowledgeObject, int objectNumber) {
        String currentItemDirectory = makeNewDirectory(objectNumber);
        String dcFileName = currentItemDirectory + "/dublin_core.xml";
        File contentsFile = new File(currentItemDirectory + "/contents");

        try {
            BufferedWriter contentsWriter = new BufferedWriter(new FileWriter(contentsFile));
            OutputXml xmlWriter = new OutputXml(dcFileName);
            xmlWriter.start();
            Map<String, OutputXml> nonDCWriters = new HashMap<>();
            Path filePath = bitstreamFolderPath.resolve(knowledgeObject.getFileName());
            processMetaBodyRowFile(contentsWriter, filePath);
            for (Map.Entry<String, String> optionalMetaDataField : knowledgeObject.getOptionalMetaData().entrySet()) {
                String[] dublinPieces = optionalMetaDataField.getKey().split("\\.");
                if (dublinPieces.length < 2) {
                    // strange field, skip
                    continue;
                }
                String schema = dublinPieces[0];
                if (schema.contentEquals("dc")) {
                    processMetaBodyRowField(optionalMetaDataField.getKey(), optionalMetaDataField.getValue(), xmlWriter);
                } else {
                    if (!nonDCWriters.containsKey(schema)) {
                        OutputXml schemaWriter = new OutputXml(currentItemDirectory + File.separator + "metadata_" + schema + ".xml", schema);
                        schemaWriter.start();
                        nonDCWriters.put(schema, schemaWriter);
                    }
                    processMetaBodyRowField(optionalMetaDataField.getKey(), optionalMetaDataField.getValue(), nonDCWriters.get(schema));
                }
            }
            contentsWriter.close();
            xmlWriter.end();
            for (String key : nonDCWriters.keySet()) {
                nonDCWriters.get(key).end();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds the values for the specific piece of metadata to the output. Accepts
     * multiple values per value so long as they are separated by the separator character
     *
     * @param field_header Field name, such as dc.description or dc.description.abstract
     * @param field_value  Metadata value or values. Multiple values can be separated by a separator character.
     * @param xmlWriter    The xml file that the data is being written to
     */
    private void processMetaBodyRowField(String field_header, String field_value, OutputXml xmlWriter) {
        // process Metadata field. Multiple entries can be specified with separator character
        String[] fieldValues = field_value.split(seperatorRegex);
        for (int valueNum = 0; valueNum < fieldValues.length; valueNum++) {
            if (fieldValues[valueNum].trim().length() > 0) {
                xmlWriter.writeOneDC(field_header, fieldValues[valueNum].trim());
            } else {
                continue;
            }
        }
        //TODO test that this works in both cases of single value and multiple value
    }

    /**
     * Processes the files for the filename column.
     * open contents
     * for-each files as file
     * copy file into directory
     * add file to contents
     *  @param contentsWriter Writer to the contents file which tracks the files to ingest for item
     * @param filePath      String with filePath.
     */
    private void processMetaBodyRowFile(BufferedWriter contentsWriter, Path filePath) {
        try {
            String contentsRow = filePath.toString();
            String contentsPrefix = String.format("-r -s %s -f ", storeNumber);
            contentsRow = contentsPrefix.concat(contentsRow); //TODO: hardcoded store number
            contentsWriter.append(contentsRow);
            contentsWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Makes a new directory for the item being processed
     * /path/to/input/SimpleArchiveFormat/item_27/
     *
     * @param itemNumber Iterator for the item being processed, Starts from zero.
     * @return Absolute path to the newly created directory
     */
    private String makeNewDirectory(int itemNumber) {
        File newDirectory = new File(outputFilePath + "/item_" + itemNumber);
        newDirectory.mkdir();
        return newDirectory.getAbsolutePath();
    }

    public void setStoreNumber(String storeNumber) {
        this.storeNumber = storeNumber;
    }
}