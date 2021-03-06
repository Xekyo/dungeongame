package test;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.LinkedList;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import dg.*;

public class GridTest {
	Gameboard board;
	Coordinates center;
	Coordinates topLeft;
	Coordinates topRight;
	Coordinates left;
	Coordinates right;
	Coordinates bottomLeft;
	Coordinates bottomRight;
	Coordinates viewPoint;
	Coordinates outOfBounds;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	/*-
	 * Grid is:
	 *   F F
	 *  F W W
	 *   F V
	 *   
	 * V is viewPoint.
	 */
	@Before
	public void setUp() {
		board = new Gameboard();

		center = new Coordinates(0, 0);
		topLeft = new Coordinates(center, Direction.TOPLEFT);
		topRight = new Coordinates(center, Direction.TOPRIGHT);
		left = new Coordinates(center, Direction.LEFT);
		right = new Coordinates(center, Direction.RIGHT);
		bottomLeft = new Coordinates(center, Direction.BOTTOMLEFT);
		bottomRight = new Coordinates(center, Direction.BOTTOMRIGHT);
		viewPoint = bottomRight;
		outOfBounds = new Coordinates(0, 2); // NOT added to grid

		board.addField(topLeft, Terrain.FLOOR);
		board.addField(topRight, Terrain.FLOOR);
		board.addField(left, Terrain.FLOOR);
		board.addField(center, Terrain.WALL);
		board.addField(right, Terrain.WALL);
		board.addField(bottomLeft, Terrain.FLOOR);
		board.addField(viewPoint, Terrain.FLOOR);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void addFieldTest() {
		board = new Gameboard();
		assertEquals("Center was not added, should be out of bounds.", false, board.isInBounds(center));
		board.addField(center, Terrain.WALL);
		assertEquals("Center was added, should be in bounds.", true, board.isInBounds(center));
	}

	@Test
	public void isInBoundsTest() {
		assertEquals("center was added to board, should be in bounds", true, board.isInBounds(center));
		assertEquals("left was added to board, should be in bounds", true, board.isInBounds(left));
		assertEquals("outOfBounds should be not in-bounds", false, board.isInBounds(outOfBounds));

	}

	@Test
	public void getNeighborsTest() {
		LinkedList<Coordinates> bottomRightNeighbors = board.getNeighbors(viewPoint);
		assertEquals("bottomRight should have three neighbors", 3, bottomRightNeighbors.size());
		assertEquals("bottomLeft should be neighbor of bottomRight", true, bottomRightNeighbors.contains(bottomLeft));
		assertEquals("center should be neighbor of bottomRight", true, bottomRightNeighbors.contains(center));
		assertEquals("right should be neighbor of bottomRight", true, bottomRightNeighbors.contains(right));

		LinkedList<Coordinates> centerNeighbors = board.getNeighbors(center);
		assertEquals("center should have six neighbors", 6, centerNeighbors.size());
		assertEquals("bottomLeft should be neighbor of center", true, centerNeighbors.contains(bottomLeft));
		assertEquals("right should be neighbor of center", true, centerNeighbors.contains(right));
		assertEquals("bottomRight should be neighbor of center", true, centerNeighbors.contains(bottomRight));
		assertEquals("left should be neighbor of center", true, centerNeighbors.contains(left));
		assertEquals("topRight should be neighbor of center", true, centerNeighbors.contains(topRight));
		assertEquals("topLeft should be neighbor of center", true, centerNeighbors.contains(topLeft));

		exception.expect(IndexOutOfBoundsException.class);
		board.getNeighbors(outOfBounds);
	}

	@Test
	public void isVisibleTest() {
		assertEquals("topLeft is out of sight", false, board.isVisible(viewPoint, topLeft));
		assertEquals("topRight is out of sight", false, board.isVisible(viewPoint, topRight));
		assertEquals("left is visible", true, board.isVisible(viewPoint, left));
		assertEquals("center is visible", true, board.isVisible(viewPoint, center));
		assertEquals("right is visible", true, board.isVisible(viewPoint, right));
		assertEquals("bottomLeft is visible", true, board.isVisible(viewPoint, bottomLeft));

		exception.expect(IndexOutOfBoundsException.class);
		board.isVisible(viewPoint, outOfBounds);
	}
	

	@Test
	public void isVisibleAlongTouchingWallTest() {
		board = new Gameboard();
		board.addField(viewPoint, Terrain.FLOOR);
		board.addField(center, Terrain.FLOOR);
		board.addField(topRight, Terrain.FLOOR);
		
		assertEquals(true, board.isVisible(viewPoint, topRight));
	}
	
	@Test
	public void tinyFieldOfViewTest() {
		HashSet<Coordinates> visibleFields = board.getFieldOfView(viewPoint);
		assertEquals(5, visibleFields.size());
		assertEquals(true, visibleFields.contains(viewPoint));
		assertEquals(true, visibleFields.contains(bottomLeft));
		assertEquals(true, visibleFields.contains(left));
		assertEquals(true, visibleFields.contains(center));
		assertEquals(true, visibleFields.contains(right));
		assertEquals(false, visibleFields.contains(topLeft));
		assertEquals(false, visibleFields.contains(topRight));		
	}
	
	@Test
	public void longRangeViewTest() {
		board = GameBoardUtils.boardGenerator("F F F F W F W F F $ F F F F F W W F F F $ F W F W F F F F F F F $ F W W W W W F W F F $ W F F W W W W W F $");
		viewPoint = new Coordinates(7, 0);
		HashSet<Coordinates> visibleFields = board.getFieldOfView(viewPoint);
		

		assertEquals(false, board.isVisible(viewPoint, new Coordinates(1,-1)));
		
		assertEquals(false, visibleFields.contains(new Coordinates(3,-1)));
		assertEquals(false, visibleFields.contains(new Coordinates(2,-1)));
		assertEquals(false, visibleFields.contains(new Coordinates(1,-1)));
		assertEquals(false, visibleFields.contains(new Coordinates(0,0)));
		assertEquals(true, visibleFields.contains(new Coordinates(1,0)));
		assertEquals(true, visibleFields.contains(new Coordinates(2,0)));
		assertEquals(true, visibleFields.contains(new Coordinates(3,0)));
		assertEquals(true, visibleFields.contains(new Coordinates(4,0)));
		assertEquals(true, visibleFields.contains(new Coordinates(5,0)));
		assertEquals(true, visibleFields.contains(new Coordinates(6,0)));
		assertEquals(true, visibleFields.contains(new Coordinates(2,0)));
		
	}

}
