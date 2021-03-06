package processing.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import processing.core.PApplet;

public class IntDict {
    protected int count;
    protected String[] keys;
    protected int[] values;
    private HashMap<String, Integer> indices = new HashMap();

    public IntDict() {
        this.count = 0;
        this.keys = new String[10];
        this.values = new int[10];
    }

    public IntDict(int length) {
        this.count = 0;
        this.keys = new String[length];
        this.values = new int[length];
    }

    public IntDict(BufferedReader reader) {
        String[] lines = PApplet.loadStrings(reader);
        this.keys = new String[lines.length];
        this.values = new int[lines.length];

        for(int i = 0; i < lines.length; ++i) {
            String[] pieces = PApplet.split(lines[i], '\t');
            if (pieces.length == 2) {
                this.keys[this.count] = pieces[0];
                this.values[this.count] = PApplet.parseInt(pieces[1]);
                this.indices.put(pieces[0], this.count);
                ++this.count;
            }
        }

    }

    public IntDict(String[] keys, int[] values) {
        if (keys.length != values.length) {
            throw new IllegalArgumentException("key and value arrays must be the same length");
        } else {
            this.keys = keys;
            this.values = values;
            this.count = keys.length;

            for(int i = 0; i < this.count; ++i) {
                this.indices.put(keys[i], i);
            }

        }
    }

    public IntDict(Object[][] pairs) {
        this.count = pairs.length;
        this.keys = new String[this.count];
        this.values = new int[this.count];

        for(int i = 0; i < this.count; ++i) {
            this.keys[i] = (String)pairs[i][0];
            this.values[i] = (Integer)pairs[i][1];
            this.indices.put(this.keys[i], i);
        }

    }

    public int size() {
        return this.count;
    }

    public void resize(int length) {
        if (length > this.count) {
            throw new IllegalArgumentException("resize() can only be used to shrink the dictionary");
        } else if (length < 1) {
            throw new IllegalArgumentException("resize(" + length + ") is too small, use 1 or higher");
        } else {
            String[] newKeys = new String[length];
            int[] newValues = new int[length];
            PApplet.arrayCopy(this.keys, newKeys, length);
            PApplet.arrayCopy(this.values, newValues, length);
            this.keys = newKeys;
            this.values = newValues;
            this.count = length;
            this.resetIndices();
        }
    }

    public void clear() {
        this.count = 0;
        this.indices = new HashMap();
    }

    private void resetIndices() {
        this.indices = new HashMap(this.count);

        for(int i = 0; i < this.count; ++i) {
            this.indices.put(this.keys[i], i);
        }

    }

    public Iterable<IntDict.Entry> entries() {
        return new Iterable<IntDict.Entry>() {
            public Iterator<IntDict.Entry> iterator() {
                return IntDict.this.entryIterator();
            }
        };
    }

    public Iterator<IntDict.Entry> entryIterator() {
        return new Iterator<IntDict.Entry>() {
            int index = -1;

            public void remove() {
                IntDict.this.removeIndex(this.index);
                --this.index;
            }

            public IntDict.Entry next() {
                ++this.index;
                IntDict.Entry e = IntDict.this.new Entry(IntDict.this.keys[this.index], IntDict.this.values[this.index]);
                return e;
            }

            public boolean hasNext() {
                return this.index + 1 < IntDict.this.size();
            }
        };
    }

    public String key(int index) {
        return this.keys[index];
    }

    protected void crop() {
        if (this.count != this.keys.length) {
            this.keys = PApplet.subset(this.keys, 0, this.count);
            this.values = PApplet.subset(this.values, 0, this.count);
        }

    }

    public Iterable<String> keys() {
        return new Iterable<String>() {
            public Iterator<String> iterator() {
                return IntDict.this.keyIterator();
            }
        };
    }

    public Iterator<String> keyIterator() {
        return new Iterator<String>() {
            int index = -1;

            public void remove() {
                IntDict.this.removeIndex(this.index);
                --this.index;
            }

            public String next() {
                return IntDict.this.key(++this.index);
            }

            public boolean hasNext() {
                return this.index + 1 < IntDict.this.size();
            }
        };
    }

    public String[] keyArray() {
        this.crop();
        return this.keyArray((String[])null);
    }

    public String[] keyArray(String[] outgoing) {
        if (outgoing == null || outgoing.length != this.count) {
            outgoing = new String[this.count];
        }

        System.arraycopy(this.keys, 0, outgoing, 0, this.count);
        return outgoing;
    }

    public int value(int index) {
        return this.values[index];
    }

