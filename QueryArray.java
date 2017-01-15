import java.util.*;

public class QueryArray {


    private static final int ARRAY_SIZE=10000000;
    private static final int QUERY_SIZE=15;
    private static final int LIMIT=100;

    public static void main(String[] args) {
        test(10);


    }

    public static void test(int n){
        int array[] = new int[ARRAY_SIZE];
        int query[] = new int[QUERY_SIZE];

        long average = 0;

        for(int i =0; i<n;i++){

            Random rd = new Random(System.currentTimeMillis());

            for(int j = 0 ;j<ARRAY_SIZE;j++){
                array[j]=rd.nextInt(LIMIT);
            }

            for(int j = 0 ; j<QUERY_SIZE;j++){
                array[j]=rd.nextInt(LIMIT);
            }

            long start = System.currentTimeMillis();
            findQuery(array, query);
            long time = System.currentTimeMillis() - start;
            average+=time;
            System.out.println(i+"th query found in "+time+"ms");
        }
        System.out.println("----------------------------------------");
        System.out.println("Average time is : "+average/n+"ms");

    }

    public static void findQuery(int array[], int query[]) {


        if (query.length == 1) {
            for (int i = 0; i < array.length; i++) {
                if (array[i] == query[0]) {
                    System.out.println("Found in idex " + i);
                    return;
                }
            }
            System.out.println("Not found !");
            return;
        }

        HashSet<Integer> elements = new HashSet<>(query.length);
        for (int i = 0; i < query.length - 1; i++) {
            elements.add(query[i]);

        }
        elements.add(query[query.length - 1]);

        QueryHolder state = new QueryHolder(query, elements.size());
        for (int i = 0; i < array.length; i++) {
            if (!elements.contains(array[i])) {
                continue;
            }

            state.processValue(array[i], i);

        }

        if (state.completed == null) {
            System.out.println("Not Found");
        } else {
            System.out.println("Winner is : " + state.completed.idxStart + " and " + (state.completed.idxStart + state.completed.length));
        }


    }

    static class QueryHolder {
        int query[];


        Map<Integer, Map<Integer, Candidate>> candidates;
        Candidate completed;


        public QueryHolder(int query[], int distinctElements) {
            this.query = query;
            candidates = new HashMap<>(distinctElements);
        }

        public void processValue(int value, int i) {


            ArrayList<Candidate> toMerge = new ArrayList<>();

            if (value == query[0]) {
                Candidate candidate = new Candidate(i, query[1]);
                toMerge.add(candidate);
            }

            Map<Integer, Candidate> needed = candidates.get(value);
            if (needed != null && needed.size() > 0){
                for (Iterator<Map.Entry<Integer, Candidate>> it = needed.entrySet().iterator(); it.hasNext(); ) {
                    Map.Entry<Integer, Candidate> next = it.next();
                    Candidate c = next.getValue();
                    if (next.getKey() + 1 == query.length) {
                        c.calculateLength(i);
                        if (completed == null) {
                            completed = c;
                        } else {
                            if (completed.length > c.length) {
                                completed = c;
                            }
                        }
                        it.remove();
                    } else {
                        it.remove();
                        c.positionQuery++;
                        c.neededValue = query[c.positionQuery];
                        toMerge.add(c);
                    }
                }
            }

            for (Candidate c : toMerge) {
                Map<Integer, Candidate> nextStep = candidates.get(c.neededValue);
                if (nextStep == null) {
                    nextStep = new HashMap<>();
                    candidates.put(c.neededValue, nextStep);
                }
                nextStep.put(c.positionQuery, c);
            }

        }
    }

    static class Candidate {
        int idxStart;
        int neededValue;
        int positionQuery;
        int length = -1;

        public Candidate(int idxStart, int neededValue) {
            this.idxStart = idxStart;
            this.neededValue = neededValue;
            positionQuery = 1;
        }

        public void calculateLength(int indexEnd) {
            length = indexEnd - idxStart;
        }
    }

}

