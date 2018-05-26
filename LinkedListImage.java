import java.io.*;
import java.util.*;

class listNode {

    /* Data Attributes*/
    public int start;
    public int end;
    public listNode next;
    
    /* constructors */
    public listNode(int s, int e, listNode n) { 
        start =s;
        end = e;
        next = n;
    }
    
    /* getNext*/
    public listNode getNext() {return next;}
    
    /* getElement*/
    public int getStart() {return start;}

    public int getEnd() {return end;}
    
    /*setNext*/
    public void setNext(listNode n) { next = n; }

}


public class LinkedListImage implements CompressedImageInterface {

    private listNode [] arr;
    private int height,width;
    private int[] noOfBlacks;

	public LinkedListImage(String filename)
	{
		try{
            FileInputStream fstream = new FileInputStream(filename);
            Scanner s = new Scanner (fstream);
            width = s.nextInt();
            height = s.nextInt();
            int f = -1;

            boolean[][] myGrid = new boolean[height][width];
            int[] b = new int [height];

            for(int i=0; i< height; i++){
                for(int j=0; j< width; j++){
                    f= s.nextInt();
                    if(f==1)
                        myGrid[i][j]=true;
                    else{
                        myGrid[i][j]=false;
                        b[i]++;
                    }
                }
            }

            myFunc(myGrid, width, height);


        } catch (FileNotFoundException e) {
            System.out.println("FIle not found");
        }
	}

    public LinkedListImage(boolean[][] grid, int width, int height)
    {
		myFunc(grid, height, width);
    }

    private void myFunc(boolean[][] grid, int height1 ,int width1){
        height = height1;
        width = width1;
        noOfBlacks = new int[height];
        arr = new listNode [height];
        listNode head = null;
        int st, e=-1, black=0;

        for(int i=0; i<height; i++){
            for(int j=width-1; j>= 0; j--){
                if(grid[i][j]==false){
                    if(black==0){
                        e=j;
                    }
                black++;
                noOfBlacks[i]++;
                }

            if(j>0){
                if(grid[i][j-1]==true&&grid[i][j]==false){
                    st=j;
                    black = 0;
                    head = new listNode (st, e, head);
                }
            }

            if(j==0&&grid[i][j]==false){
                st=0;
                black=0;

                head = new listNode (st, e, head);
            }


            }

            arr[i] = head;
            head = null;
        }
    }

    public boolean getPixelValue(int x, int y) throws PixelOutOfBoundException
    {
		try{
            if(x<0||x>=height||y<0||y>=width)
                throw new PixelOutOfBoundException("x or y not entered within range");
        

            listNode h = arr[x];

            while(h!=null){

                if(y<h.getStart())
                    return true;

                if(y>=h.getStart() && y<=h.getEnd())
                    return false;

                h = h.getNext();
            }
        } catch (PixelOutOfBoundException e){
            System.out.println("x or y not entered within range");
        }

        return true;
    }

