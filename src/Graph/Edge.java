package Graph;



public class Edge implements Comparable<Edge>{
	private Vertex u,v;
	private int weight;
	/*The order is a value to keep track of their index in the sorted edges list*/
	private int order;
	
	public Edge(Vertex u, Vertex v, int weight){
		this.u = u;
		this.v = v;
		this.weight = weight;
		this.order = 0;
	}
	public boolean isIncident( Vertex v){
		if(this.v == v){
			return true;
		}
		if(this.u == v){
			return true;
		}
		return false;
	}
	
	public Vertex getVertex1(){
		return u;
	}
	public Vertex getVertex2(){
		return v;
	}
	public int getWeight(){
		return weight;
	}
	
	public int getOrder(){
		return order;
	}
	
	public void setOrder(int order){
		this.order = order;
	}

	@Override
	public int compareTo(Edge e) {
		if( this.weight > e.weight ) 
			return 1;
		else if ( this.weight == e.weight)
			return 0;
		else
			return -1;
	}
	public String toString(){
		return "(" + u + "," + v + ") weight : " + weight ;
	}
}
