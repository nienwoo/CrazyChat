package com.crazymakercircle.coccurent;

import com.crazymakercircle.util.Print;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by 尼恩 at 疯狂创客圈
 */

public class JavaFutureDemo
{

    public static final int SLEEP_GAP = 500;


    public static String getCurThreadName()
    {
        return Thread.currentThread().getName();
    }

    static class HotWarterJob implements Callable<Boolean> //①
    {

        @Override
        public Boolean call() throws Exception //②
        {

            try
            {
                Print.tcfo("洗好水壶");
                Print.tcfo("灌上凉水");
                Print.tcfo("放在火上");

                //线程睡眠一段时间，代表烧水中
                Thread.sleep(SLEEP_GAP);
                Print.tcfo("水开了");

            } catch (InterruptedException e)
            {
                Print.tcfo(" 发生异常被中断.");
                return false;
            }
            Print.tcfo(" 运行结束.");

            return true;
        }
    }

    static class WashJob implements Callable<Boolean>
    {

        @Override
        public Boolean call() throws Exception
        {


            try
            {
                Print.tcfo("洗茶壶");
                Print.tcfo("洗茶杯");
                Print.tcfo("拿茶叶");
                //线程睡眠一段时间，代表清洗中
                Thread.sleep(SLEEP_GAP);
                Print.tcfo("洗完了");

            } catch (InterruptedException e)
            {
                Print.tcfo(" 清洗工作 发生异常被中断.");
                return false;
            }
            Print.tcfo(" 清洗工作  运行结束.");
            return true;
        }

    }

    static boolean  warterOk = false;
    static boolean cupOk =false;


    public static void drinkTea()
    {
        if (warterOk && cupOk)
        {
            Print.tcfo("泡茶喝");
        }
        else if (!warterOk)
        {
            Print.tcfo("烧水失败，没有茶喝了");
        }
        else if (!cupOk)
        {
            Print.tcfo("杯子洗不了，没有茶喝了");
        }

    }
    public static void main(String args[])
    {

        Callable<Boolean> hJob = new HotWarterJob();//③
        FutureTask<Boolean> hTask =
                new FutureTask<Boolean>(hJob);//④
        Thread hThread = new Thread(hTask, "** 烧水-Thread");//⑤

        Callable<Boolean> wJob = new WashJob();//③
        FutureTask<Boolean> wTask =
                new FutureTask<Boolean>(wJob);//④
        Thread wThread = new Thread(wTask, "$$ 清洗-Thread");//⑤


        hThread.start();
        wThread.start();
        Thread.currentThread().setName("主线程");

        try
        {

             warterOk = hTask.get();
             cupOk = wTask.get();

//            hThread.join();
//            wThread.join();
             drinkTea();


        } catch (InterruptedException e)
        {
            Print.tcfo(getCurThreadName() + "发生异常被中断.");
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        }
        Print.tcfo(getCurThreadName() + " 运行结束.");
    }
}