import java.lang.reflect.Array;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: asarawgi
 * Date: 10/21/12
 * Time: 1:43 PM
 * To change this template use File | Settings | File Templates.
 */


public class SudokuSolver {

    /*
     * We represent the grid as following
     *
     * A1 A2 A3 A4 | A5 A6 A7 A8| A9 A10 A11 A12
     * B1 B2 B3 B4 | B5 B6 B7 B8| B9 B10 B11 B12
     * C1 C2 C3 C4 | C5 C6 C7 C8| C9 C10 C11 C12
     */


    private ArrayList<HashSet<Integer>> unitList;
    private HashMap<Integer, HashSet<Integer>> units;
    private HashMap<Integer, HashSet<Integer>> peers;


    public SudokuSolver() {

        unitList = new ArrayList<HashSet<Integer>>();
        units = new HashMap<Integer, HashSet<Integer>>();
        peers = new HashMap<Integer, HashSet<Integer>>();

        //ArrayList<HashSet<Integer>> squareUnits = new ArrayList<HashSet<Integer>>();




        @SuppressWarnings("unchecked")
        HashSet<Integer>[][] squareUnits = (HashSet<Integer>[][]) Array.newInstance(HashSet.class, 4, 3);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++)
                squareUnits[i][j] = new HashSet<Integer>();
        }

        for (int i = 0; i < 12; i++) {
            HashSet<Integer> rows = new HashSet<Integer>();
            HashSet<Integer> columns = new HashSet<Integer>();
            for (int j = 0; j < 12; j++) {
                rows.add(i*12 + j);
                columns.add(j * 12 + i);

                squareUnits[i/3][j/4].add(i*12 + j);
            }
            unitList.add(rows);
            unitList.add(columns);
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++)
                unitList.add(squareUnits[i][j]);
        }

        for (int i = 0; i < 12; i ++) {
           for (int j = 0; j < 12; j++) {
               for (HashSet<Integer> unit : unitList) {

                   if (unit.contains(i*12 + j)) {
                       if (units.get(i*12 + j)  == null) {
                           HashSet<Integer> temp = new HashSet<Integer>();
                           units.put(i*12 + j, temp);
                       }
                       HashSet<Integer> temp = units.get(i*12 + j);
                       for (Integer k: unit) {
                           temp.add(k);
                       }
                       units.put(i*12 + j, temp);
                   }
               }
           }
        }

        for (Map.Entry<Integer, HashSet<Integer>> entry : units.entrySet()) {
            Integer key = entry.getKey();
            HashSet<Integer> value = entry.getValue();
            HashSet<Integer> temp = new HashSet<Integer>();
            for (Integer i : value) {
                temp.add(i);
                temp.remove(key);
                peers.put(key, temp);
            }
        }



    }

    private DefaultHashMap<Integer, HashSet<Integer>>
    assign(DefaultHashMap<Integer, HashSet<Integer>> values, int index, int value) {
        // Deep Copy
        if (values == null)
            return null;
        HashSet<Integer> valueSet = new HashSet<Integer>(values.get(index));
        valueSet.remove(value);
        for (Integer i : valueSet) {
            if (eliminate(values, index, i) == null)
                 return null;
        }

        return values;
    }

    private DefaultHashMap<Integer, HashSet<Integer>>
    eliminate(DefaultHashMap<Integer, HashSet<Integer>> values, int index, int value) {
        if (!values.get(index).contains(value))
            return values;
        HashSet<Integer> temp = values.get(index);
        temp.remove(value);
        values.put(index, temp);

        if (values.get(index).isEmpty())
            return null;
        else if (values.get(index).size() == 1) {
            int value2 = (Integer)values.get(index).toArray()[0];
            HashSet<Integer> peerSet = peers.get(index);
            for (Integer peer: peerSet) {
                if (eliminate(values, peer, value2) == null)
                    return null;
            }
        }

        HashSet<Integer> unitSet = units.get(index);
        HashSet<Integer> containsSet  = new HashSet<Integer>();
        for (Integer unit: unitSet) {
            if (values.get(unit).contains(value))
                containsSet.add(unit);
        }

        if (containsSet.isEmpty())
            return null;
        else if (containsSet.size() == 1) {
            int unit = (Integer)containsSet.toArray()[0];
            if (assign(values, unit, value ) == null)
                return null;
        }
        return values;
    }


    DefaultHashMap<Integer, HashSet<Integer>>
    search(DefaultHashMap<Integer, HashSet<Integer>> values) {
        if (values == null)
            return null;
        boolean isSolved = true;
        int minKey = 0;
        int minValue = Integer.MAX_VALUE;
        for (Map.Entry<Integer, HashSet<Integer>> entry: values.entrySet()) {
            int domainSize = entry.getValue().size();

            if ( domainSize != 1) {
                isSolved = false;
            }
            if (domainSize > 1) {
                if (domainSize < minValue) {
                    minValue = domainSize;
                    minKey = entry.getKey();
                }
            }

        }

        if (isSolved)  {
            System.out.println("Yeah Solved");
            return values;
        }

        HashSet<Integer> possibleValues = values.get(minKey);
        for (Integer assignment: possibleValues) {
            DefaultHashMap<Integer, HashSet<Integer>> solvedValue =  search(assign(deepCloneValues(values), minKey, assignment));
            if (solvedValue != null)
                return solvedValue;

        }

        // Shouldn't go here
        return null;
    }

    private DefaultHashMap<Integer, HashSet<Integer>>
    deepCloneValues(DefaultHashMap<Integer, HashSet<Integer>> original) {
        DefaultHashMap<Integer, HashSet<Integer>> cloned = new DefaultHashMap<Integer, HashSet<Integer>>(new HashSet<Integer>());
        for (Map.Entry<Integer, HashSet<Integer>> entry: original.entrySet()) {
            HashSet<Integer> temp = cloned.get(entry.getKey());
            HashSet<Integer> clonedHashSet = new HashSet<Integer>();
            for (Integer i: entry.getValue())
                clonedHashSet.add(i);
            cloned.put(entry.getKey(), clonedHashSet);
        }

        return cloned;
    }

    private DefaultHashMap<Integer, HashSet<Integer>>
    initializeValues() {
        DefaultHashMap<Integer, HashSet<Integer>> values = new DefaultHashMap<Integer, HashSet<Integer>>(new HashSet<Integer>());
        for (int i = 0; i < 144; i++) {
            for (int j= 1; j < 13; j++)    {
                HashSet<Integer> temp = values.get(i);
                temp.add(j);
                values.put(i, temp);
            }


        }
        return values;
    }


    public DefaultHashMap<Integer, HashSet<Integer>>
    parseString(String grid) {
        DefaultHashMap<Integer, HashSet<Integer>> values = initializeValues();
        String[] gridValues = grid.split(",");
        assert gridValues.length == 144;
        for (int i = 0; i < 144; i++) {
            if (!gridValues[i].equals("#")) {
                DefaultHashMap<Integer, HashSet<Integer>> clonedValues = deepCloneValues(values);
                values = assign(clonedValues, i, Integer.parseInt(gridValues[i]));

            }
        }

        return values;
    }
}
