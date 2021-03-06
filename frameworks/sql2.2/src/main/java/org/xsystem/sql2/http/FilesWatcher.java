/*
 * Copyright 2017 Andrey Timofeev.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xsystem.sql2.http;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author Andrey Timofeev
 */
public class FilesWatcher extends Thread {

    private final List<File> files;
    private AtomicBoolean stop = new AtomicBoolean(false);
    FileChageEvent changeEvent;

    public FilesWatcher(List<File> files, FileChageEvent changeEvent) {
        this.files = files;
        this.changeEvent = changeEvent;
    }

    public boolean isStopped() {
        return stop.get();
    }

    public void stopThread() {
        stop.set(true);
    }

    public void doOnChange(File f) {
        if (changeEvent != null) {
            changeEvent.onChange(f);
        }
        // Do whatever action you want here
    }

    File getMonitoringFile(String filename) {
        for (File file : files) {
            if (filename.equals(file.getName())) {
                return file;
            }
        }
        return null;
    }

    @Override
    public void run() {

        try (WatchService watcher = FileSystems.getDefault().newWatchService()) {
            HashSet checkSet = new HashSet();
            for (File file : files) {
                Path path = file.toPath().getParent();
                if (!checkSet.contains(path)) {
                    path.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);
                    checkSet.add(path);
                }
            }
            while (!isStopped()) {
                WatchKey key;
                try {
                    key = watcher.poll(25, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    return;
                }
                if (key == null) {
                    Thread.yield();
                    continue;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    @SuppressWarnings("unchecked")
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path filename = ev.context();
                    if (kind == StandardWatchEventKinds.OVERFLOW) {
                        Thread.yield();
                        continue;
                    } else if (kind == java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY) {
                        File f = getMonitoringFile(filename.toString());
                        if (f != null) {
                            doOnChange(f);
                        }
                    }
                    boolean valid = key.reset();
                    if (!valid) {
                        break;
                    }
                }
                Thread.yield();
            }
        } catch (Throwable e) {
            // Log or rethrow the error
        }
    }
}
