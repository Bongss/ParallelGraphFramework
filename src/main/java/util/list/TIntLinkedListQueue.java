package util.list;

import gnu.trove.list.linked.TIntLinkedList;

public class TIntLinkedListQueue extends TIntLinkedList {

    public TIntLinkedListQueue() {
        super();
    }

    public int poll() {
        if (super.size() == 0) {
            return -1;
        } else {
            return super.removeAt(0);
        }
    }
}
