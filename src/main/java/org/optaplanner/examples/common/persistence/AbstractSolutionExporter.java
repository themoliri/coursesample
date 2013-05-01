/*
 * Copyright 2010 JBoss Inc
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

package org.optaplanner.examples.common.persistence;

import java.io.File;
import java.util.Arrays;

import org.optaplanner.core.impl.solution.Solution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSolutionExporter {

	protected final transient Logger logger = LoggerFactory.getLogger(getClass());
	
    private static final String DEFAULT_INPUT_FILE_SUFFIX = ".xml";
    protected SolutionDao solutionDao;

    public AbstractSolutionExporter(SolutionDao solutionDao) {
        this.solutionDao = solutionDao;
    }

    protected File getInputDir() {
        return new File(solutionDao.getDataDir(), "solved");
    }

    protected File getOutputDir() {
        return new File(solutionDao.getDataDir(), "output");
    }

    protected String getInputFileSuffix() {
        return DEFAULT_INPUT_FILE_SUFFIX;
    }

    protected abstract String getOutputFileSuffix();

    public void convertAll() {
        File inputDir = getInputDir();
        File outputDir = getOutputDir();
        outputDir.mkdirs();
        File[] inputFiles = inputDir.listFiles();
        Arrays.sort(inputFiles);
        if (inputFiles == null) {
            throw new IllegalArgumentException(
                    "Your working dir should be optaplanner-examples and contain: " + inputDir);
        }
        for (File inputFile : inputFiles) {
            String inputFileName = inputFile.getName();
            if (inputFileName.endsWith(getInputFileSuffix())) {
                Solution solution = solutionDao.readSolution(inputFile);
                String outputFileName = inputFileName.substring(0,
                        inputFileName.length() - getInputFileSuffix().length())
                        + getOutputFileSuffix();
                File outputFile = new File(outputDir, outputFileName);
                writeSolution(solution, outputFile);
            }
        }
    }

    public abstract void writeSolution(Solution solution, File outputFile);
}
