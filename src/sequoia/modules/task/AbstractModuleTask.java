package sequoia.modules.task;

public abstract class AbstractModuleTask implements IModuleTask{



    protected void failed(String msg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected void failed(String msg, IModuleTask iTask) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected void successed() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected void successed(String msg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
/*
    public static final String toHex(byte[] data, int off, int length) {
        //double size, two bytes (hex range) for one byte
        StringBuffer buf = new StringBuffer(data.length * 2);
        for (int i = off; i < length; i++) {
            //don't forget the second hex digit
            if (((int) data[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString((int) data[i] & 0xff, 16));
            if (i < data.length - 1) {
                buf.append(" ");
            }
        }
        return buf.toString();
    }//toHex

    private static boolean SendPacket(DataOutputStream DataOut, byte abyte0[], int length) {
        int i;
        i = length;

        try {
            DataOut.write(abyte0, 0, i);
            DataOut.flush();
        } catch (IOException ex) {
            LogKit.error("发送数据包失败:", ex);
          // System.err.println("Send NextPacket Failed I/O: " + ex);
            return false;
        }
        return true;
    }

    private static byte[] RecvNextPacket(DataInputStream DataIn, int length) {
        wait(DataIn, length);
        byte abyte0[] = new byte[length];
        try {
            DataIn.read(abyte0, 0, length);
            if (abyte0 == null) {
                return null;
            }
        } catch (IOException ioexception) {
            LogKit.error("读取数据包失败:", ioexception);
          //  System.err.println("Read NextPacket Failed I/O: " + ioexception);
            return null;
        }
        return abyte0;
    }

    private static void wait(DataInputStream dataInputStream, int length) {
        //       header1 = readAllWeather.getHead();
        //       readAllWeather.printHead();
        //       System.out.print("##");
        int efawrefa = 0;
        int waefawfafe = 0;
        try {
            efawrefa = dataInputStream.available();
        } catch (IOException ex) {
            LogKit.error("等待数据包异常:", ex);
        }
        //   System.out.print("数据长度：" + lengthI);
        while (efawrefa != length) {
            waefawfafe += 100;
            if (waefawfafe > 6000) {
                break;
            }
            try {
                efawrefa = dataInputStream.available();
            } catch (IOException ex) {
                //  Logger.getLogger(NetInOutMethod.class.getName()).log(Level.SEVERE, null, ex);
                LogKit.error("等待数据包失败:", ex);
              //  System.err.println("dataInputStream.available() Failed I/O: " + ex);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                    Logger.getLogger(AbstractModuleTask.class.getName()).log(Level.SEVERE, null, ex);
                LogKit.error("Thread.sleep(50) Failed:", ex);
              //  System.err.println("Thread.sleep(50) Failed: " + ex);
            }
        }
    }
    */
}
