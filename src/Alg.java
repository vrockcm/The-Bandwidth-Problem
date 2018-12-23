
import java.io.File;
import java.io.FileNotFoundException;
import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


/**
 * Please run my program on test files more than once. The solution depends highly on the partial 
 * solution found with the smallest bandwidth and the permutation itself. Sometimes it takes like 0-3 secs on big data files and sometimes
 * it takes more than 2-3 mins on the same big data files. Basically it works when it wants to work. 
 * From what I've seen more the number of edges, the more time it takes.
 * 
 * 
 * Randomizing is done only when my vertex swap improves the distance between both of them.
 * Whenever I use minBandwith(), I select a random vertex from the maxEdge pair found and make 
 * it a pivot vertex(basically the vertex with the max edge)
 * Now I select another random vertex and swap it with my pivot vertex, if this causes the max edge length to go down then I keep the swap
 * else I just swap them back. I do this in a 600 time loop until the max edge improves or 
 * I do it 60 times without finding an improvement(for infinity loop deadlock) for that pivot max edge vertex.
 * 
 * Pruning method:
 * Go through all the vertices not used in the partial solution and if one of them makes a very long
 * edge of some sort with any other vertex in the partial permutation, stop the search, as nothing less than that edge will be found. 
 * 
 * 
 * 
 * This algorithm works best with t,bt and p. Worst on r and gg.
 * Link to datasets : https://www3.cs.stonybrook.edu/~skiena/373/bandwidth/
 * Lowest times found randomly:
 *   Type of Graph   |     Size    |   Partial Solution Bandwidth  |  Min Bandwidth   |   Time of sec
 *       bt             g-bt-20-19              4                       3                    0 sec
 *       gg             g-gg-35-58              7                       5                    7 sec
 *        p             g-p-40-39               4                       1                    0 sec
 *        r             g-r-18-46               8                       7                    1 min 1 sec
 *        r             g-r-20-42               8                       7                    1 min 1 sec
 *        t             g-t-40-39               4                       3                    0 sec
 * 
 * 
 * Highest Solution Found in under 1 min
 * g-p-40-39
 * Now starting to find solution...
 *   Starting minimum:
 *   4
 *   Min is now: 
 *   3
 *   Min is now: 
 *   2
 *   Min is now: 
 *   1
 *   Solution: 
 *   39 17 8 1 32 19 36 31 3 22 15 24 12 37 14 13 4 20 25 35 6 21 40 30 33 2 27 28 9 5 18 34 10 11 38 29 26 23 7 16 
 *   Minimum Bandwidth: 
 *   1
 *   BUILD SUCCESSFUL (total time: 0 seconds)
 * 
 * 
 * g-t-40-39
 * Now starting to find solution...
 *
 *   Starting minimum:
 *   4
 *   Min is now: 
 *   3
 *   Solution: 
 *   26 22 20 19 25 7 6 34 31 9 36 14 30 8 11 35 1 39 27 16 5 4 21 40 23 29 17 28 12 18 3 32 33 13 24 15 10 37 2 38 
 *   Minimum Bandwidth: 
 *   3
 *   BUILD SUCCESSFUL (total time: 0 seconds)
 * 
 * @author Vrockcm
 */


public class Alg {  
    int[]  solution;
    int minBandwidth;
    int vertex_count, edge_count;
    int[] RandomSwaps;
    int[] vertices;
    int[] location;
    int[] used;
    ArrayList<Edge> edges;
    int pivot;
    
    public Alg()
    {
        minBandwidth = Integer.MAX_VALUE;
        edges = new ArrayList<Edge>();
        pivot=0;
    }
    
