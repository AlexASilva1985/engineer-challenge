package br.com.engineerchallenge.observer;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;

@Service
@Slf4j
public class FolderObserver {
    private PublishSubject<Path> subject = PublishSubject.create();

    public Observable<Path> observeFolder(String folderPath) throws IOException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path folder = Paths.get(folderPath);
        folder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

        Thread observerThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    WatchKey key = watchService.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        WatchEvent.Kind<?> kind = event.kind();
                        log.info("Novo evento detectado no path {}", folderPath);
                        if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                            Path filePath = folder.resolve((Path) event.context());
                            subject.onNext(filePath);
                        }
                    }
                    key.reset();
                }
            } catch (InterruptedException | ClosedWatchServiceException e) {
                log.error("Falha na observação de arquivos na pasta: " + folderPath);
                log.error(e.getMessage());
            } finally {
                log.info("Diretorio observado {}", folderPath);
            }
        });
        observerThread.start();

        return subject;
    }
}