    public void setPixelValue(int x, int y, boolean val) throws PixelOutOfBoundException
    {
		try{
        if(x<0||x>=height||y<0||y>=width)
            throw new PixelOutOfBoundException("x or y not entered within range");

        boolean g = getPixelValue(x,y);
        if(g==val) {
            return;
        }
        else if(g==true&&val==false){
            noOfBlacks[x]++;
            listNode h = arr[x];

            if(h==null){//creator
                arr[x] = new listNode(y,y,null);
                return;
            }

        int kl = 0;

            for(h = arr[x]; h!= null; h = h.getNext()){
                if(h.getStart()>y+1&& kl==0){
                    listNode k = new listNode(y,y,h);
                    arr[x] = k;
                    return;
                }
                else if(h.getStart()==y+1){
                    h.start=h.getStart()-1;
                    return;
                }
                else if(h.getEnd()==(y-1)){
                    if(h.getNext()!=null){
                        if(h.getNext().getStart()==(y+1)){
                            h.end = h.getNext().getEnd();
                            h.setNext(h.getNext().getNext());
                        }
                        else{
                            h.end = h.getEnd()+1;
                        }
                    }
                    else{
                    h.end=h.getEnd()+1;
                    }
                    return;
                }
                else if(h.getEnd() < (y-1)){

                    if(h.getNext()!=null){
                        if(h.getNext().getStart() > (y+1)){
                            listNode v = new listNode(y,y, h.getNext());
                            h.setNext(v);
                            h=h.getNext();
                            return;
                        }
                    }
                    else{
                        listNode z = new listNode(y,y, null);
                        h.setNext(z);
                        return;
                    }
                }

                kl++;
            }
        }
        else if(g==false&&val==true){
            noOfBlacks[x]--;
            listNode h = arr[x],prev = null;
            int kl=0;

            for(h = arr[x]; h!= null; h = h.getNext()){
                if(h.getStart()<=y&&h.getEnd()>=y){
                    if(h.getStart()==h.getEnd()){
                        if(kl==0){
                            arr[x]=h.getNext();
                        }
                        else{
                            prev.setNext(h.getNext());
                        }
                        return;
                    }
                    else{
                        if(h.getStart()==y)
                            h.start+=1;
                        else if(h.getEnd()==y)
                            h.end-=1;
                        else{
                            int lu = h.getEnd();
                            h.end=y-1;
                            listNode nv = new listNode(y+1,lu,h.getNext());
                            h.setNext(nv);
                        }
                        return;
                    }
                }
                kl=1;
                prev = h;
            }

        }
        } catch (PixelOutOfBoundException e){
            System.out.println("x or y not entered within range");
        }
    }

    public int[] numberOfBlackPixels()
    {
		return noOfBlacks;
    }
    
    public void invert()
    {
		for(int i=0; i<height; i++){
            noOfBlacks[i]=width - noOfBlacks[i];
            listNode n = null, prev = arr[i], prev1 = null, head = null,tail = null;
            int ko =0, e=0;

            if(arr[i]!=null){
                for(listNode h = arr[i]; h!= null; h = h.getNext()){
                    
                    if(ko==0){
                        if(h.getStart()!=0){
                            tail = new listNode(0, h.getStart()-1, null);
                            head = tail;
                        }
                        ko++;
                    }
                    else{
                        if(head!=null){
                            tail.setNext(new listNode(e+1, h.getStart()-1, null));
                            tail = tail.getNext();
                        }
                        else{
                            tail = new listNode(e+1, h.getStart()-1, null);
                            head = tail;
                        }
                    }

                    e = h.getEnd();
            
                }

                if(e!=width-1){
                    if(head!=null){
                        tail.setNext(new listNode(e+1, width-1, null));
                        tail = tail.getNext();
                    }
                    else{
                        tail = new listNode(e+1, width-1, null);
                        head = tail;
                    }
                }
                
            }
            else{
                head = new listNode(0, width-1, null); 
            }
            arr[i] = head;
        }
    }
    
    public void performAnd(CompressedImageInterface img) throws BoundsMismatchException
    {
		LinkedListImage img2 = (LinkedListImage) img;

        if(height!=img2.height||width!=img2.width){
            throw new BoundsMismatchException("images are not of same size");
        }

        boolean[][] a = new boolean[height][width];

        for(int i=0; i<height; i++){

            int st = 0, q = 0;

            for(listNode prin = img2.arr[i]; prin!=null; prin = prin.getNext()){
                for(int j=st; j<prin.getStart(); j++){
                    a[i][q]=true;
                    q++;
                }

                for(int j=prin.getStart(); j<=prin.getEnd(); j++){
                    a[i][q]=false;
                    q++;
                }

                st = prin.getEnd() + 1;
            }

            while(q!=width){
                a[i][q]=true;
                q++;
            }

        }

        CompressedImageInterface copyOfImg = new LinkedListImage(a,width,height);
        LinkedListImage zar = (LinkedListImage) copyOfImg;

        zar.invert(); //notA
        this.invert(); //notB
        this.performOr(copyOfImg);// notA + notB
        this.invert();
    }
    
