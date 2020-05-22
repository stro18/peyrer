package de.peyrer.indexer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;

public abstract class AbstractIndexer implements IIndexer {

    protected Path indexPath;

    // ".." for parent directory
    protected Path createIndexDirectory(String ... directory) throws IOException {

        Path wantedIndexPath = Paths.get(System.getProperty("user.dir"));

        while(directory.length > 0 && directory[0].equals("..")){
            wantedIndexPath = wantedIndexPath.getParent();
            directory = Arrays.copyOfRange(directory, 1, directory.length);
        }

        wantedIndexPath = Paths.get(wantedIndexPath.toString(), directory);

        if(Files.exists(wantedIndexPath)){
            Files.walk(wantedIndexPath)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }

        return Files.createDirectories(wantedIndexPath);
    }

    @Override
    public String getIndexPath() throws IOException {
        return this.indexPath.toString();
    }
}
