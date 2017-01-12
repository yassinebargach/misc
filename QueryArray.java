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
                }
            }
            System.out.println("Not found !");
            return;
        }

        HashSet<Integer> elements = new HashSet<>();
        for(int i =0;i<query.length-1;i++){
            elements.add(query[i]);

        }
        elements.add(query[query.length-1]);

        QueryHolder mySuperStructure = new QueryHolder(query);
        for(int i = 0 ; i < array.length;i++){
            if(!elements.contains(array[i])){
                continue;
            }

            mySuperStructure.processValue(array[i],i);

        }

        if(mySuperStructure.completed.size()==0){
            System.out.println("Not Found");
        }
        else {
            Candidate finalCandidat = mySuperStructure.completed.get(0);
            for(int i = 0 ; i<mySuperStructure.completed.size();i++){
                if (finalCandidat.length>mySuperStructure.completed.get(i).length){
                    finalCandidat = mySuperStructure.completed.get(i);
                }
            }
            System.out.println("Winner is : "+finalCandidat.idxStart+ " and "+(finalCandidat.idxStart+finalCandidat.length));
        }




    }

    static class QueryHolder{
        int query[];

        //Todo : candidates can be more smart to avoid looping on all elements
        List<Candidate> candidates=new ArrayList<>();
        List<Candidate> completed=new ArrayList<>();


        public QueryHolder(int query[]) {
            this.query=query;
        }

        public void processValue(int value, int i) {
            for(Iterator<Candidate> iterator = candidates.iterator();iterator.hasNext();){
                Candidate c =iterator.next();
                if(c.neededValue==value){
                    if (++c.positionQuery==query.length){
                        System.out.println("Query found between "+c.idxStart+ " and "+i );
                        c.calculateLength(i);
                        completed.add(c);
                        iterator.remove();
                    }else{
                        c.neededValue=query[c.positionQuery];
                    }
                }
            }
            if(value==query[0]){
                candidates.add(new Candidate(i,query[1]));
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

    static class Pair{
        int idx,value;

        public Pair(int idx, int value) {
            this.idx = idx;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Pair pair = (Pair) o;

            if (idx != pair.idx) return false;
            return value == pair.value;

        }

        @Override
        public int hashCode() {
            int result = idx;
            result = 31 * result + value;
            return result;
        }
    }

}

