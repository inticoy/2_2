import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Node {
    protected int size;
    protected int keyNum;
    protected int[] keys;


    // constructors
    public Node() {
        size = 0;
        keyNum = 0;

    }

    public Node(int size) {
        this.size = size;
        keyNum = 0;

        keys = new int[size];

        // make all keys -1
        for (int i = 0; i < size; i++) {
            keys[i] = -1;
        }
    }

    public int getSize() {
        return size;
    }

    public void write(PrintWriter printWriter) throws IOException {
        //PrintWriter pw = new PrintWriter(file, true);


        if(this instanceof LeafNode){
            ((LeafNode) this).writeLeaf(printWriter);

        }else if(this instanceof NonLeafNode){
            ((NonLeafNode) this).writeNonLeaf(printWriter);
        }else{
            System.out.println("else");
        }


        //implement
        //pw.close();
    }

    public boolean singleSearch(int key) {
        boolean ret = false;
        if (this instanceof LeafNode) {
            ret = ((LeafNode) this).find(key);
        } else if (this instanceof NonLeafNode) {
            ret = ((NonLeafNode) this).find(key);
        }
        if(!ret){
            System.out.println("NOT FOUND");
        }
        return ret;
    }

    public boolean insertSearch(int key) { //same as singleSearch, but no sout
        boolean ret = false;
        if (this instanceof LeafNode) {
            ret = ((LeafNode) this).find(key);
        } else if (this instanceof NonLeafNode) {
            ret = ((NonLeafNode) this).find2(key);
        }
        return ret;
    }

    public void rangeSearch(int startNum, int endNum) {
        if (this instanceof LeafNode) {
            ((LeafNode) this).range(startNum, endNum);
        } else if (this instanceof NonLeafNode) {
            ((NonLeafNode) this).range(startNum, endNum);
        }
    }

    public boolean isFull() {
        if (size == keyNum) return true;
        else return false;
    }

    public void printNode() {
        if (this instanceof LeafNode) {
            ((LeafNode) this).printNode();
        } else if (this instanceof NonLeafNode) {
            ((NonLeafNode) this).printNode();
        }
    }

    public Node firstInsert(int key, int value){
        if(this.insertSearch(key)){ // already existing key
            return this;
        }

        if (!(this instanceof LeafNode) && !(this instanceof NonLeafNode)) { // first insert
            LeafNode leafNode = new LeafNode(this.size);
            leafNode.add(key, value);
            return leafNode;
        } else if (this instanceof LeafNode) {
            Node node = ((LeafNode) this).add(key, value);
            if (node == null) return this;

            else {
                NonLeafNode nonLeafNode = new NonLeafNode(size);
                nonLeafNode.keyNum = 1;

                //LeafNode leafNode = ((LeafNode) this).add(key, value);
                //if 위에서 add 는  했으니까
                LeafNode leafNode = (LeafNode) node;
                //this 랑 leafNode 저장

                nonLeafNode.keys[0] = leafNode.keys[0];
                nonLeafNode.addNode(0, this);
                nonLeafNode.addNode(1, leafNode);

                return nonLeafNode;
            }
        }
        return null;
    }

    public Node insert(int key, int value) {

        if(this.insertSearch(key)){ // already existing key
            return this;
        }
        //System.out.print("insert " + key);

        if (!(this instanceof LeafNode) && !(this instanceof NonLeafNode)) { // first insert
            LeafNode leafNode = new LeafNode(this.size);
            leafNode.add(key, value);
            return leafNode;
        } else if (this instanceof LeafNode) {
            Node node = ((LeafNode) this).add(key, value);
            if(node == null) return null;
            // return this  0926 0254 여기가 문제였던것 같다. 처음에 추가할때랑 나중에 다름.
            else {
                NonLeafNode nonLeafNode = new NonLeafNode(size);
                nonLeafNode.keyNum = 1;

                //LeafNode leafNode = ((LeafNode) this).add(key, value);
                //if 위에서 add 는  했으니까
                LeafNode leafNode = (LeafNode) node;
                //this 랑 leafNode 저장

                nonLeafNode.keys[0] = leafNode.keys[0];
                nonLeafNode.addNode(0,this);
                nonLeafNode.addNode(1, leafNode);

                return nonLeafNode;
            }
        } else if (this instanceof NonLeafNode){
            Node node = ((NonLeafNode) this).goTo(key, value);
            if(node == null) return this; //원래 return this.임
            else{
                // leaf 가 올라오면 ? // 올라오는건 다 Non leaf 인가? - 아니다 실험결과 leaf 도 올라옴.
                if(node instanceof LeafNode){

                    return this;
                } // NonLeafNode 가 올라옴
                NonLeafNode nonLeafNode = (NonLeafNode) node; // 하나짜리 노드임

                //0926 0329
                boolean checkKey = false;
                for (int i = 0; i < keyNum; i++) {
                    if(this.keys[i] == nonLeafNode.keys[0]) checkKey = true;
                }
                if(checkKey){
                    return this;
                }

                /////////////////

                NonLeafNode temp;
                temp = ((NonLeafNode) this).add(nonLeafNode.keys[0] ,nonLeafNode); // 첫번째 key 와 주소 [1]만 중요

                if(temp == null){
                    return this;
                }else{ // non 단계에서 split됨 // 어떻게 구현?

                    System.out.println("check");
                    return temp;
                }
            }
        } else {
            return null;
        }
    }
}

