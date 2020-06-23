import java.util.ArrayList;

public class ArrayDim<T> implements Cloneable {
    private final T[] obj;
    private final int[] dims;

    public ArrayDim(T[] obj) {
        this.obj = obj.clone();
        this.dims = calculateDim(this.obj);
    }

    public T[] getObj() {
        return this.obj.clone();
    }

    public int[] getDims() {
        return this.dims.clone();
    }

    public int getDeepestDim() {
        return getDeepestDim(this.obj);
    }

    private int getDeepestDim(T[] obj) {
        int nDims = 0;
        String className = obj.getClass().getName();

        for (char c : className.toCharArray()) {
            if (c == '[') nDims++;
            else break;
        }

        return nDims;
    }

    private int[] calculateDim(T[] obj) {
        if (isFlatArray(obj)) return new int[]{obj.length};

        ArrayList<Integer> dimsLength = new ArrayList<>();
        ArrayList<Integer> currDimLengths = new ArrayList<>();
        ArrayList<int[]> nextDimsLengths = new ArrayList<>();

        for (T t : obj)
            if (isPrimitive(t)) break;
            else {
                currDimLengths.add(obj.length);
                nextDimsLengths.add(calculateDim((T[]) t));
            }

        if (all(currDimLengths)) {
            dimsLength.add(currDimLengths.get(0));

            int minLength = Integer.MAX_VALUE;
            for (int[] nextDimsLength : nextDimsLengths)
                if (minLength > nextDimsLength.length)
                    minLength = nextDimsLength.length;

            for (int j = 0; j < minLength; j++) {
                boolean isEqualDim = true;

                for (int i = 0; i < nextDimsLengths.size() - 1; i++)
                    isEqualDim = isEqualDim && nextDimsLengths.get(i)[j] == nextDimsLengths.get(i + 1)[j];

                if (isEqualDim) dimsLength.add(nextDimsLengths.get(0)[j]);
                else break;
            }
        }

        int[] resDims = new int[dimsLength.size()];
        for (int i = 0; i < dimsLength.size(); i++)
            resDims[i] = dimsLength.get(i);

        return resDims;
    }

    private boolean isFlatArray(T[] obj) {
        return getDeepestDim(obj) == 1;
    }

    private boolean isPrimitive(T obj) {
        return obj.getClass().getName().toCharArray()[0] != '[';
    }

    private boolean all(ArrayList<Integer> arr) {
        if (arr.size() == 0) return false;

        boolean res = true;

        for (int i = 0; i < arr.size() - 1; i++) {
            res = res && arr.get(i).equals(arr.get(i + 1));
        }

        return res;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        if (isFlatArray(this.obj))
            return String.format(this.obj.toString() + " having a length of %s", this.dims[0]);

        String dim = "";
        for (int i = 0; i < this.dims.length; i++)
            dim += this.dims[i] + (i != this.dims.length - 1 ? "x" : "");

        return String.format(this.obj.toString() + " having a %s dimension", dim);
    }
}
