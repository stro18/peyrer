package de.peyrer.indexer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public abstract class AbstractIndexer implements IIndexer {

    protected Path createIndexDirectory(String directory) throws IOException {
        Path wantedIndexPath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", directory);
        Files.walk(wantedIndexPath)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        return Files.createDirectory(wantedIndexPath);
    }
}
