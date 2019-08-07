package huff;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


class Node // ������ Ʈ��, �� �������� ���̴� ��� ��ü
{
	public int freq;
	public char alphabet;
	public Node leftNode;
	public Node rightNode;

	public Node(int freq, char alphabet)
	{
		this.freq = freq;
		this.alphabet = alphabet;
		leftNode = rightNode = null;
	}
}

class MinHeap // �ּ� ��
{
	// �ּ����� �� �� �ִ� ������ �ִ� ������ �ҹ��� 26�� + �빮�� 26�� + �����̽� 1�� + �������� 1 = 54
	private ArrayList<Node> tree = new ArrayList<Node>(54);   

	public MinHeap()
	{
		tree.add(null); // ù ���� ����
	}
	public void insert(Node n)
	{
		tree.add(n);

		int childPos = tree.size()-1;
		int parentPos = childPos/2;

		// freq�� �������� heap ����
		while(parentPos >= 1 && tree.get(childPos).freq < tree.get(parentPos).freq)
		{
			Collections.swap(tree, childPos, parentPos);

			childPos = parentPos;
			parentPos = childPos/2;
		}
	}
	// heap�� ��������� true
	public boolean isEmpty()
	{
		return (tree.size() <= 1);
	}
	// �ּ� ��� ��ȯ.
	public Node extractMinNode()
	{
		if(isEmpty()) return null; // heap�� ������� ���

		Node min = tree.get(1);

		int top = tree.size()-1;

		tree.set(1, tree.get(top));
		tree.remove(top); // �Ǹ����� ���ҷ� ��ü

		int parentPos = 1;
		int leftPos = parentPos*2;
		int rightPos = parentPos*2 + 1;

		// ���� �ڽ��� �ִ� ��쿡�� ������ �����. ���� ���� ���� Ʈ���̱� ������
		// ���� �ڽ��� ������ ������ �ڽĵ� ���ٴ� ���̱� �����̴�.
		while(leftPos <= tree.size()-1)
		{
			int targetPos;
			if(rightPos > tree.size()-1) // ������ �ڽ��� ���� ���
			{
				if(tree.get(leftPos).freq >= tree.get(parentPos).freq) // ���� �ڽ��� �� ũ�� for ����
					break;
				targetPos = leftPos;
			}
			else // ���� ������ ���� �ִ� ���
			{
				if(tree.get(leftPos).freq >= tree.get(parentPos).freq &&
						tree.get(rightPos).freq >= tree.get(parentPos).freq)
						break; // �� �ڽ� ��尡 �� ũ�ų� ������ while�� ����

				// �� ���� ���� swap
				targetPos = (tree.get(leftPos).freq < tree.get(rightPos).freq) ? leftPos : rightPos;
			}

			Collections.swap(tree, targetPos, parentPos);

			// top-down ��ȸ
			parentPos = targetPos;
			leftPos = parentPos*2;
			rightPos = parentPos*2 + 1;
		}
		return min;
	}
	public void printTree()
	{
		for(Node n : tree)
			if(n != null)
				System.out.print(n.freq+" ");
		System.out.println("");
	}
}
public class huffc {
	// ���ĺ� �󵵼� �����ϴ� HashMap
	public static HashMap<Character, Integer> freq = new HashMap<Character, Integer>();
	public static Node huffmanTree=null;
	public static int bitotal;
	public static int chartotal;

	// ���ĺ� �󵵼� count�ϴ� �Լ�
	public static void countAlphabetFrequency(String src)
	{
		try {
		  //���� �о����
	      BufferedReader in = new BufferedReader(new FileReader(src));
	      String s;

	      while ((s = in.readLine()) != null) {
	    	  	// ������ ������ �ϳ��� ���ϴ�.
	    	  	for(int i=0;i<s.length();i++)
	    	  	{
	    	  		char c = s.charAt(i);
	    	  		if(freq.containsKey(c)) freq.put(c, freq.get(c)+1);
	    	  		else freq.put(c, 1);
	    	  		 chartotal++;
	    	  	}
	      }
	      in.close();
	    } catch (IOException e) {
	        System.err.println(e); // ������ �ִٸ� �޽��� ���
	        System.exit(1);
	    }
	}
	public static void makeHuffmanTree()
	{
		MinHeap mh = new MinHeap();

		if(freq.isEmpty()) // �� �� �� ���� ������ null�� return
			return;

		// �ּ� ���� ������ �� �� �� ���ĺ��� �����մϴ�.
		for(char key : freq.keySet())
			mh.insert(new Node(freq.get(key), key));

		while(true)
		{
			// �ּ� ��� 2�� ����
			Node leftChild = mh.extractMinNode();
			Node rightChild = mh.extractMinNode();

			// ���ο� �θ� ��带 ����ϴ�. �θ� ����� freq�� ���� �ڽ��� freq�� �� �Դϴ�.
			huffmanTree = new Node(leftChild.freq+rightChild.freq, '.');

			huffmanTree.leftNode = leftChild;
			huffmanTree.rightNode = rightChild;

			if(mh.isEmpty()) return; // ���� ��������� huffman Ʈ���� �ϼ�.

			mh.insert(huffmanTree);
		}
	}
	// HuffmanTree�� root�� ������ ���� ������ �ڵ带 ������ݴϴ�.
	// trace�� Ʈ���� �����ϱ� ���� �迭�Դϴ�.
	public static void printEachCharacterCode(Node htRoot, int []trace, int top)
	{
		// left�� Ž���� ��� 0�� ����ϰ�, right�� Ž���� ��� 1�� ����� �մϴ�.
		// �ܸ� ��带 ������ ���, ��, left right ��� null�� ��� �ܸ� ����� character�� ����մϴ�.
		if(htRoot.leftNode != null)
		{
			trace[top] = 0;
			printEachCharacterCode(htRoot.leftNode, trace, top+1);
		}
		if(htRoot.rightNode != null)
		{
			trace[top] = 1;
			printEachCharacterCode(htRoot.rightNode, trace, top+1);
		}

		if(htRoot.leftNode == null && htRoot.rightNode == null) // �ܸ� ��带 ������ ���
		{
			System.out.print(htRoot.alphabet + "(�� ��: " + htRoot.freq +"): ");
			bitotal+=printArr(trace, top)*htRoot.freq;
		}
	}
	public static int printArr(int[] arr, int top)
	{
		//���� ��ü ����
		
		int total = 0;
          
        	for(int i=0;i<top;i++) {
        		
    			System.out.print(arr[i]);
    			total++;
    			
    		
        	}
        	System.out.println("");
            
        	return total;
        

	}

	public static void printFreq()
	{
		for(char key : freq.keySet())
			System.out.println("key: " + key + ", freq: " + freq.get(key));
	}
	
	
	public static void main(String[] args)
	{
	
		long tt = System.currentTimeMillis();
		makeHuffmanTree();
		
	
		
	}
}