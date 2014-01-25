/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) radix(10) lradix(10) 
// Source File Name:   ManhattanConnectionRouter.java

package ncmdp.router;

/**
 * 曼哈顿直角的连线图形
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.AbstractRouter;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Ray;
import org.eclipse.draw2d.geometry.Rectangle;

@SuppressWarnings("deprecation")
public final class MyManhattanConnectionRouter extends AbstractRouter
{
    private class ReservedInfo
    {

        public List<Integer> reservedRows;
        public List<Integer> reservedCols;

        private ReservedInfo()
        {
            reservedRows = new ArrayList<Integer>(2);
            reservedCols = new ArrayList<Integer>(2);
        }

        ReservedInfo(ReservedInfo reservedinfo)
        {
            this();
        }
    }


    public MyManhattanConnectionRouter()
    {
        rowsUsed = new HashMap<Integer, Integer>();
        colsUsed = new HashMap<Integer, Integer>();
        reservedInfo = new HashMap<Connection, ReservedInfo>();
    }

    public void invalidate(Connection connection)
    {
        removeReservedLines(connection);
    }

    private int getColumnNear(Connection connection, int r, int n, int x)
    {
        int min = Math.min(n, x);
        int max = Math.max(n, x);
        if(min > r)
        {
            max = min;
            min = r - (min - r);
        }
        if(max < r)
        {
            min = max;
            max = r + (r - max);
        }
        int proximity = 0;
        int direction = -1;
        if(r % 2 == 1)
            r--;
        while(proximity < r) 
        {
            Integer i = Integer.valueOf(r + proximity * direction);
            if(!colsUsed.containsKey(i))
            {
                colsUsed.put(i, i);
                reserveColumn(connection, i);
                return i.intValue();
            }
            int j = i.intValue();
            if(j <= min)
                return j + 2;
            if(j >= max)
                return j - 2;
            if(direction == 1)
            {
                direction = -1;
            } else
            {
                direction = 1;
                proximity += 25;
            }
        }
        return r;
    }

    protected Ray getDirection(Rectangle r, Point p)
    {
        int distance = Math.abs(r.x - p.x);
        Ray direction = LEFT;
        int i = Math.abs(r.y - p.y);
        if(i <= distance)
        {
            distance = i;
            direction = UP;
        }
        i = Math.abs(r.bottom() - p.y);
        if(i <= distance)
        {
            distance = i;
            direction = DOWN;
        }
        i = Math.abs(r.right() - p.x);
        if(i < distance)
        {
            distance = i;
            direction = RIGHT;
        }
        return direction;
    }

    protected Ray getEndDirection(Connection conn)
    {
        ConnectionAnchor anchor = conn.getTargetAnchor();
        Point p = getEndPoint(conn);
        Rectangle rect;
        if(anchor.getOwner() == null)
        {
            rect = new Rectangle(p.x - 1, p.y - 1, 2, 2);
        } else
        {
            rect = conn.getTargetAnchor().getOwner().getBounds().getCopy();
            conn.getTargetAnchor().getOwner().translateToAbsolute(rect);
        }
        return getDirection(rect, p);
    }

    protected int getRowNear(Connection connection, int r, int n, int x)
    {
        int min = Math.min(n, x);
        int max = Math.max(n, x);
        if(min > r)
        {
            max = min;
            min = r - (min - r);
        }
        if(max < r)
        {
            min = max;
            max = r + (r - max);
        }
        int proximity = 0;
        int direction = -1;
        if(r % 2 == 1)
            r--;
        while(proximity < r) 
        {
            Integer i = Integer.valueOf(r + proximity * direction);
            if(!rowsUsed.containsKey(i))
            {
                rowsUsed.put(i, i);
                reserveRow(connection, i);
                return i.intValue();
            }
            int j = i.intValue();
            if(j <= min)
                return j + 2;
            if(j >= max)
                return j - 2;
            if(direction == 1)
            {
                direction = -1;
            } else
            {
                direction = 1;
                proximity += 25;
            }
        }
        return r;
    }

    protected Ray getStartDirection(Connection conn)
    {
        ConnectionAnchor anchor = conn.getSourceAnchor();
        Point p = getStartPoint(conn);
        Rectangle rect;
        if(anchor.getOwner() == null)
        {
            rect = new Rectangle(p.x - 1, p.y - 1, 2, 2);
        } else
        {
            rect = conn.getSourceAnchor().getOwner().getBounds().getCopy();
            conn.getSourceAnchor().getOwner().translateToAbsolute(rect);
        }
        return getDirection(rect, p);
    }

    protected void processPositions(Ray start, Ray end, List<Integer> positions, boolean horizontal, Connection conn)
    {
        removeReservedLines(conn);
        int pos[] = new int[positions.size() + 2];
        if(horizontal)
            pos[0] = start.x;
        else
            pos[0] = start.y;
        int i;
        for(i = 0; i < positions.size(); i++)
            pos[i + 1] = ((Integer)positions.get(i)).intValue();

        if(horizontal == (positions.size() % 2 == 1))
            pos[++i] = end.x;
        else
            pos[++i] = end.y;
        PointList points = new PointList();
        points.addPoint(new Point(start.x, start.y));
        for(i = 2; i < pos.length - 1; i++)
        {
            horizontal = !horizontal;
            int prev = pos[i - 1];
            int current = pos[i];
            boolean adjust = i != pos.length - 2;
            Point p;
            if(horizontal)
            {
                if(adjust)
                {
                    int min = pos[i - 2];
                    int max = pos[i + 2];
                    pos[i] = current = getRowNear(conn, current, min, max);
                }
                p = new Point(prev, current);
            } else
            {
                if(adjust)
                {
                    int min = pos[i - 2];
                    int max = pos[i + 2];
                    pos[i] = current = getColumnNear(conn, current, min, max);
                }
                p = new Point(current, prev);
            }
            points.addPoint(p);
        }

        points.addPoint(new Point(end.x, end.y));
        conn.setPoints(points);
    }

    public void remove(Connection connection)
    {
        removeReservedLines(connection);
    }

    protected void removeReservedLines(Connection connection)
    {
        ReservedInfo rInfo = (ReservedInfo)reservedInfo.get(connection);
        if(rInfo == null)
            return;
        for(int i = 0; i < rInfo.reservedRows.size(); i++)
            rowsUsed.remove(rInfo.reservedRows.get(i));

        for(int i = 0; i < rInfo.reservedCols.size(); i++)
            colsUsed.remove(rInfo.reservedCols.get(i));

        reservedInfo.remove(connection);
    }

    protected void reserveColumn(Connection connection, Integer column)
    {
        ReservedInfo info = (ReservedInfo)reservedInfo.get(connection);
        if(info == null)
        {
            info = new ReservedInfo(null);
            reservedInfo.put(connection, info);
        }
        info.reservedCols.add(column);
    }

    protected void reserveRow(Connection connection, Integer row)
    {
        ReservedInfo info = (ReservedInfo)reservedInfo.get(connection);
        if(info == null)
        {
            info = new ReservedInfo(null);
            reservedInfo.put(connection, info);
        }
        info.reservedRows.add(row);
    }

    public void route(Connection conn)
    {
        if(conn.getSourceAnchor() == null || conn.getTargetAnchor() == null)
            return;
        Point startPoint = getStartPoint(conn);
        conn.translateToRelative(startPoint);
        Point endPoint = getEndPoint(conn);
        conn.translateToRelative(endPoint);
        Ray start = new Ray(startPoint);
        Ray end = new Ray(endPoint);
        Ray average = start.getAveraged(end);
        Ray direction = new Ray(start, end);
        Ray startNormal = getStartDirection(conn);
        Ray endNormal = getEndDirection(conn);
        List<Integer> positions = new ArrayList<Integer>(5);
        boolean horizontal = startNormal.isHorizontal();
        if(horizontal)
            positions.add(Integer.valueOf(start.y));
        else
            positions.add(Integer.valueOf(start.x));
        horizontal = !horizontal;
        if(startNormal.dotProduct(endNormal) == 0)
        {
            if(startNormal.dotProduct(direction) < 0 || endNormal.dotProduct(direction) > 0)
            {
                int i;
                if(startNormal.dotProduct(direction) < 0)
                    i = startNormal.similarity(start.getAdded(startNormal.getScaled(10)));
                else
                if(horizontal)
                    i = average.y;
                else
                    i = average.x;
                positions.add(Integer.valueOf(i));
                horizontal = !horizontal;
                if(endNormal.dotProduct(direction) > 0)
                    i = endNormal.similarity(end.getAdded(endNormal.getScaled(10)));
                else
                if(horizontal)
                    i = average.y;
                else
                    i = average.x;
                positions.add(Integer.valueOf(i));
                horizontal = !horizontal;
            }
        } else
        if(startNormal.dotProduct(endNormal) > 0)
        {
            int i;
            if(startNormal.dotProduct(direction) >= 0)
                i = startNormal.similarity(start.getAdded(startNormal.getScaled(10)));
            else
                i = endNormal.similarity(end.getAdded(endNormal.getScaled(10)));
            positions.add(Integer.valueOf(i));
            horizontal = !horizontal;
        } else
        {
            int i;
            if(startNormal.dotProduct(direction) < 0)
            {
                i = startNormal.similarity(start.getAdded(startNormal.getScaled(10)));
                positions.add(Integer.valueOf(i));
                horizontal = !horizontal;
            }
            if(horizontal)
                i = average.y;
            else
                i = average.x;
            positions.add(Integer.valueOf(i));
            horizontal = !horizontal;
            if(startNormal.dotProduct(direction) < 0)
            {
                i = endNormal.similarity(end.getAdded(endNormal.getScaled(10)));
                positions.add(Integer.valueOf(i));
                horizontal = !horizontal;
            }
        }
        if(horizontal)
            positions.add(Integer.valueOf(end.y));
        else
            positions.add(Integer.valueOf(end.x));
        processPositions(start, end, positions, startNormal.isHorizontal(), conn);
    }

    private Map<Integer, Integer> rowsUsed;
    private Map<Integer, Integer> colsUsed;
    private Map<Connection, ReservedInfo> reservedInfo;
    private static Ray UP = new Ray(0, -1);
    private static Ray DOWN = new Ray(0, 1);
    private static Ray LEFT = new Ray(-1, 0);
    private static Ray RIGHT = new Ray(1, 0);

}


/*
	DECOMPILATION REPORT

	Decompiled from: D:\eclipse3.2.2\plugins\org.eclipse.draw2d_3.2.0.v20060626.jar
	Total time: 15 ms
	Jad reported messages/errors:
	Exit status: 0
	Caught exceptions:
*/