    void runThru() throws FileNotFoundException{
        File file = new File("test.txt");
        Scanner filesc = new Scanner(file);
        vertex_count=Integer.parseInt(filesc.next());
        edge_count=Integer.parseInt(filesc.next());
        RandomSwaps = new int[vertex_count];
        location = new int[vertex_count];
        vertices = new int[vertex_count];
        solution = new int[vertex_count];
        used = new int[vertex_count];
        for(int i=0;i<vertex_count;i++){
           RandomSwaps[i]=i;
           vertices[i]=i;
           location[i]=i;
           used[i]=0;
        }
        while(filesc.hasNext()){
                int vertex1 = Integer.parseInt(filesc.next());
                int vertex2 = Integer.parseInt(filesc.next());
                edges.add(new Edge(vertex1-1,vertex2-1));
        } 
        shuffleArray(RandomSwaps);
        int oldMaxEdge,newMaxEdge=0;
        for (int i = 0; i < 600; i++) {
            Random random = new Random();
            int v2 = random.nextInt(vertex_count);
            oldMaxEdge = minBandwidth();
            int v1 = location[pivot];
            int count=0;
            do {
                int temp=vertices[v1]; 
                vertices[v1]=vertices[v2]; 
                vertices[v2]=temp;
                location[vertices[v1]]=v1;
                location[vertices[v2]]=v2;
               
                newMaxEdge=minBandwidth();
                if (newMaxEdge>oldMaxEdge) {
                    temp=vertices[v1]; 
                    vertices[v1]=vertices[v2]; 
                    vertices[v2]=temp;
                    location[vertices[v1]]=v1;
                    location[vertices[v2]]=v2;
                }
                count++;
                v2 = random.nextInt(vertex_count);
            } while (newMaxEdge>=oldMaxEdge && count<60);
        }
        minBandwidth = minBandwidth();
        for (int i=0; i<vertex_count; i++) 
            solution[i]=vertices[i];
        System.out.println("Now starting to find solution...\n");
        System.out.println("Starting minimum:\n"+ minBandwidth);
        Iterate(0); 
        System.out.println("Solution: ");
        for (int i=0; i<vertex_count; i++) 
            System.out.print(solution[i]+1+" ");
            System.out.println();
        System.out.println( "Minimum Bandwidth: \n"+ minBandwidth);
    }
   
    public void Iterate(int level){
        int i;
        if (level==vertex_count) 
        {
            int max=minBandwidth();     //if bandwidth decreased then overwrite solution
            if (max<minBandwidth) {
                minBandwidth=max;
                for (i=0; i<vertex_count; i++){
                    solution[i]=vertices[i];
                }
                System.out.println("Min is now: \n"+ minBandwidth);
            }
        } 
        else {
            for (i=0; i<edges.size(); i++) 
            {
                int v1=edges.get(i).v1;
                int v2=edges.get(i).v2;
                if (used[v1] == 1 && used[v2] == 0)           // - check if its one of the vertices 
                    if ( location[v1] <= level-minBandwidth)  // replaced below.   
                            return; //Go back up              // - If this vertex we replaced makes a 
                if (used[v2] == 1 && used[v1] == 0)           // longer edge then what it was earlier
                    if (location[v2] <= level-minBandwidth)   // before we replaced.
                            return; //Go back up              // - If it is then stop looking
            }                                                 // for a solution with that vertex
            for (int k=0; k<vertex_count; k++) 
            {
                i = RandomSwaps[k];
                if (used[i] == 0) 
                {
                    used[i] = 1;        //replace vertex at level with randomly picked vertex
                    vertices[level]=i;
                    location[i]=level;
                    Iterate(level+1);   //this basically checks all positions one by one
                    used[i] = 0;        //if it returns then no good solution exists
                }
            }
        }
    }

    /**
     *go through all the edges and calculate the distance
     * between each pairing in the permutation. 
        Storing the highest one used for this solution.
     * @return
     */
    public int minBandwidth() 
    {
    int diff,maxVertex=0;
        for(int j=0;j<edges.size();j++){
            int v1=edges.get(j).v1;
            int v2=edges.get(j).v2;
            diff = abs(location[v2]-location[v1]); 
            if (diff > maxVertex) 
            { 
                maxVertex = diff;
                pivot=v1; 
                Random random = new Random();
                if (random.nextInt()%2==0) 
                    pivot=v2;
            }
        }
        return maxVertex;
    }
    
    /**
     *This is used to shuffle an array randomly so I can pick a random 
     * vertex based on the index.
     * 
     * @return
     */
    public void shuffleArray(int[] arr) { 
        Random random = new Random();
        random.nextInt();
        for (int i = 0; i < vertex_count; i++) {
            int change = random.nextInt(vertex_count);
            int helper = arr[i];
            arr[i] = arr[change];
            arr[change] = helper;
        }
    }
     
}
