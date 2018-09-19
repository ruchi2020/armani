package com.chelseasystems.rb;

import com.chelseasystems.oi.MethodPathTransferable;
import com.chelseasystems.oi.NodeObject;
import java.awt.Point;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceContext;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.InvalidDnDOperationException;
import java.io.PrintStream;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class JTreeDragabble extends JTree{

	private DragSource dragSource;
	  private DragGestureListener dGListener;
	  private DragSourceListener dSListener;

	  public JTreeDragabble()
	  {
	    init();
	  }

	  public JTreeDragabble(TreeModel treeModel) {
	    super(treeModel);
	    init();
	  }

	  private void init() {
	    this.dragSource = DragSource.getDefaultDragSource();
	    this.dGListener = new DragGestureListener()
	    {
	      public void dragGestureRecognized(DragGestureEvent dge) {
	        JTreeDragabble.this.startDrag(dge);
	      }
	    };
	    this.dSListener = new DSListener();
	    this.dragSource.createDefaultDragGestureRecognizer(this, 3, this.dGListener);
	  }

	  private void startDrag(DragGestureEvent dge)
	  {
	    TreePath path = getPathForLocation(dge.getDragOrigin().x, dge.getDragOrigin().y);
	    if (path == null) {
	      System.out.println("decline to start drag, cursor not over tree node");
	      return;
	    }
	    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)path.getLastPathComponent();
	    NodeObject nodeObject = (NodeObject)selectedNode.getUserObject();
	    if (!nodeObject.sLevel.equals("OBJECT")) {
	      System.out.println("decline to start drag, cursor is not over an OBJECT node : " + nodeObject.sLevel);
	      return;
	    }
	    String className = nodeObject.oLevel.getClass().getName().substring(nodeObject.oLevel.getClass().getName().lastIndexOf(".") + 1);
	    //Vivek Mishra : Added condition to allow ArmCurrency drag.
	    if ((!className.equals("String")) && (!className.equals("Date")) && (!className.equals("Timestamp")) && (!className.equals("Currency")) && (!className.equals("Long")) && (!className.equals("Double")) && (!className.equals("Integer")) && (!className.equals("Boolean")) && (!className.equals("Float")) && (!className.equals("ResourceBundleKey")) && (!className.equals("ArmCurrency")))
	    {
	      System.out.println("decline to start drag, cursor does not point to simple object : " + className);
	      return;
	    }
	    Object[] pathComponents = path.getPath();
	    NodeObject[] nodeObjects = new NodeObject[pathComponents.length];
	    for (int i = 0; i < pathComponents.length; i++) {
	      nodeObjects[i] = ((NodeObject)((DefaultMutableTreeNode)pathComponents[i]).getUserObject());
	    }
	    MethodPath methodPath = new MethodPath();
	    methodPath.setNodeObjects(nodeObjects);
	    MethodPathTransferable transferable = new MethodPathTransferable(methodPath);
	    try {
	      dge.startDrag(DragSource.DefaultCopyNoDrop, transferable, this.dSListener);
	    } catch (InvalidDnDOperationException idoe) {
	      System.out.println("Invalid drag start exception: " + idoe);
	    }
	  }

	  class DSListener implements DragSourceListener
	  {
	    DSListener()
	    {
	    }

	    public void dragEnter(DragSourceDragEvent dsde) {
	      DragSourceContext context = dsde.getDragSourceContext();
	      int myAction = dsde.getDropAction();
	      if ((myAction & 0x1) != 0) {
	        context.setCursor(DragSource.DefaultCopyDrop);
	      }
	      else
	        context.setCursor(DragSource.DefaultCopyNoDrop);
	    }

	    public void dragOver(DragSourceDragEvent dsde)
	    {
	    }

	    public void dropActionChanged(DragSourceDragEvent dsde)
	    {
	    }

	    public void dragExit(DragSourceEvent dse)
	    {
	    }

	    public void dragDropEnd(DragSourceDropEvent dsde)
	    {
	    }
	  }
	  
}
