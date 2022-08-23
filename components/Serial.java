package components;

public class Serial extends Component {

    public void printData(int data) {
        System.out.print((char) data);
    }

    @Override
    public int access(int address, int data, int flags) {
        printData(data);
        return 0;
    }

}
