/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Vrockcm
 */
  public class Vertex 
{
    int value;
    boolean used;
     
    public Vertex(int value,int initlocation)
    {
        this.value = value;
        used=false;
    }
    
    
    public boolean is_used(){
            return used;
    }
    
    public void setUsed(boolean value){
            used = value;
    }
    
}
