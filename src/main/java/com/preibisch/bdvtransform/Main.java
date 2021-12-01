package com.preibisch.bdvtransform;

import picocli.CommandLine;

import java.io.File;
import java.io.IOException;

@CommandLine.Command(name = "Main", version = "Main 1.0", mixinStandardHelpOptions = true)
public class Main implements Runnable {

    @CommandLine.Parameters(paramLabel = "<files>",
            description = "Image files to be opened exmpl.: NRRD")
    private String[] files;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
//        System.exit(exitCode);
    }

    @Override
    public void run() {
        try {
            if (files == null){
                System.out.println("ERROR: Please specify files");
                return;
            }
            for (String path : files){
                if (!(new File(path).isFile())) {
                    System.out.println("ERROR: Image " + path + " doesn't exist");
                    return;
                }}
            new BDVStacking(files);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
