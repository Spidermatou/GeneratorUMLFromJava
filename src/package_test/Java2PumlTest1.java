package package_test;

import java.util.*;

/**
 * traitement des types primitifs UMl et des multiplicités
 */


public class Java2PumlTest1
{
}

class A
{
    private int anInt;
    protected short aShort;
    long aLong;
    public byte aByte;

    private final float aFloat = 0.0F;
    protected static double aDouble;
    static final char aChar = ' ';
    public static final boolean aBoolean = true;

    private int anIntFunction(int anIntParam) { return 0; }
    protected short aShortFunction(short aShortParam) { return 0; }
    long aLongFunction(long aLongParam) { return 0; }
    public byte aByteFunction(byte aByteParam) { return 0; }

    private static float aFloatFunction(float aFloatParam) { return 0; }
    protected static double aDoubleFunction(double aDoubleParam) { return 0; }
    static char aCharFunction(char aCharParam) { return ' '; }
    static public boolean aBooleanFunction(boolean aBooleanParam) { return true; }
}

abstract class B
{
    private Integer anInteger;
    protected Short aShort;
    Long aLong;
    public Byte aByte;

    private final Float aFloat = 0.0F;
    protected static Double aDouble;
    static final Character aCharacter = ' ';
    public static final Boolean aBoolean = true;

    private Integer anIntFunction(Integer anIntParam) { return 0; }
    protected Short aShortFunction(Short aShortParam) { return 0; }
    Long aLongFunction(Long aLongParam) { return 0L; }
    public Byte aByteFunction(Byte aByteParam) { return 0; }

    private Float aFloatFunction(Float aFloatParam) { return Float.NaN; }
    protected abstract Double aDoubleFunction(Double aDoubleParam);
    static Character aCharacterFunction(Character aCharacterParam) { return ' '; }
    public static Boolean aBooleanFunction(Boolean aBooleanParam) { return true; }
}

class C
{
    private int[] anIntArray;
    protected Short[] aShortArray;
    long[][] anArrayOfLongArray;
    public Byte[][] anArrayOfByteArray;

    private final List<Float> aFloatList = null;
    protected static List<List<Double>> aListOfDoubleList;
    static final Set<Character> aSetOfCharacter = null;
    public static final Set<List<Boolean>> aSetOfBooleanList = null;

    private int[] anIntArrayFunction(int[] anIntArrayParam) { return null; }
    protected Short[] aShortArrayFunction(Short[] aShortArrayParam) { return null; }
    long[][][] aLong3DArrayFunction(long[][][][] someLongs) { return null; }
    public Byte[][][][][] aByte5DArrayFunction(Byte[][][][][][] someBytes) { return null; }

    private List<Float> aFloatListFunction(List<Float> aFloatListParam) { return null; }
    protected List<List<Double>> aListOfDoubleListFunction(List<List<List<Double>>> someDoubles) { return null; }
    Set<Character> aCharacterSetFunction(Set<Character> aCharacterSetParam) { return null; }
    public Set<List<Boolean>> aSetOfBooleanListFunction(Collection<Set<List<Boolean[][]>[]>> someBooleans) { return null; }
}

abstract class D
{
    private A a;
    B[] bs;
    protected List<C> cs;
    public static List<List<D>[]> ds;

    private A aFunction(A a0, A[] a1, A[][] a2, A[][][] a3) { return null; }
    B[] bFunction(B b0, List<B>[] b1, List<List<B>[]>[] b2, List<List<List<B>[]>[]>[] b3) { return null; }
    protected abstract List<C> cFunction(C c0, List<C[]> c1, List<List<C[]>[]> c2, List<List<List<C[]>[]>[]> c3);
    public static Collection<D> dFunction(D d0, Collection<D> d1, HashSet<D> d2, ArrayList<D> d3, HashMap<C,D> d4) { return null; }
}

abstract class E
{
    private Map<Character, Integer> mapOfInt;
    Map<Short, Double[]> mapOfDoubleArray;
    protected Map<Integer, A> mapOfA;
    public Map<Double, A[]> mapOfArrayOfA;

    private Map<Character, Integer> mapOfIntFunction(Map<Character, Integer> a0, HashMap<Character, Integer[]> a1,
                                                     TreeMap<Character, Integer[][]> a2, Map<Character, Integer[][][]> a3)
    { return null; }
    abstract Map<Short, Double[]> mapOfDoubleArrayFunction(Map<Short, Double[]> a0, Map<Short, List<Double>> a1,
                                                           HashMap<Short, ArrayList<Double[]>> a2,
                                                           TreeMap<Short, Map<Short, TreeSet<Double[]>>> a3);

    protected Map<Short, A> mapOfAFunction(Map<Integer, A> a0, Map<Short, List<A>> a1,
                                                  Map<Short, List<A>[]> a2, Map<Short, HashMap<Short, A>> a3)
    { return null; }
    public static Map<Double, A[]> mapOfArrayOfAFunction(Map<Integer, A[]> a0, Map<Double, List<A[]>> a1,
                                 Map<Double, List<A[]>[]> a2, TreeMap<Double, Map<Double, A[]>> a3)
    { return null; }
}


