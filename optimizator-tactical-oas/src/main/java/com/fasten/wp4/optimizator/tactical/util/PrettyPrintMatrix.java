package com.fasten.wp4.optimizator.tactical.util;

import static java.lang.String.format;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

public final class PrettyPrintMatrix {

    private static final char BORDER_KNOT = '+';
    private static final char HORIZONTAL_BORDER = '-';
    private static final char VERTICAL_BORDER = '|';

    private static final String DEFAULT_AS_NULL = "(NULL)";

    private final PrintStream out;
    private final String asNull;
    
    public static String printStr(Object[] table) {
		return printStr(new Object[][] {table});
	}

	public static String printStr(int[] table) {
		return printStr(new int[][] {table});
	}

	public static String printStr(double[] table) {
		return printStr(new double[][] {table});
	}

	public static String printStr(Object[][] table) {
		try {
			final Charset charset = StandardCharsets.UTF_8;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream ps= new PrintStream(baos, true, charset.name());
			new PrettyPrintMatrix(ps).print(table);
			String content = new String(baos.toByteArray(), charset);
			ps.close();
			baos.close();
			return content;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String printStr(int[][] table) {
		try {
			final Charset charset = StandardCharsets.UTF_8;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream ps= new PrintStream(baos, true, charset.name());
			new PrettyPrintMatrix(ps).print(table);
			String content = new String(baos.toByteArray(), charset);
			ps.close();
			baos.close();
			return content;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String printStr(double[][] table) {
		try {
			final Charset charset = StandardCharsets.UTF_8;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream ps= new PrintStream(baos, true, charset.name());
			new PrettyPrintMatrix(ps).print(table);
			String content = new String(baos.toByteArray(), charset);
			ps.close();
			baos.close();
			return content;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String printStr(Object[][][] table) {
		try {
			final Charset charset = StandardCharsets.UTF_8;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream ps= new PrintStream(baos, true, charset.name());
			new PrettyPrintMatrix(ps).print(table);
			String content = new String(baos.toByteArray(), charset);
			ps.close();
			baos.close();
			return content;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String printStr(double[][][] table) {
    	try {
    		final Charset charset = StandardCharsets.UTF_8;
    		ByteArrayOutputStream baos = new ByteArrayOutputStream();
    		PrintStream ps= new PrintStream(baos, true, charset.name());
    		new PrettyPrintMatrix(ps).print(table);
    		String content = new String(baos.toByteArray(), charset);
    		ps.close();
    		baos.close();
    		return content;
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return null;
    }

    public PrettyPrintMatrix(PrintStream out) {
        this(out, DEFAULT_AS_NULL);
    }

    public PrettyPrintMatrix(PrintStream out, String asNull) {
        if ( out == null ) {
            throw new IllegalArgumentException("No print stream provided");
        }
        if ( asNull == null ) {
            throw new IllegalArgumentException("No NULL-value placeholder provided");
        }
        this.out = out;
        this.asNull = asNull;
    }

    public void print(int[][] table) {
    	print(Arrays.stream(table).map(array -> Arrays.stream(array).boxed().toArray(Integer[]::new)).toArray(Integer[][]::new));
    }

    public void print(double[][] table) {
    	print(Arrays.stream(table).map(array -> Arrays.stream(array).boxed().toArray(Double[]::new)).toArray(Double[][]::new));
    }
    
    public void print(double[][][] table) {
    	
    	String[][] reduce = new String[table.length][];
    	
    	for (int c = 0; c < table.length; c++) {
    		String[] vector = new String[table[c].length];
			for (int w = 0; w < table[c].length; w++) {
				String result="";
				for (int s = 0; s < table[c][w].length; s++) {
					
					String[][] innerPrint = new String[1][1];
					innerPrint[0] = new String[table[c][w].length];
					result = DoubleStream.of(table[c][w]).boxed()
	                         .map(o->{return StringUtils.leftPad(o.toString(),10);})
	                         .collect(Collectors.joining(","));
					
				}
				vector[w]=result;
			}
			reduce[c]=vector;
		}
    	
    	print(reduce);
    }

    public void print(Object[][][] table) {
    	
    	String[][] reduce = new String[table.length][];
    	
    	for (int c = 0; c < table.length; c++) {
    		String[] vector = new String[table[c].length];
    		for (int w = 0; w < table[c].length; w++) {
    			String result="";
    			for (int s = 0; s < table[c][w].length; s++) {
    				
    				String[][] innerPrint = new String[1][1];
    				innerPrint[0] = new String[table[c][w].length];
    				result = Stream.of(table[c][w])
    						.map(o->{return StringUtils.leftPad((o!=null)?o.toString():asNull,10);})
    						.collect(Collectors.joining(","));
    				
    			}
    			vector[w]=result;
    		}
    		reduce[c]=vector;
    	}
    	
    	print(reduce);
    }
    
    public void print(Object[][] table) {
        if ( table == null ) {
            throw new IllegalArgumentException("No tabular data provided");
        }
        if ( table.length == 0 ) {
            return;
        }
        final int[] widths = new int[getMaxColumns(table)];
        adjustColumnWidths(table, widths);
        printPreparedTable(table, widths, getHorizontalBorder(widths));
    }

    private void printPreparedTable(Object[][] table, int widths[], String horizontalBorder) {
        final int lineLength = horizontalBorder.length();
        out.println(horizontalBorder);
        for ( final Object[] row : table ) {
            if ( row != null ) {
                out.println(getRow(row, widths, lineLength));
                out.println(horizontalBorder);
            }
        }
    }

    private String getRow(Object[] row, int[] widths, int lineLength) {
        final StringBuilder builder = new StringBuilder(lineLength).append(VERTICAL_BORDER);
        final int maxWidths = widths.length;
        for ( int i = 0; i < maxWidths; i++ ) {
            builder.append(padRight(getCellValue(safeGet(row, i, null)), widths[i])).append(VERTICAL_BORDER);
        }
        return builder.toString();
    }

    private String getHorizontalBorder(int[] widths) {
        final StringBuilder builder = new StringBuilder(256);
        builder.append(BORDER_KNOT);
        for ( final int w : widths ) {
            for ( int i = 0; i < w; i++ ) {
                builder.append(HORIZONTAL_BORDER);
            }
            builder.append(BORDER_KNOT);
        }
        return builder.toString();
    }

    private int getMaxColumns(Object[][] rows) {
        int max = 0;
        for ( final Object[] row : rows ) {
            if ( row != null && row.length > max ) {
                max = row.length;
            }
        }
        return max;
    }

    private void adjustColumnWidths(Object[][] rows, int[] widths) {
        for ( final Object[] row : rows ) {
            if ( row != null ) {
                for ( int c = 0; c < widths.length; c++ ) {
                    final String cv = getCellValue(safeGet(row, c, asNull));
                    final int l = cv.length();
                    if ( widths[c] < l ) {
                        widths[c] = l;
                    }
                }
            }
        }
    }

    private static String padRight(String s, int n) {
        return format("%1$-" + n + "s", s);
    }

    private static String safeGet(Object[] array, int index, String defaultValue) {
        return index < array.length ? ((array[index]!=null)?array[index].toString():null) : defaultValue;
    }

    private String getCellValue(Object value) {
        return value == null ? asNull : value.toString();
    }

}