    public void performOr(CompressedImageInterface img) throws BoundsMismatchException
    {
		LinkedListImage img2 = (LinkedListImage) img;
        if(height!=img2.height||width!=img2.width){
            throw new BoundsMismatchException("images are not of same size");
        }

        boolean[][] a = new boolean[height][width];

        for(int i=0; i<height; i++){

            int st = 0, q = 0;

            for(listNode prin = img2.arr[i]; prin!=null; prin = prin.getNext()){
                for(int j=st; j<prin.getStart(); j++){
                    a[i][q]=true;
                    q++;
                }

                for(int k=prin.getStart(); k<=prin.getEnd(); k++){
                    a[i][q]=false;
                    q++;
                }

                st = prin.getEnd() + 1;
            }

            while(q!=width){
                a[i][q]=true;
                q++;
            }

        }

        CompressedImageInterface copyOfImg = new LinkedListImage(a,width,height);
        LinkedListImage zar = (LinkedListImage) copyOfImg;

        for(int i=0; i< height; i++){
            listNode h1 = arr[i], h2 = zar.arr[i], head = null, tail = null;
            noOfBlacks[i]=0;

            while(h1!=null&&h2!=null){
                if(h1.getStart()>=h2.getStart()&&h1.getEnd()>=h2.getEnd()){

                    if(h1.getStart()<=h2.getEnd()){

                        noOfBlacks[i]+=h2.getEnd()-h1.getStart()+1;

                        if(head!=null){
                            tail.setNext(new listNode(h1.getStart(), h2.getEnd(), null));
                            tail = tail.getNext();
                        }
                        else{
                            tail = new listNode(h1.getStart(), h2.getEnd(), null);
                            head = tail;
                        }
                    }

                    h2 = h2.getNext();

                }
                else if(h1.getStart()<=h2.getStart()&&h1.getEnd()<=h2.getEnd()){

                    if(h2.getStart()<=h1.getEnd()){

                        noOfBlacks[i]+=h1.getEnd()-h2.getStart()+1;

                        if(head!=null){
                            tail.setNext(new listNode(h2.getStart(), h1.getEnd(), null));
                            tail = tail.getNext();
                        }
                        else{
                            tail = new listNode(h2.getStart(), h1.getEnd(), null);
                            head = tail;
                        }
                    }

                    h1 = h1.getNext();

                }
                else if(h1.getStart()<=h2.getStart()&&h1.getEnd()>=h2.getEnd()){

                    noOfBlacks[i]+=h2.getEnd()-h2.getStart()+1;

                    if(head!=null){

                        tail.setNext(new listNode(h2.getStart(), h2.getEnd(), null));
                        tail = tail.getNext();
                    }
                    else{
                        tail = new listNode(h2.getStart(), h2.getEnd(), null);
                        head = tail;
                    }

                    h2 = h2.getNext();
                }
                else if(h1.getStart()>=h2.getStart()&&h1.getEnd()<=h2.getEnd()){

                    noOfBlacks[i]+=h1.getEnd()-h1.getStart()+1;

                    if(head!=null){
                        tail.setNext(new listNode(h1.getStart(), h1.getEnd(), null));
                        tail = tail.getNext();
                    }
                    else{
                        tail = new listNode(h1.getStart(), h1.getEnd(), null);
                        head = tail;
                    }

                    h1 = h1.getNext();
                }
            }

                arr[i] = head;
        }
    }
    
