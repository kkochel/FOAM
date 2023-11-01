/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package pl.lodz.uni.biobank.foam.outbox;

import org.apache.sshd.common.file.FileSystemFactory;
import org.apache.sshd.common.file.root.RootedFileSystemProvider;
import org.apache.sshd.common.session.SessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

@Component
public class LocalFileSystemFactory implements FileSystemFactory {
    private static final Logger log = LoggerFactory.getLogger(LocalFileSystemFactory.class);

    @Value("${application.local.directory}")
    private String outboxFolder;

    @Override
    public FileSystem createFileSystem(SessionContext session) {
        String username = session.getUsername();
        String root = outboxFolder.endsWith(File.separator) ? outboxFolder + username : outboxFolder + File.separator + username;
        File home = new File(root);
        home.mkdirs();

        log.info("Created directory: {} for user: {} ", home.getAbsolutePath(), username);
        return createNewFileSystem(home);
    }

    private static FileSystem createNewFileSystem(File home) {
        try {
            return new RootedFileSystemProvider().newFileSystem(home.toPath(), Collections.emptyMap());
        } catch (IOException e) {
            log.error("Error during creating directory: {}", home.getAbsolutePath());
            throw new CreateNewDirectoryException(e);
        }
    }

    @Override
    public Path getUserHomeDir(SessionContext session) {
        String username = session.getUsername();
        if (outboxFolder.endsWith(File.separator)) {
            return Paths.get(outboxFolder + username);
        } else {
            return Paths.get(outboxFolder + File.separator + username);
        }
    }
}
