package com.fayayo.study.dataLoad;

import com.fayayo.study.dataLoad.conn.ConnnectionManager;
import com.fayayo.study.dataLoad.convert.ConvertInputStream;
import com.fayayo.study.middleware.buffer.BufferRecordExchange;
import com.fayayo.study.middleware.channel.Channel;
import com.fayayo.study.middleware.channel.impl.MemoryChannel;
import com.fayayo.study.middleware.elements.Record;
import com.fayayo.study.middleware.elements.StringColumn;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author dalizu on 2018/9/20.
 * @version v1.0
 * @desc 通过load命令导入到Mysql
 */
public class LoadRecord {


    public static void main(String[] args) throws Exception {

        //定义内存通道
        Channel channel=new MemoryChannel();

        //生产数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("生产者开始产生数据......");

                    producerData(new BufferRecordExchange(channel));
            }
        }).start();

        //消费者对数据进行消费

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("消费者开始消费数据......");

                try {
                    consumerData(new BufferRecordExchange(channel));
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }


    /**
     * 字段值内出现"escaped by"字符，该字符将被去除，同时保留其后一个字符；
     默认
     abc$cc

     加上 ESCAPED BY '$' "
     abccc
     *
     *
     * */
    public static void loadData(BufferRecordExchange bufferRecordExchange)throws SQLException{

        //转换数据为inputStream
        InputStream inputStream=new ConvertInputStream(bufferRecordExchange);


        Connection connection= ConnnectionManager.getConnection();

        String path="abc.txt";

        //【注意:数据的格式 每列是逗号分隔，每行是\n进行分隔】
        /*String loadSql = "load data local infile '"+path+"' ignore into table user1 " +
                "character set 'utf8' " +
                "fields terminated by ',' " +
                "optionally enclosed by '\"' " +
                "lines terminated by '\\n' (username,telephone,mail)";*/

       String writeMode="ignore";

        String loadSql = "load data local infile 'datax_tmp.txt' " + writeMode + " into table ";
        loadSql += "user1";
        loadSql += " character set 'utf8' ";
        loadSql += " fields terminated by ',' ";
        //loadSql+=" optionally enclosed by '\"' ";
        loadSql += " ESCAPED BY '\\\\' ";
        loadSql += " lines terminated by '\\n'";
        loadSql += " (username,telephone,mail)";


        System.out.println("loadSql=====>"+loadSql);

        PreparedStatement statement=connection.prepareStatement(loadSql);

        if (statement.isWrapperFor(com.mysql.jdbc.Statement.class)) {
            com.mysql.jdbc.PreparedStatement mysqlStatement = statement.unwrap(com.mysql.jdbc.PreparedStatement.class);

            mysqlStatement.setLocalInfileInputStream(inputStream);
            mysqlStatement.executeUpdate();
        }else {
            System.out.println("未知错误，请检查!!!!!!");
        }

        connection.close();


    }

    private static void producerData(BufferRecordExchange bufferRecordExchange) {

        int count=0;

        for (int i=0;i<1;i++){

            for (int j=0;j<100;j++){
                StringColumn stringColumn=new StringColumn("test"+count);
                Record record=bufferRecordExchange.createRecord();
                record.addColumn(stringColumn);


                StringColumn telCol=new StringColumn("1392177818"+count);
                record.addColumn(telCol);

                StringColumn mailCol=new StringColumn("abc\\vv"+count);
                record.addColumn(mailCol);

                bufferRecordExchange.sendToWriter(record);
                count++;
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        bufferRecordExchange.terminate();//结束标志
    }

    private static void consumerData(BufferRecordExchange bufferRecordExchange) throws SQLException {

        loadData(bufferRecordExchange);
        System.out.println("exit!!!!!!");
    }



}
