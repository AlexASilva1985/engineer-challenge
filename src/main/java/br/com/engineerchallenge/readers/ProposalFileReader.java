package br.com.engineerchallenge.readers;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProposalFileReader implements ItemReader<File> {
    private Iterator<File> iterator;
    private boolean isReadAllFile;

    @Value("${sftp.directory.received}")
    private String directoryPath;

    public ProposalFileReader() {
        isReadAllFile = true;
    }

    @Override
    public File read() {
        if (isReadAllFile) {
            prepareFileList();
        }

        if (iterator.hasNext()) {
            return iterator.next();
        }

        isReadAllFile = true;
        return null;
    }

    private void prepareFileList() {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

        isReadAllFile = false;

        if (files != null) {
            iterator = Arrays.asList(files).iterator();
        } else {
            this.iterator = Collections.emptyIterator();
        }
    }
}