    public void performXor(CompressedImageInterface img) throws BoundsMismatchException
    {
		LinkedListImage img2 = (LinkedListImage) img;
        if(height!=img2.height||width!=img2.width){
            throw new BoundsMismatchException("images are not of same size");
        }


        boolean[][] a = new boolean[height][width];

        for(int i=0; i<height; i++){

            int st = 0, q = 0;

            for(listNode prin = arr[i]; prin!=null; prin = prin.getNext()){
                for(int j=st; j<prin.getStart(); j++){
                    a[i][q]=true;
                    q++;
                }

                for(int j=prin.getStart(); j<=prin.getEnd(); j++){
                    a[i][q]=false;
                    q++;
                }

                st = prin.getEnd() + 1;
            }

            while(q!=width){
                a[i][q]=true;
                q++;
            }

        }

        CompressedImageInterface copyOfA = new LinkedListImage(a, width, height);

        for(int i=0; i<height; i++){

            int st = 0, q = 0;

            for(listNode prin = img2.arr[i]; prin!=null; prin = prin.getNext()){
                for(int j=st; j<prin.getStart(); j++){
                    a[i][q]=true;
                    q++;
                }

                for(int j=prin.getStart(); j<=prin.getEnd(); j++){
                    a[i][q]=false;
                    q++;
                }

                st = prin.getEnd() + 1;
            }

            while(q!=width){
                a[i][q]=true;
                q++;
            }

        }

        CompressedImageInterface copyOfImg = new LinkedListImage(a,width,height);

        copyOfA.invert(); //notA
        copyOfA.performAnd(copyOfImg); //notA and B
        copyOfImg.invert(); //notB

        this.performAnd(copyOfImg);
        this.performOr(copyOfA);
    }
    
    public String toStringUnCompressed()
    {
		String str = "";
        str = str + width + " " + height;

        for(int i=0; i<height; i++){

            int st = 0, q = 0;
            str = str  + ",";

            for(listNode prin = arr[i]; prin!=null; prin = prin.getNext()){
                for(int j=st; j<prin.getStart(); j++){
                    str =str + " " + 1;
                    q++;
                }

                for(int j=prin.getStart(); j<=prin.getEnd(); j++){
                    str = str + " " + 0;
                    q++;
                }

                st = prin.getEnd() + 1;
            }

            while(q!=width){
                str = str + " " + 1;
                q++;
            }

        }

        return str;
    }
    
    public String toStringCompressed()
    {
		String str = "";

        str = str + width + " " + height;

        for(int i=0; i<height; i++){
            str = str + ", ";

            for(listNode prin = arr[i]; prin!=null; prin=prin.getNext()){
                str = str + prin.getStart() + " " + prin.getEnd() + " ";
            }

            str = str + "-1";
        }

        return str;
    }

