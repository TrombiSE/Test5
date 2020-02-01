import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;

public class Test {

    static ArrayList<Integer> g[];
    static int ans[];

    public static void main(String[] args) {
        InputReader in = new InputReader(System.in);
        PrintWriter w = new PrintWriter(System.out);

        int n = in.nextInt();
        int m = in.nextInt();
        int a[] = new int[m];
        int b[] = new int[m];
        for (int i = 0; i < m; i++) {
            int x = in.nextInt() - 1;
            int y = in.nextInt() - 1;
            int z = in.nextInt();
            a[z] = x;
            b[z] = y;
        }

        DisjointSet ds = new DisjointSet(n);
        ans = new int[3 * m];

        g = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            g[i] = new ArrayList<Integer>();
        }

        for (int i = 0; i < m; i++) {
            if (ds.find(a[i]) != ds.find(b[i])) {
                g[a[i]].add(b[i]);
                g[a[i]].add(i);
                g[b[i]].add(a[i]);
                g[b[i]].add(i);
                ds.merge(a[i], b[i]);
                // System.out.println(a[i] + " " + b[i]);
            }
        }

        dfs(0, -1);

        int max = 0;
        for (int i = 0; i < ans.length; i++)
            if (ans[i] > 0) {
                max = i;
            }

        for (int i = max; i >= 0; i--) {
            w.print(ans[i]);
        }
        w.println();

        w.close();
    }

    static int dfs(int curr, int prev) {
        int st = 1;
        for (int i = 0; i < g[curr].size();) {
            int v = g[curr].get(i++);
            int h = g[curr].get(i++);
            if (v == prev) {
                continue;
            }
            int ss = dfs(v, curr);
            long times = ss * 1L * (g.length - ss);
            int idx = h;
            // System.out.println(curr + " " + v + " " + ss + " " + idx + " " + times);
            int carry = 0;
            while (times >= 1 || carry > 0) {
                carry = add(idx, carry + (int) (times & 1));
                times >>= 1;
                idx++;
            }
            // System.out.println(ans[0] + " " + ans[1] + " " + ans[2] + " " + ans[3]);
            st += ss;
        }
        return st;
    }

    static public int add(int idx, int val) {
        ans[idx] += val;
        int ret = Math.max(0, ans[idx] >> 1);
        ans[idx] = (ans[idx] & 1);
        return ret;
    }

    static public class DisjointSet {

        public int rank[], parent[], size[];
        public int n, comp;

        public DisjointSet(int n) {
            this.n = n;
            makeSet();
        }

        public void makeSet() {
            rank = new int[n];
            parent = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
            size = new int[n];
            Arrays.fill(size, 1);
            comp = n;
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        public void merge(int x, int y) {
            int xRoot = find(x);
            int yRoot = find(y);

            if (xRoot == yRoot)
                return;

            size[xRoot] = size[yRoot] = size[xRoot] + size[yRoot];

            if (rank[xRoot] < rank[yRoot]) {
                parent[xRoot] = yRoot;
            } else {
                parent[yRoot] = xRoot;
                if (rank[xRoot] == rank[yRoot]) {
                    rank[xRoot]++;
                }
            }

            comp--;
        }

    }

    static class InputReader {

        private final InputStream stream;
        private final byte[] buf = new byte[8192];
        private int curChar, snumChars;
        private SpaceCharFilter filter;

        public InputReader(InputStream stream) {
            this.stream = stream;
        }

        public int snext() {
            if (snumChars == -1)
                throw new InputMismatchException();
            if (curChar >= snumChars) {
                curChar = 0;
                try {
                    snumChars = stream.read(buf);
                } catch (IOException e) {
                    throw new InputMismatchException();
                }
                if (snumChars <= 0)
                    return -1;
            }
            return buf[curChar++];
        }

        public int nextInt() {
            int c = snext();
            while (isSpaceChar(c)) {
                c = snext();
            }
            int sgn = 1;
            if (c == '-') {
                sgn = -1;
                c = snext();
            }
            int res = 0;
            do {
                if (c < '0' || c > '9')
                    throw new InputMismatchException();
                res *= 10;
                res += c - '0';
                c = snext();
            } while (!isSpaceChar(c));
            return res * sgn;
        }

        public long nextLong() {
            int c = snext();
            while (isSpaceChar(c)) {
                c = snext();
            }
            int sgn = 1;
            if (c == '-') {
                sgn = -1;
                c = snext();
            }
            long res = 0;
            do {
                if (c < '0' || c > '9')
                    throw new InputMismatchException();
                res *= 10;
                res += c - '0';
                c = snext();
            } while (!isSpaceChar(c));
            return res * sgn;
        }

        public int[] nextIntArray(int n) {
            int a[] = new int[n];
            for (int i = 0; i < n; i++) {
                a[i] = nextInt();
            }
            return a;
        }

        public String readString() {
            int c = snext();
            while (isSpaceChar(c)) {
                c = snext();
            }
            StringBuilder res = new StringBuilder();
            do {
                res.appendCodePoint(c);
                c = snext();
            } while (!isSpaceChar(c));
            return res.toString();
        }

        public boolean isSpaceChar(int c) {
            if (filter != null)
                return filter.isSpaceChar(c);
            return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
        }

        public interface SpaceCharFilter {
            public boolean isSpaceChar(int ch);
        }
    }
}