    public Iterable<Integer> values() {
        return new Iterable<Integer>() {
            public Iterator<Integer> iterator() {
                return IntDict.this.valueIterator();
            }
        };
    }

    public Iterator<Integer> valueIterator() {
        return new Iterator<Integer>() {
            int index = -1;

            public void remove() {
                IntDict.this.removeIndex(this.index);
                --this.index;
            }

            public Integer next() {
                return IntDict.this.value(++this.index);
            }

            public boolean hasNext() {
                return this.index + 1 < IntDict.this.size();
            }
        };
    }

    public int[] valueArray() {
        this.crop();
        return this.valueArray((int[])null);
    }

    public int[] valueArray(int[] array) {
        if (array == null || array.length != this.size()) {
            array = new int[this.count];
        }

        System.arraycopy(this.values, 0, array, 0, this.count);
        return array;
    }

    public int get(String key) {
        int index = this.index(key);
        if (index == -1) {
            throw new IllegalArgumentException("No key named '" + key + "'");
        } else {
            return this.values[index];
        }
    }

    public int get(String key, int alternate) {
        int index = this.index(key);
        return index == -1 ? alternate : this.values[index];
    }

    public void set(String key, int amount) {
        int index = this.index(key);
        if (index == -1) {
            this.create(key, amount);
        } else {
            this.values[index] = amount;
        }

    }

    public void setIndex(int index, String key, int value) {
        if (index >= 0 && index < this.count) {
            this.keys[index] = key;
            this.values[index] = value;
        } else {
            throw new ArrayIndexOutOfBoundsException(index);
        }
    }

    public boolean hasKey(String key) {
        return this.index(key) != -1;
    }

    public void increment(String key) {
        this.add(key, 1);
    }

    public void increment(IntDict dict) {
        for(int i = 0; i < dict.count; ++i) {
            this.add(dict.key(i), dict.value(i));
        }

    }

    public void add(String key, int amount) {
        int index = this.index(key);
        if (index == -1) {
            this.create(key, amount);
        } else {
            int[] var10000 = this.values;
            var10000[index] += amount;
        }

    }

    public void sub(String key, int amount) {
        this.add(key, -amount);
    }

    public void mult(String key, int amount) {
        int index = this.index(key);
        if (index != -1) {
            int[] var10000 = this.values;
            var10000[index] *= amount;
        }

    }

    public void div(String key, int amount) {
        int index = this.index(key);
        if (index != -1) {
            int[] var10000 = this.values;
            var10000[index] /= amount;
        }

    }

    private void checkMinMax(String functionName) {
        if (this.count == 0) {
            String msg = String.format("Cannot use %s() on an empty %s.", functionName, this.getClass().getSimpleName());
            throw new RuntimeException(msg);
        }
    }

    public int minIndex() {
        if (this.count == 0) {
            return -1;
        } else {
            int index = 0;
            int value = this.values[0];

            for(int i = 1; i < this.count; ++i) {
                if (this.values[i] < value) {
                    index = i;
                    value = this.values[i];
                }
            }

            return index;
        }
    }

    public String minKey() {
        this.checkMinMax("minKey");
        int index = this.minIndex();
        return index == -1 ? null : this.keys[index];
    }

    public int minValue() {
        this.checkMinMax("minValue");
        return this.values[this.minIndex()];
    }

    public int maxIndex() {
        if (this.count == 0) {
            return -1;
        } else {
            int index = 0;
            int value = this.values[0];

            for(int i = 1; i < this.count; ++i) {
                if (this.values[i] > value) {
                    index = i;
                    value = this.values[i];
                }
            }

            return index;
        }
    }

    public String maxKey() {
        int index = this.maxIndex();
        return index == -1 ? null : this.keys[index];
    }

    public int maxValue() {
        this.checkMinMax("maxIndex");
        return this.values[this.maxIndex()];
    }

    public int sum() {
        long amount = this.sumLong();
        if (amount > 2147483647L) {
            throw new RuntimeException("sum() exceeds 2147483647, use sumLong()");
        } else if (amount < -2147483648L) {
            throw new RuntimeException("sum() less than -2147483648, use sumLong()");
        } else {
            return (int)amount;
        }
    }

    public long sumLong() {
        long sum = 0L;

        for(int i = 0; i < this.count; ++i) {
            sum += (long)this.values[i];
        }

        return sum;
    }

    public int index(String what) {
        Integer found = (Integer)this.indices.get(what);
        return found == null ? -1 : found;
    }

