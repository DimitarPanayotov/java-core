public class CourseScheduler {
    public static int maxNonOverlappingCourses(int[][] courses) {
        int count = 0;

        int n = courses.length;

        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (courses[j][1] < courses[minIndex][1]) {
                    minIndex = j;
                }
            }

            int tempStart = courses[i][0];
            int tempEnd = courses[i][1];
            courses[i][0] = courses[minIndex][0];
            courses[i][1] = courses[minIndex][1];
            courses[minIndex][0] = tempStart;
            courses[minIndex][1] = tempEnd;
        }

        int lastEndingHour = -1;
        for (int i = 0; i < n; i++) {
            if (courses[i][0] >= lastEndingHour) {
                count++;
                lastEndingHour = courses[i][1];
            }
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println(maxNonOverlappingCourses(new int[][] {
            {9, 11}, {10, 12}, {11, 13}, {15, 16}
        })); //3

        System.out.println(maxNonOverlappingCourses(new int[][] {
            {19, 22}, {17, 19}, {9, 12}, {9, 11}, {15, 17}, {15, 17}
        })); //4

        System.out.println(maxNonOverlappingCourses(new int[][] {
            {19, 22}
        })); //1

        System.out.println(maxNonOverlappingCourses(new int[][] {
            {13, 15}, {13, 17}, {11, 17}
        })); //1
    }
}
