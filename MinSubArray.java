import java.util.*;
/**
 * Created by yassine on 11/01/17.
 */
public class MinSubArray {

    public static void main(String[] args){
	MinSubArray m = new MinSubArray();
	m.kernelArray();
    }

    public void kernelArray(){

        int t[] = {0,1,2,2,3,1,1,0,2,3,1,1};
        Set<Integer> numbers = new HashSet<>();
        for (int i : t){
            numbers.add(i);
        }


        TreeSet<Candidate> candidates = new TreeSet<>(new Comparator<Candidate>() {
            @Override
            public int compare(Candidate o1, Candidate o2) {
                return o1.length-o2.length;
            }
        });


        Map<Integer,Integer> counts = new HashMap<>();

        int start=0, end=0,i=0;

        while(true){
            addToCounts(counts,t[i]);
            while(counts.get(t[start])>1){
                int count = counts.get(t[start]);
                counts.put(t[start],count-1);
                start++;
            }
            if(counts.keySet().size()==numbers.size()){
                candidates.add(new Candidate(start,end));
            }
            end++;
            i++;
            if(i>=t.length){
                break;
            }

        }
        if(candidates.size()==0){
            System.out.println("Not found !");
            return;
        }

        Candidate chosen = candidates.first();
        System.out.println("From : "+chosen.indexStart+" to : "+chosen.indexEnd+" - length : "+chosen.length);



    }

    void addToCounts(Map<Integer,Integer> counts, int value){
        if(counts.containsKey(value)){
            int count = counts.get(value);
            counts.put(value,count+1);
        }else{
            counts.put(value,1);
        }
    }

    class Candidate {
        final int indexStart , indexEnd, length;

        public Candidate(int start, int end) {
            indexStart = start;
            indexEnd = end;
            length = end - start + 1;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Candidate candidate = (Candidate) o;

            if (indexStart != candidate.indexStart) return false;
            return indexEnd == candidate.indexEnd;

        }

        @Override
        public int hashCode() {
            int result = indexStart;
            result = 31 * result + indexEnd;
            result = 31 * result + length;
            return result;
        }
    }


}
