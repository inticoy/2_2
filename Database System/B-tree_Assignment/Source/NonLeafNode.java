import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class NonLeafNode extends Node {

    private Node[] nodes; // last one is a pointer to the rightmost child node

    public NonLeafNode() {
        super();
    }

    public NonLeafNode(int size) {
        super(size);
        nodes = new Node[size + 1];
        for (int i = 0; i < size + 1; i++) {
            nodes[i] = null;
        }
    }

    public void writeNonLeaf(PrintWriter printWriter) throws IOException {
        //PrintWriter pw = new PrintWriter(file, true);

        printWriter.print("0 ");
        printWriter.print(keyNum + " ");
        for (int i = 0; i < keyNum; i++) {
            printWriter.print(keys[i] + " ");
        }
        printWriter.print("\n");
        for (int i = 0; i < keyNum + 1; i++) {
            nodes[i].write(printWriter);
        }
        //pw.close();
    }

    public void addNode(int index, Node node) {
        nodes[index] = node;
    }

    public boolean find(int key) {
        boolean ret;
        int i;
        for (i = 0; i < keyNum; i++) {
            if (keys[i] > key) break;
        }
        ret = nodes[i].singleSearch(key);
        return ret;
    }

    public boolean find2(int key) {
        boolean ret;
        int i;
        for (i = 0; i < keyNum; i++) {
            if (keys[i] > key) break;
        }
        ret = nodes[i].insertSearch(key);
        return ret;
    }

    public void range(int startNum, int endNum) {
        int i;
        for (i = 0; i < keyNum; i++) {
            if (keys[i] > startNum) break;
        }
        nodes[i].rangeSearch(startNum, endNum);
    }

    public void printNode() {
        System.out.print("NonLeafNode, size: " + size + ", keyNum: " + keyNum + ", keys: ");
        for (int i = 0; i < keyNum; i++) {
            System.out.print(keys[i] + " ");
        }
        System.out.println(" ");
        for (int i = 0; i < keyNum + 1; i++) {
            nodes[i].printNode();
        }

    }

    public Node goTo(int key, int value) {
        Node node;
        int i;
        for (i = 0; i < keyNum; i++) {
            if (keys[i] > key) break;
        }
        node = nodes[i].insert(key, value);
        // 추가
        if(node instanceof NonLeafNode){
            if(((NonLeafNode) node).nodes[0] instanceof NonLeafNode){
                // 자기자신한테 더하기

//                NonLeafNode newNon = new NonLeafNode(size);
//                newNon.keyNum = 1;
//                newNon.keys[0] = node.keys[0];
//                newNon.nodes[1] = node;
//                this.add(node.keys[0], newNon);
//                return newNon;


                NonLeafNode ret;
                //Node temp = ((NonLeafNode) node).nodes[1];
                ret = ((NonLeafNode) this).add(node.keys[0], (NonLeafNode)node);
//                if(ret != null) {
//                    ret.nodes[size] = temp;
//                }
//                node.printNode();
                return ret;
            }
        }

        if(nodes[0] instanceof LeafNode){
            return node;
        }
        else if(nodes[0] instanceof NonLeafNode){
            return this;
        } else {
            return null;
        }
    }

    public NonLeafNode add(int key, NonLeafNode node) {
        if (this.isFull()) {
            NonLeafNode nonLeafNode = new NonLeafNode(size); // split non
            NonLeafNode nonLeafNode1 = new NonLeafNode(size); // upper non
            // non leaf node split

            int i;
            for (i = 0; i < this.keyNum; i++) {
                if (keys[i] > key) {
                    break;
                }
            }
            // key should be place at i

            this.keyNum = ((size + 1) / 2);

            if (i == this.keyNum) {
                // 0925 수정함. - 성공
                // key 가 올라가야함.
                //앞에건 그대로 냅두고
                nonLeafNode1.keyNum += 1;
                nonLeafNode1.nodes[0] = this;
                nonLeafNode1.nodes[1] = nonLeafNode;
                // 여기까진 공통.
                nonLeafNode1.keys[0] = key;
                // 이제 뒷 노드에 자료 옮기기
                nonLeafNode.addNode(0, node.nodes[1]); // 첫번째 nodes 에는 올라온 주소
                nonLeafNode.keyNum = size - this.keyNum;
                for (int j = 0; j < size - this.keyNum; j++) {
                    nonLeafNode.keys[j] = this.keys[this.keyNum + j];
                    nonLeafNode.addNode(j + 1, this.nodes[this.keyNum + 1 + j]);
                }
                return nonLeafNode1;
            } else if (i > this.keyNum) {
                // 0926 수정 필요
                // 뒤에 있는 값이 올라감 key 로 온게 올라가는건 확실히 아님.
                // 앞에건 그대로 냅두고
                nonLeafNode1.keyNum += 1;
                nonLeafNode1.nodes[0] = this; //****************************************************************** 4시
                nonLeafNode1.nodes[1] = nonLeafNode;
                // 여기까진 공통
                nonLeafNode1.keys[0] = this.keys[this.keyNum];

                // 이미 keyNum 번째 key가 하나 올라감.
                //기존것부터 뒤로 옮김
                for (int j = 0; j < size - this.keyNum - 1; j++) {
                    nonLeafNode.keys[j] = this.keys[this.keyNum + j + 1];
                    nonLeafNode.addNode(j + 1, this.nodes[size - j]);
                }
                nonLeafNode.addNode(0, this.nodes[this.keyNum + 1]);
                nonLeafNode.keyNum = size - this.keyNum - 1;
                // 뒤에다가 key 추가
                // 밑에 있는 front에서 앞노드 수정과 똑같음.
                // 그런데 keyNum += 1; 추가
                // 그리고 this.가 아니고 뒷노드임.

                int backNum = nonLeafNode.keyNum;

                if(node.nodes[1] instanceof LeafNode){
                    int x, y;
                    for (x = 0; x < backNum; x++) {
                        if (nonLeafNode.keys[x] < key) continue; // if all keys are smaller than key, i will be keyNum
                        else break;
                    }
                    // move one by one
                    for (y = backNum - 1; y >= i; y--) { // -1 means index
                        nonLeafNode.keys[y + 1] = nonLeafNode.keys[y];
                        nonLeafNode.nodes[y + 2] = nonLeafNode.nodes[y + 1];
                    }
                    // add
                    nonLeafNode.keyNum += 1;
                    nonLeafNode.keys[x] = key;
                    nonLeafNode.nodes[x + 1] = node.nodes[1];
                }else if(node.nodes[1] instanceof NonLeafNode){ // ?
                    int x, y;
                    for (x = 0; x < backNum; x++) {
                        if (nonLeafNode.keys[x] < key) continue; // if all keys are smaller than key, i will be keyNum
                        else break;
                    }
                    // move one by one
                    for (y = backNum - 1; y >= i; y--) { // -1 means index
                        nonLeafNode.keys[y + 1] = nonLeafNode.keys[y];
                        nonLeafNode.nodes[y + 2] = nonLeafNode.nodes[y + 1];
                    }
                    // add
                    nonLeafNode.keyNum += 1;
                    nonLeafNode.keys[x] = key;
                    nonLeafNode.nodes[x + 1] = node.nodes[1];
                }

                return nonLeafNode1;

            } else if (i < this.keyNum) { //front //작은게 들어올 수 있음

                // 0926 새벽 ? 부분 나중에 문제가 될수 있지만 일단 성공

                nonLeafNode1.keyNum += 1;
                nonLeafNode1.nodes[0] = this;
                nonLeafNode1.nodes[1] = nonLeafNode;
                nonLeafNode1.keys[0] = this.keys[this.keyNum - 1];


                // 뒤 노드에 추가
                nonLeafNode.keyNum = size - this.keyNum;
                for (int j = 0; j < size - this.keyNum; j++) {
                    nonLeafNode.keys[j] = this.keys[this.keyNum + j];
                    nonLeafNode.addNode(j, this.nodes[this.keyNum + j]);
                }
                nonLeafNode.addNode(size - this.keyNum, this.nodes[size]);

                // 앞노드 (현재 노드) 수정
                if(node.nodes[1] instanceof LeafNode){
                    int x, y;
                    for (x = 0; x < keyNum; x++) {
                        if (keys[x] < key) continue; // if all keys are smaller than key, i will be keyNum
                        else break;
                    }
                    // move one by one
                    for (y = keyNum - 1; y >= i; y--) { // -1 means index
                        keys[y + 1] = keys[y];
                        nodes[y + 2] = nodes[y + 1];
                    }
                    // add
                    // this.keyNum += 1;
                    keys[x] = key;
                    nodes[x + 1] = node.nodes[1];
                }else if(node.nodes[1] instanceof NonLeafNode){ // ?
                    int x, y;
                    for (x = 0; x < keyNum; x++) {
                        if (keys[x] < key) continue; // if all keys are smaller than key, i will be keyNum
                        else break;
                    }
                    // move one by one
                    for (y = keyNum - 1; y >= i; y--) { // -1 means index
                        keys[y + 1] = keys[y];
                        nodes[y + 2] = nodes[y + 1];
                    }
                    // add
                    // this.keyNum += 1;
                    keys[x] = key;
                    nodes[x + 1] = node.nodes[1];
                }

                // this.add(key, node.nodes[1]); // failed
                //for (int j = this.keyNum - 1; j < size; j++) { } // complicated

                return nonLeafNode1;
            }
            ///////////////////////////////
            return nonLeafNode1;
        } else {
            int i, j; // find where to add
            for (i = 0; i < keyNum; i++) {
                if (keys[i] < key) continue; // if all keys are smaller than key, i will be keyNum
                else break;
            }
            // move one by one
            for (j = keyNum - 1; j >= i; j--) { // -1 means index
                keys[j + 1] = keys[j];
                nodes[j + 2] = nodes[j + 1];
            }
            // add


            //if(this.nodes[0] instanceof LeafNode) {
                this.keyNum += 1;

            // else if(this.nodes[0] instanceof NonLeafNode){ } // 필요없음.

            keys[i] = key;
            nodes[i + 1] = node.nodes[1];

            return null;
        }
    }
}