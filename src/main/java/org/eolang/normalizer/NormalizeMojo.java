/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2023 Objectionary.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.eolang.normalizer;

import com.jcabi.log.Logger;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Normalizes phi-expressions.
 *
 * @since 0.0.1
 */
@Mojo(name = "disassemble", defaultPhase = LifecyclePhase.PROCESS_CLASSES)
public final class NormalizeMojo extends AbstractMojo {

    /**
     * The directory with .phi files to normalize.
     * @since 0.1.0
     */
    @Parameter(defaultValue = "${project.build.directory}/normalizer-source")
    private File source;

    /**
     * The directory with .phi files to generate.
     * @since 0.1.0
     */
    @Parameter(defaultValue = "${project.build.directory}/normalizer-targetr")
    private File target;

    @Override
    public void execute() throws MojoExecutionException {
        try {
            this.transform();
        } catch (final IOException ex) {
            throw new MojoExecutionException(
                String.format(
                    "Failed to transform from %s to %s",
                    this.source,
                    this.target
                ),
                ex
            );
        }
        Logger.info(this, "Done!");
    }

    /**
     * Transform them all.
     * @throws IOException If fails
     */
    private void transform() throws IOException {
        final Path src = this.source.toPath();
        final Path tgt = this.target.toPath();
        Files.walk(src)
            .filter(p -> !p.toFile().isDirectory())
            .forEach(
                p -> {
                    try {
                        this.transform(p, tgt.resolve(p.relativize(src)));
                    } catch (final IOException ex) {
                        throw new IllegalArgumentException(ex);
                    }
                }
            );
        Logger.info(this, "works!");
    }

    /**
     * Transform one file.
     * @param src The source file
     * @param tgt The target
     * @throws IOException If fails
     */
    private void transform(final Path src, final Path tgt) throws IOException {
        Logger.info(this, "%s -> %s done", src, tgt);
    }
}
