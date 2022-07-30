# eye-color-guesser
Java project which guesses your eye color based off a close  up image.

This project uses a couple of steps to get to the color of the eye:
1. Edge detection through the canny algorithm
2. Determining the circular shapes through the circle hough transform.
3. Determining the dominant colors through  k-means clustering.
4. Translating the dominant hex colours to mnemonic names through a mapping table.


The project was made with java 11 and uses maven as a package manager, the only external library used is dagger which provides use of DI in the project.