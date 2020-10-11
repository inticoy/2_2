import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class LeafNode extends Node { // LeafNode is 1

    private int[] values;
    private LeafNode nextLeafNode;
    public Node pointNode;

    public LeafNode() {
        super();
        pointNode = null;
    }

    public LeafNode(int size) {
        super(size);
        values = new int[size];
        pointNode = null;
    }

    public void writeLeaf(PrintWriter printWriter) throws IOException{
        //PrintWriter pw = new PrintWriter(file, true);
        printWriter.print("1 ");
        printWriter.print(keyNum + " ");
        for (int i = 0; i < keyNum; i++) {
            printWriter.print(keys[i] + " " + values[i] + " ");
        }
        printWriter.print("\n");
        //pw.close();
    }

    public boolean find(int key) {
        if (this.keyNum == 0) {
            System.out.println("NOT FOUND, leafNode's number of key is 0");
            return false;
        }

        for (int i = 0; i < this.keyNum; i++) {
            if (keys[i] == key) {
                System.out.println("value: " + values[i]);
                return true;
            }
        }
        return false;
    }

    public LeafNode getNextLeafNode() {
        return (LeafNode) pointNode;
    }

    public void range(int startKey, int endKey) {
        int i = 0;
        for (i = 0; i < this.keyNum; i++) {
            if (keys[i] >= startKey && keys[i] <= endKey) {
                System.out.println(keys[i] + ", " + values[i]);
            }
        }
        if (i == this.keyNum && pointNode != null) {
            this.getNextLeafNode().range(startKey, endKey);
        }
    }

    public void printNode(){
        System.out.print("LeafNode, size: " + size + ", keyNum: " + keyNum + ", keys: ");
        for (int i = 0; i < keyNum; i++) {
            System.out.print(keys[i] + " ");
        }
        System.out.println(" ");
    }

    public LeafNode add(int key, int value) {
        if (this.isFull()) {
            // if full, split
            LeafNode leafNode = new LeafNode(size);

            //split implement
            int i;
            for (i = 0; i < this.keyNum; i++) {
                if (keys[i] > key) {
                    break;
                }
            }
            // key should be place at i

            this.keyNum = (size / 2) + 1;
            if(i + 1 > this.keyNum){ // key should be place behind
                leafNode.add(key, value);
                // 나머지도 add 해줌
                for (int j = this.keyNum; j < size; j++) {
                    leafNode.add(keys[j], values[j]);
                }
            } else { //front 여기가 문제
                for (int j = this.keyNum - 1; j < size; j++) {
                    leafNode.add(keys[j], values[j]);
                }
                this.keyNum -= 1;
                this.add(key, value);
            }

            //split implement

            leafNode.pointNode = this.pointNode;
            this.pointNode = leafNode;

            return leafNode;
        } else {
            int i, j; // find where to add
            for (i = 0; i < keyNum; i++) {
                if (keys[i] < key) continue; // if all keys are smaller than key, i will be keyNum
                else break;
            }
            // move one by one
            for (j = keyNum - 1; j >= i; j--) { // -1 means index
                keys[j + 1] = keys[j];
                values[j + 1] = values[j];
            }
            // add
            this.keyNum += 1;
            keys[i] = key;
            values[i] = value;
            return null;
        }
    }

}