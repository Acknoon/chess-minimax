package finalVar;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Document : Utils
 *
 * Date     : 18/10/2019
 *
 * Author   : Ariel Mazar (mazarariel@gmail.com)
 */
public class Utils
{
    public static final boolean SHOW_DEBUG_MESSAGES     = false;    // for log debug
    public static final boolean SHOW_DEBUG_WITH_DIALOG  = false;   // for log debug
    
    public static void debug(String msg)
    {
        if(SHOW_DEBUG_MESSAGES)
            System.out.println(">>  " + msg);
    }
    
    public static void debug(Exception ex)
    {
        if(SHOW_DEBUG_MESSAGES)
        {
            String errLine;
            String className = Utils.class.getPackage().getName();

            String errInfo = "Exception: " + ex + "\n";
            errInfo += "------------------------------------------------------------------\n";
            for(int i = ex.getStackTrace().length - 1; i >= 0; i--)
            {
                errLine = ex.getStackTrace()[i].toString();
                if(errLine.startsWith(className) && i > 0 && !ex.getStackTrace()[i - 1].toString().startsWith(className))
                {
                    errInfo += ">>>>  " + errLine + "\n";
                }
                else
                {
                    errInfo += ">>  " + errLine + "\n";
                }
            }

            System.out.println("\n" +errInfo);

            if(SHOW_DEBUG_WITH_DIALOG)
                showDebugDialog(errInfo);
        }
    }

    public static void showErrMsgToUser(JFrame win, String title, String msg)
    {
        JOptionPane.showMessageDialog(win, msg, title, JOptionPane.ERROR_MESSAGE);
    }
    
    public static void showDebugDialog(String msg)
    {
        showDebugDialog(msg, null);
    }
    
    public static void showDebugDialog(String msg, JFrame win)
    {
        JOptionPane optionPane = new JOptionPane(msg, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = optionPane.createDialog("Debug Message");
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);  // WILL BLOCK the program
        dialog.dispose();
    }
}
