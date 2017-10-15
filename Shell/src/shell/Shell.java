/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shell;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author rapha
 */
public class Shell {

    /**
     * @param args the command line arguments
     */ 
    
    public static CountDownLatch latch = new CountDownLatch(0);
    
    public static void main(String[] args) {
        boolean running = true;
        String commands = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        while(running){
            try {
                System.out.print("Input: ");
                commands = br.readLine();
            } catch (IOException ex) {
                Logger.getLogger(Shell.class.getName()).log(Level.SEVERE, null, ex);
            }
            String[] commandsArray = commands.split("&");
            latch = new CountDownLatch(commandsArray.length);
            for(int i = 0; i < commandsArray.length; i++){
                if(!(commandsArray[i].equals("exit"))){
                    ShellThread t = new ShellThread();
                    t.setCommand(commandsArray[i]);
                    t.start();
                }
                else{
                    running = false;
                    latch.countDown();
                }
            }
            try {
                latch.await(15, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                Logger.getLogger(Shell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