    protected void create(String what, int much) {
        if (this.count == this.keys.length) {
            this.keys = PApplet.expand(this.keys);
            this.values = PApplet.expand(this.values);
        }

        this.indices.put(what, this.count);
        this.keys[this.count] = what;
        this.values[this.count] = much;
        ++this.count;
    }

    public int remove(String key) {
        int index = this.index(key);
        if (index == -1) {
            throw new NoSuchElementException("'" + key + "' not found");
        } else {
            int value = this.values[index];
            this.removeIndex(index);
            return value;
        }
    }

    public int removeIndex(int index) {
        if (index >= 0 && index < this.count) {
            int value = this.values[index];
            this.indices.remove(this.keys[index]);

            for(int i = index; i < this.count - 1; ++i) {
                this.keys[i] = this.keys[i + 1];
                this.values[i] = this.values[i + 1];
                this.indices.put(this.keys[i], i);
            }

            --this.count;
            this.keys[this.count] = null;
            this.values[this.count] = 0;
            return value;
        } else {
            throw new ArrayIndexOutOfBoundsException(index);
        }
    }

    public void swap(int a, int b) {
        String tkey = this.keys[a];
        int tvalue = this.values[a];
        this.keys[a] = this.keys[b];
        this.values[a] = this.values[b];
        this.keys[b] = tkey;
        this.values[b] = tvalue;
    }

    public void sortKeys() {
        this.sortImpl(true, false, true);
    }

    public void sortKeysReverse() {
        this.sortImpl(true, true, true);
    }

    public void sortValues() {
        this.sortValues(true);
    }

    public void sortValues(boolean stable) {
        this.sortImpl(false, false, stable);
    }

    public void sortValuesReverse() {
        this.sortValuesReverse(true);
    }

    public void sortValuesReverse(boolean stable) {
        this.sortImpl(false, true, stable);
    }

    protected void sortImpl(final boolean useKeys, final boolean reverse, final boolean stable) {
        Sort s = new Sort() {
            public int size() {
                return IntDict.this.count;
            }

            public int compare(int a, int b) {
                int diff = false;
                int diffx;
                if (useKeys) {
                    diffx = IntDict.this.keys[a].compareToIgnoreCase(IntDict.this.keys[b]);
                    if (diffx == 0) {
                        diffx = IntDict.this.values[a] - IntDict.this.values[b];
                    }
                } else {
                    diffx = IntDict.this.values[a] - IntDict.this.values[b];
                    if (diffx == 0 && stable) {
                        diffx = IntDict.this.keys[a].compareToIgnoreCase(IntDict.this.keys[b]);
                    }
                }

                return reverse ? -diffx : diffx;
            }

            public void swap(int a, int b) {
                IntDict.this.swap(a, b);
            }
        };
        s.run();
        this.resetIndices();
    }

    public FloatDict getPercent() {
        double sum = (double)this.sum();
        FloatDict outgoing = new FloatDict();

        for(int i = 0; i < this.size(); ++i) {
            double percent = (double)this.value(i) / sum;
            outgoing.set(this.key(i), (float)percent);
        }

        return outgoing;
    }

    public IntDict copy() {
        IntDict outgoing = new IntDict(this.count);
        System.arraycopy(this.keys, 0, outgoing.keys, 0, this.count);
        System.arraycopy(this.values, 0, outgoing.values, 0, this.count);

        for(int i = 0; i < this.count; ++i) {
            outgoing.indices.put(this.keys[i], i);
        }

        outgoing.count = this.count;
        return outgoing;
    }

    public void print() {
        for(int i = 0; i < this.size(); ++i) {
            System.out.println(this.keys[i] + " = " + this.values[i]);
        }

    }

    public void save(File file) {
        PrintWriter writer = PApplet.createWriter(file);
        this.write(writer);
        writer.close();
    }

    public void write(PrintWriter writer) {
        for(int i = 0; i < this.count; ++i) {
            writer.println(this.keys[i] + "\t" + this.values[i]);
        }

        writer.flush();
    }

    public String toJSON() {
        StringList items = new StringList();

        for(int i = 0; i < this.count; ++i) {
            items.append(JSONObject.quote(this.keys[i]) + ": " + this.values[i]);
        }

        return "{ " + items.join(", ") + " }";
    }

    public String toString() {
        return this.getClass().getSimpleName() + " size=" + this.size() + " " + this.toJSON();
    }

    public class Entry {
        public String key;
        public int value;

        Entry(String key, int value) {
            this.key = key;
            this.value = value;
        }
    }
}
