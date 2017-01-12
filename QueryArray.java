import java.util.*;

public class QueryArray {


    public static void main(String[] args){
        int array[] = {1,2,4,2,1,3,1,4,2,2,3,1,3,4};
        int query[] = {1,2,3};
        findQuery(array,query);
    }

    public static void findQuery(int array[], int query[]){


        if(query.length==1){
            for(int i=0;i< array.length;i++){
                if (array[i]==query[0]) {
                    System.out.println("Found in idex "+ i);
                    return;
                }
            }
            System.out.println("Not found !");
            return;
        }

        HashSet<Integer> elements = new HashSet<>(query.length);
        for(int i =0;i<query.length-1;i++){
            elements.add(query[i]);

        }
        elements.add(query[query.length-1]);

        QueryHolder state = new QueryHolder(query,elements.size());
        for(int i = 0 ; i < array.length;i++){
            if(!elements.contains(array[i])){
                continue;
            }

            state.processValue(array[i],i);

        }

        if(state.completed == null){
            System.out.println("Not Found");
        }
        else {
            System.out.println("Winner is : "+state.completed.idxStart+ " and "+(state.completed.idxStart+state.completed.length));
        }




    }

    static class QueryHolder{
        int query[];


        Map<Integer,Map<Integer,Candidate>> candidates;
        Candidate completed;


        public QueryHolder(int query[], int distinctElements)
        {
            this.query=query;
            candidates  = new HashMap<>(distinctElements);
        }

        public void processValue(int value, int i) {

            if(value==query[0]){
                Candidate candidate = new Candidate(i, query[1]);
                Map<Integer,Candidate> needed = candidates.get(candidate.neededValue);
                if(needed == null){
                    needed = new HashMap<>();
                    candidates.put(candidate.neededValue,needed);
                }
                needed.put(candidate.positionQuery,candidate);
            }

            Map<Integer,Candidate> needed = candidates.get(value);
            if(needed==null || needed.size()==0) return;

            for(Iterator<Map.Entry<Integer,Candidate>> it = needed.entrySet().iterator();it.hasNext();){
                Map.Entry<Integer,Candidate> next = it.next();
                Candidate c = next.getValue();
                if(next.getKey()+1==query.length){
                    System.out.println("Query found between "+c.idxStart+ " and "+i );
                    c.calculateLength(i);
                    if(completed == null){
                        completed=c;
                    }else{
                        if(completed.length>c.length){
                            completed=c;
                        }
                    }
                    it.remove();
                }else{
                    it.remove();
                    c.positionQuery++;
                    c.neededValue=query[c.positionQuery];
                    Map<Integer, Candidate> nextStep = candidates.get(c.neededValue);
                    if(nextStep==null){
                        nextStep = new HashMap<>();
                        candidates.put(c.neededValue,nextStep);
                    }
                    nextStep.put(c.positionQuery,c);
                }
            }
        }
    }

    static class Candidate{
        int idxStart;
        int neededValue;
        int positionQuery;
        int length = -1;

        public Candidate(int idxStart,int neededValue) {
            this.idxStart = idxStart;
            this.neededValue = neededValue;
            positionQuery = 1;
        }

        public void calculateLength(int indexEnd){
            length = indexEnd-idxStart;
        }
    }

}

