package org.eightfoldconsulting.nimas2pdf;


import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;

/**
 *
 * @author Gregory Buchenberger
 */
public class FindImageFilesDirectoryWalker extends DirectoryWalker {

// Create a filter for Non-hidden directories
    private static IOFileFilter dirFilter = FileFilterUtils.andFileFilter(FileFilterUtils.directoryFileFilter(), HiddenFileFilter.VISIBLE);

    private static IOFileFilter fileFilter = FileFilterUtils.andFileFilter(FileFilterUtils.fileFileFilter(),
            FileFilterUtils.orFileFilter(FileFilterUtils.suffixFileFilter(".png"), FileFilterUtils.suffixFileFilter(".jpg")));

    // Combine the directory and file filters using an OR condition
    private static FileFilter filter = FileFilterUtils.orFileFilter(dirFilter, fileFilter);


    
    /**
     *
     */
    public FindImageFilesDirectoryWalker() {
        super(filter, -1);
    }


    /**
     *
     * @param file
     * @param depth
     * @param results
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void handleFile(final File file, final int depth, final Collection results) throws IOException {
        //log.info("Found file: " + file.getAbsolutePath());
        results.add(file);
    }

    /**
     *
     * @param dir
     * @return
     * @throws MalformedURLException
     */
    public List<File> getFiles(File dir) throws MalformedURLException {
        List<File> files = new ArrayList<File>();

        URL url = dir.toURI().toURL();

        if (url == null) {
                System.out.println("Unable to find root folder of image files!");
                return files;
        }

        File directory = new File(url.getFile());

        try {
                walk(directory, files);
        }
        catch (IOException e) {
               System.out.println("Problem finding image files! "+ e.getMessage());
        }

        return files;
    }
}

