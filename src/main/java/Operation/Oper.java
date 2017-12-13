package Operation;
import beans.StorageNode;

import java.util.LinkedList;

/**
 * Created by Ting on 2017/7/17.
 */
public class Oper {
    //选择出不超过num个的Remain最大的节点
    public static LinkedList<StorageNode> chooseNode(LinkedList<StorageNode> li, int num){
        LinkedList<StorageNode> list=new LinkedList<StorageNode>();
        //这里有可能发生线程冲突
        for(int i=0;i<li.size();i++){
            if(li.get(i).getOnline())
            list.add(li.get(i));
        }
        for(int i=list.size()-1;i>=0;i--){
            for(int j=0;j<i;j++){
                if(list.get(j).getRemain()<list.get(j+1).getRemain()){
                    StorageNode node=list.get(j);
                    list.remove(j);
                    list.add(j+1,node);
                }
            }
        }
        LinkedList<StorageNode> lis=new LinkedList<StorageNode>();
        for(int i=0;i<num&&i<list.size();i++){
            lis.add(list.get(i));
        }
        return lis;
    }
    public static void main(String[] args){
//        LinkedList<StorageNode> list=new LinkedList<StorageNode>();
//        StorageNode node1=new StorageNode();
//        node1.setRemain(1000L);
//        node1.setOnline(true);
//        StorageNode node2=new StorageNode();
//        node2.setRemain(2000L);
//        node2.setOnline(true);
//        StorageNode node3=new StorageNode();
//        node3.setRemain(3000L);
//        node3.setOnline(true);
//        StorageNode node4=new StorageNode();
//        node4.setRemain(4000L);
//        node4.setOnline(true);
//        list.add(node1);
//        list.add(node2);
//        list.add(node3);
//        list.add(node4);
//
//        LinkedList<StorageNode> li=Oper.chooseNode(list,2);
//        for(int i=0;i<li.size()-1;i++){
//                System.out.println(li.get(i).getRemain());
//        }
    }
}
