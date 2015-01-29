package usertracking.prototype.profile;

public class DWT {
	protected float seq1[];
	protected float seq2[];
	protected int wrapingPath[][];

	protected int m, n, K;
	protected double wrapingDistance;

	public DWT(float[] sample, float[] template) {
		seq1 = sample;
		seq2 = template;

		n = seq1.length;
		m = seq2.length;
		K = 1;

		wrapingPath = new int[n + m][2];
		wrapingDistance = 0.0;
		this.calculate();
	}

	public void calculate() {
		double accumulatedDistance = 0.0;
		double[][] d = new double[n][m];
		double[][] D = new double[n][m];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				d[i][j] = distanceBetween(seq1[i], seq2[j]);
			}
		}

		D[0][0] = d[0][0];

		for (int i = 1; i < n; i++) {
			D[i][0] = d[i][0] + D[i - 1][0];
		}

		for (int j = 1; j < m; j++) {
			D[0][j] = d[0][j] + D[0][j - 1];
		}

		for (int i = 1; i < n; i++) {
			for (int j = 1; j < m; j++) {
				accumulatedDistance = Math.min(Math.min(D[i - 1][j], D[i - 1][j - 1]), D[i][j-1]);
				accumulatedDistance += d[i][j];
				D[i][j] = accumulatedDistance;
			}
		}

		accumulatedDistance = D[n - 1][m - 1];

		int i = n - 1;
		int j = m - 1;
		int minIidex = 1;

		wrapingPath[K - 1][0] = i;
		wrapingPath[K - 1][1] = j;

		while ((i + j) != 0) {
			if (i == 0) {
				j -= 1;
			} else if (j == 0) {
				i -= 1;
			} else {
				double[] array = { D[i - 1][j], D[i][j - 1], D[i - 1][j - 1] };
				minIidex = this.getIndexOfMinimum(array);

				if (minIidex == 0) {
					i -= 1;
				} else if (minIidex == 1) {
					j -= 1;
				} else if (minIidex == 2) {
					i -= 1;
					j -= 1;
				}
			}
			K++;
			wrapingPath[K - 1][0] = i;
			wrapingPath[K - 1][1] = j;
		}
		wrapingDistance = accumulatedDistance / K;
		this.reversePath(wrapingPath);
	}

	private void reversePath(int[][] path) {
		int[][] newPath = new int[K][2];
		for (int i = 0; i < K; i++) {
			for (int j = 0; j < 2; j++) {
				newPath[i][j] = path[K - i - 1][j];
			}
		}
		wrapingPath = newPath;
	}

	private int getIndexOfMinimum(double[] array) {
		int index = 0;
		double val = array[0];

		for (int i = 1; i < array.length; i++) {
			if (array[i] < val) {
				val = array[i];
				index = i;
			}
		}

		return index;
	}

	public String toString() {
		String retVal = "Wrapping distance " + wrapingDistance + "\n";
		retVal += "Wrapping path: {";
		for (int i = 0; i < K; i++) {
			retVal += "(" + wrapingPath[i][0] + ", " + wrapingPath[i][1] + ")";
			retVal += (i == K - 1) ? "}" : ", ";
		}
		return retVal;
	}

	private double distanceBetween(float f, float g) {
		return (f - g) * (f - g);
	}
	
	
	public static void main(String[] args){
		float[] n2 = {1.3f, 3.7f, 4.1f, 3.3f};
		float[] n1 = {1.5f, 3.9f, 4.1f};
		
		DWT dwt = new DWT(n1, n2);
		System.out.println(dwt);
	}
}