    public static void main(String[] args) {
    	// testing all methods here :
    	boolean success = true;

    	// check constructor from file
    	CompressedImageInterface img1 = new LinkedListImage("sampleInputFile.txt");

    	// check toStringCompressed
    	String img1_compressed = img1.toStringCompressed();
    	String img_ans = "16 16, -1, 5 7 -1, 3 7 -1, 2 7 -1, 2 2 6 7 -1, 6 7 -1, 6 7 -1, 4 6 -1, 2 4 -1, 2 3 14 15 -1, 2 2 13 15 -1, 11 13 -1, 11 12 -1, 10 11 -1, 9 10 -1, 7 9 -1";
    	success = success && (img_ans.equals(img1_compressed));

    	if (!success)
    	{
    		System.out.println("Constructor (file) or toStringCompressed ERROR");
    		return;
    	}

    	// check getPixelValue
    	boolean[][] grid = new boolean[16][16];
    	for (int i = 0; i < 16; i++)
    		for (int j = 0; j < 16; j++)
    		{
                try
                {
        			grid[i][j] = img1.getPixelValue(i, j);                
                }
                catch (PixelOutOfBoundException e)
                {
                    System.out.println("Errorrrrrrrr");
                }
    		}

    	// check constructor from grid
    	CompressedImageInterface img2 = new LinkedListImage(grid, 16, 16);
    	String img2_compressed = img2.toStringCompressed();
    	success = success && (img2_compressed.equals(img_ans));

    	if (!success)
    	{
    		System.out.println("Constructor (array) or toStringCompressed ERROR");
    		return;
    	}

    	// check Xor
        try
        {
        	img1.performXor(img2);       
        }
        catch (BoundsMismatchException e)
        {
            System.out.println("Errorrrrrrrr");
        }
    	for (int i = 0; i < 16; i++)
    		for (int j = 0; j < 16; j++)
    		{
                try
                {
        			success = success && (!img1.getPixelValue(i,j));                
                }
                catch (PixelOutOfBoundException e)
                {
                    System.out.println("Errorrrrrrrr");
                }
    		}

    	if (!success)
    	{
    		System.out.println("performXor or getPixelValue ERROR");
    		return;
    	}

    	// check setPixelValue
    	for (int i = 0; i < 16; i++)
        {
            try
            {
    	    	img1.setPixelValue(i, 0, true);            
            }
            catch (PixelOutOfBoundException e)
            {
                System.out.println("Errorrrrrrrr");
            }
        }

    	// check numberOfBlackPixels
    	int[] img1_black = img1.numberOfBlackPixels();
    	success = success && (img1_black.length == 16);
    	for (int i = 0; i < 16 && success; i++)
    		success = success && (img1_black[i] == 15);
    	if (!success)
    	{
    		System.out.println("setPixelValue or numberOfBlackPixels ERROR");
    		return;
    	}

    	// check invert
        img1.invert();
        for (int i = 0; i < 16; i++)
        {
            try
            {
                success = success && !(img1.getPixelValue(i, 0));            
            }
            catch (PixelOutOfBoundException e)
            {
                System.out.println("Errorrrrrrrr");
            }
        }
        if (!success)
        {
            System.out.println("invert or getPixelValue ERROR");
            return;
        }

    	// check Or
        try
        {
            img1.performOr(img2);        
        }
        catch (BoundsMismatchException e)
        {
            System.out.println("Errorrrrrrrr");
        }
        for (int i = 0; i < 16; i++)
            for (int j = 0; j < 16; j++)
            {
                try
                {
                    success = success && img1.getPixelValue(i,j);
                }
                catch (PixelOutOfBoundException e)
                {
                    System.out.println("Errorrrrrrrr");
                }
            }
        if (!success)
        {
            System.out.println("performOr or getPixelValue ERROR");
            return;
        }

        // check And
        try
        {
            img1.performAnd(img2);    
        }
        catch (BoundsMismatchException e)
        {
            System.out.println("Errorrrrrrrr");
        }
        for (int i = 0; i < 16; i++)
            for (int j = 0; j < 16; j++)
            {
                try
                {
                    success = success && (img1.getPixelValue(i,j) == img2.getPixelValue(i,j));             
                }
                catch (PixelOutOfBoundException e)
                {
                    System.out.println("Errorrrrrrrr");
                }
            }
        if (!success)
        {
            System.out.println("performAnd or getPixelValue ERROR");
            return;
        }

    	// check toStringUnCompressed
        String img_ans_uncomp = "16 16, 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1, 1 1 1 1 1 0 0 0 1 1 1 1 1 1 1 1, 1 1 1 0 0 0 0 0 1 1 1 1 1 1 1 1, 1 1 0 0 0 0 0 0 1 1 1 1 1 1 1 1, 1 1 0 1 1 1 0 0 1 1 1 1 1 1 1 1, 1 1 1 1 1 1 0 0 1 1 1 1 1 1 1 1, 1 1 1 1 1 1 0 0 1 1 1 1 1 1 1 1, 1 1 1 1 0 0 0 1 1 1 1 1 1 1 1 1, 1 1 0 0 0 1 1 1 1 1 1 1 1 1 1 1, 1 1 0 0 1 1 1 1 1 1 1 1 1 1 0 0, 1 1 0 1 1 1 1 1 1 1 1 1 1 0 0 0, 1 1 1 1 1 1 1 1 1 1 1 0 0 0 1 1, 1 1 1 1 1 1 1 1 1 1 1 0 0 1 1 1, 1 1 1 1 1 1 1 1 1 1 0 0 1 1 1 1, 1 1 1 1 1 1 1 1 1 0 0 1 1 1 1 1, 1 1 1 1 1 1 1 0 0 0 1 1 1 1 1 1";
        success = success && (img1.toStringUnCompressed().equals(img_ans_uncomp)) && (img2.toStringUnCompressed().equals(img_ans_uncomp));

        if (!success)
        {
            System.out.println("toStringUnCompressed ERROR");
            return;
        }
        else
            System.out.println("ALL TESTS SUCCESSFUL! YAYY!");
    }
}